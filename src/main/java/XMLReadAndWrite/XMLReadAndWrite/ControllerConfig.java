package XMLReadAndWrite.XMLReadAndWrite;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;

import zju.edu.cn.apm.urlcombine.io.model.URLTreeNode;
import zju.edu.cn.apm.urlcombine.io.model.XMLTreeNode;

/**
 * 
 * @ClassName: ControllerConfig
 * @Description: TODO(控制加载配置文件)
 * @author ishadow comegogo@yeah.net
 * @date Sep 21, 2016 5:55:15 PM
 */
public class ControllerConfig {
	private static Logger LOGGER = Logger.getLogger(ControllerConfig.class);
	private final static String XMLPATH = "testTree.xml";
	private final static String SPLITFLAG = " ";
	private static Map<String, URLTreeNode> rootnodes = new ConcurrentHashMap<String, URLTreeNode>();
	private static Map<String, Map<String, URLTreeNode>> urlTreeNodesMap = new ConcurrentHashMap<String, Map<String, URLTreeNode>>();
	private static Map<String, Map<String, XMLTreeNode>> xmlTreeNodesMap = new ConcurrentHashMap<String, Map<String, XMLTreeNode>>();
	
	public Map<String, URLTreeNode> importTree() {
		init();
		return rootnodes;
	}
	/**
	 * 
	 * @Title: init @Description: TODO(从xml读取内容将程序变量初始化) @throws
	 */
	public static void init() {
		InputStream is = ControllerConfig.class.getClassLoader().getResourceAsStream(XMLPATH);
		register(is);
		/**
		 * 根据xml映射将所有节点进行新建
		 */
		for (Map.Entry<String, Map<String, XMLTreeNode>> entry : xmlTreeNodesMap.entrySet()) {
			for (Map.Entry<String, XMLTreeNode> entryXmlTreeNode : entry.getValue().entrySet()) {
				urlTreeNodesMap.get(entry.getKey()).put(entryXmlTreeNode.getKey(),
						new URLTreeNode(entryXmlTreeNode.getKey(), null));
				LOGGER.debug(entryXmlTreeNode.toString());
			}
		}
		/**
		 * 根据xml映射将所有节点的关系进行关联
		 */
		for (Map.Entry<String, Map<String, XMLTreeNode>> entry : xmlTreeNodesMap.entrySet()) {
			nodesRelationshipRecovery(entry.getValue(), urlTreeNodesMap.get(entry.getKey()));
		}
		/**
		 * 遍历输出所有节点
		 */
		for (Map.Entry<String, Map<String, URLTreeNode>> entry : urlTreeNodesMap.entrySet()) {
			trasvel(entry.getValue());
			rootnodes.put(entry.getKey(), entry.getValue().get(entry.getKey()));
//			System.out.println("rootnodes------------------------------------------------------");
//			trasvel(rootnodes);
//			System.out.println("rootnodes------------------------------------------------------");
		}
		
	}

	/**
	 * 
	 * @Title: trasvel @Description: TODO(遍历输出树的url内存节点信息)
	 * @throws
	 */
	public static void trasvel(Map<String, URLTreeNode> outputNodes) {
		for (Map.Entry<String, URLTreeNode> entry : outputNodes.entrySet()) {
			StringBuffer output = new StringBuffer();
			output.append("node:["+ entry.getValue().getName() + ";");
			if (entry.getValue().getParent() != null) {
				output.append(entry.getValue().getParent().getName() + ";");
			} else {
				output.append("null;");
			}
			if (entry.getValue().getChild().entrySet().size() > 0) {
				for (Map.Entry<String, URLTreeNode> entrychild : entry.getValue().getChild().entrySet()) {
					output.append(entrychild.getValue().getName() + " ");
				}
			} else {
				output.append("null");
			}
			output.append(";");
			if (entry.getValue().getGrandson().entrySet().size() > 0) {
				for (Map.Entry<String, URLTreeNode> entrygrandson : entry.getValue().getGrandson().entrySet()) {
					output.append(entrygrandson.getValue().getName() + " ");
				}
			} else {
				output.append("null");
			}
			output.append("]");
			LOGGER.debug(output.toString());
		}
	}
	/**
	 * 
	 * @Title: trasvelXML
	 * @Description: TODO 遍历输出树的XML存储节点信息
	 * @param outputXMLNodes
	 * @throws
	 */
	public static void trasvelXML(Map<String,XMLTreeNode> outputXMLNodes) {
		for (Map.Entry<String, XMLTreeNode> entry : outputXMLNodes.entrySet()) {
			StringBuffer output = new StringBuffer();
			output.append("node:["+ entry.getValue().getRootnode() + ";");
			output.append(entry.getValue().getName() + ";");
			output.append(entry.getValue().getParent() + ";");
			output.append(entry.getValue().getChild() + ";");
			output.append(entry.getValue().getGrandson() + ";");
			output.append("]");
			LOGGER.debug(output.toString());
		}
	}

	/**
	 * @Title: nodesRelationshipRecovery @Description: TODO(关联节点间关系)
	 *         由于所有节点都进行了新建，但是节点之间的关系并没有进行关联。第二遍加载，将节点之间的关系进行关联 @throws
	 */
	public static void nodesRelationshipRecovery(Map<String, XMLTreeNode> relationSource,
			Map<String, URLTreeNode> relationDestination) {
		try {
			for (Map.Entry<String, XMLTreeNode> entry : relationSource.entrySet()) {
				// 得到需要进行关联关系的节点
				URLTreeNode temple = relationDestination.get(entry.getValue().getName());
				// 关联父节点，父节点只有一个
				if (entry.getValue().getParent() != null && !entry.getValue().getParent().equals("null")) {
					temple.setParent(relationDestination.get(entry.getValue().getParent()));
				}
				// 关联子节点
				if (entry.getValue().getChild() != null && !entry.getValue().getChild().equals("null")) {
					String[] childArray = entry.getValue().getChild().split(SPLITFLAG);
					for (int i = 0; i < childArray.length; i++) {
						temple.getChild().put(childArray[i], relationDestination.get(childArray[i]));
					}
				}
				// 关联孙子节点
				if (entry.getValue().getGrandson() != null && !entry.getValue().getGrandson().equals("null")) {
					String[] grandsonArray = entry.getValue().getGrandson().split(SPLITFLAG);
					for (int i = 0; i < grandsonArray.length; i++) {
						temple.getGrandson().put(grandsonArray[i], relationDestination.get(grandsonArray[i]));
					}
				}
//				LOGGER.debug(entry.getKey());
			}
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.info(e.toString());
		}

	}

	/**
	 * 
	 * @Title: register @Description: TODO(调用库函数和加载处理类进行加载指定文件读入流) @param
	 *         is @throws
	 */
	public static void register(InputStream is) {
		try {
			SAXParserFactory factory = SAXParserFactory.newInstance();
			ConfigXmlHandler dh = new ConfigXmlHandler();
			factory.newSAXParser().parse(is, dh);
		} catch (Exception e) {
			LOGGER.error("register xml error!  "+e.toString());
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					LOGGER.error("close inputstream xml error.");
				}
			}
		}
	}

	/**
	 * 有问题。。。。。这里尽量不要去新建node。。。。 
	 * @Title:transferStringToURLTreeNode 
	 * @Description: TODO 将字符串转化称treenode节点集合 例如将：e f h.转换成3个urltreenode的map返回 
	 * @param input 需要转换的字符串 
	 * @return 转换过后的节点集合 
	 * @throws
	 */
	public Map<String, URLTreeNode> transferStringToURLTreeNode(String input) {
		Map<String, URLTreeNode> result = new ConcurrentHashMap<String, URLTreeNode>();
		if (input == null || input.trim().length() <= 0) {
			// 不返回null。返回null的话，如果调用本方法的方法不做安全检查的话，很容易使调用本方法的方法直接崩溃掉。
			return result;
		}
		String[] inputNodeSource = input.split(SPLITFLAG);
		for (String nodename : inputNodeSource) {
			result.put(nodename, new URLTreeNode(nodename, null));
		}
		return result;
	}

	public static Map<String, Map<String, XMLTreeNode>> getXmlTreeNodesMap() {
		return xmlTreeNodesMap;
	}

	public static void setXmlTreeNodesMap(Map<String, Map<String, XMLTreeNode>> xmlTreeNodesMap) {
		ControllerConfig.xmlTreeNodesMap = xmlTreeNodesMap;
	}

	public static Map<String, Map<String, URLTreeNode>> getUrlTreeNodesMap() {
		return urlTreeNodesMap;
	}

	public static void setUrlTreeNodesMap(Map<String, Map<String, URLTreeNode>> urlTreeNodesMap) {
		ControllerConfig.urlTreeNodesMap = urlTreeNodesMap;
	}

	public static Map<String, URLTreeNode> getRootnodes() {
		return rootnodes;
	}

	public static void setRootnodes(Map<String, URLTreeNode> rootnodes) {
		ControllerConfig.rootnodes = rootnodes;
	}
}
