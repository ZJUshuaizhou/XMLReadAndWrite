package XMLReadAndWrite.XMLReadAndWrite;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import zju.edu.cn.apm.urlcombine.io.model.URLTreeNode;
import zju.edu.cn.apm.urlcombine.io.model.XMLTreeNode;

/**
 * 
 * @ClassName: WriteXML
 * @Description: TODO(这里用一句话描述这个类的作用)
 * @author ishadow comegogo@yeah.net
 * @date Sep 21, 2016 4:27:42 PM
 */
public class WriteXML {
	private static Logger LOGGER = Logger.getLogger(WriteXML.class);
	private static String CONFIG = "config";
	private static String TREE = "tree";
	private static String TREENODE = "treenode";
	private static String ROOTNODE = "rootnode";
	private static String NAME = "name";
	private static String PARENT = "parent";
	private static String CHILD = "child";
	private static String GRANDSON = "grandson";
	private static Map<String, Map<String, URLTreeNode>> urlTreeNodesMap = new ConcurrentHashMap<String, Map<String, URLTreeNode>>();
	private static Map<String, Map<String, XMLTreeNode>> xmlTreeNodesMap = new ConcurrentHashMap<String, Map<String, XMLTreeNode>>();

	public static void main(String[] args) {
		// WriteXML writeXML = new WriteXML();
		// writeXML.structureTreeNode();
		ControllerConfig.init();
		Map<String, URLTreeNode> rootnodes = ControllerConfig.getRootnodes();
		
		WriteXML wXml=new WriteXML();
		wXml.exportTree(rootnodes, "/home/ishadow/Desktop/test1.xml");
//		for (Map.Entry<String, URLTreeNode> entrychild : rootnodes.entrySet()) {
//			urlTreeNodesMap.put(entrychild.getKey(), new ConcurrentHashMap<String, URLTreeNode>());
//			urlTreeNodesMap.get(entrychild.getKey()).put(entrychild.getKey(), entrychild.getValue());
//			trasvelUrlTree(entrychild.getKey(), entrychild.getValue());
//			LOGGER.debug(entrychild.getKey());
//		}
//
//		for (Map.Entry<String, Map<String, URLTreeNode>> entry : urlTreeNodesMap.entrySet()) {
//			LOGGER.debug("rootnod : ----------------" + entry.getKey() + "------------------");
//			ControllerConfig.trasvel(entry.getValue());
//		}
//		transformFromURLNodesToXMLNodes();
//		for (Map.Entry<String, Map<String, XMLTreeNode>> entry : xmlTreeNodesMap.entrySet()) {
//			LOGGER.debug("rootnod : ----------------" + entry.getKey() + "------------------");
//			ControllerConfig.trasvelXML(entry.getValue()); 
//		}
//		String path="/home/ishadow/Desktop/test1.xml";
//		structureTreeNode(path);
	}
	
	public boolean exportTree(Map<String, URLTreeNode> rootnodes,String path) {
		try {
			for (Map.Entry<String, URLTreeNode> entrychild : rootnodes.entrySet()) {
				urlTreeNodesMap.put(entrychild.getKey(), new ConcurrentHashMap<String, URLTreeNode>());
				urlTreeNodesMap.get(entrychild.getKey()).put(entrychild.getKey(), entrychild.getValue());
				trasvelUrlTree(entrychild.getKey(), entrychild.getValue());
				LOGGER.debug(entrychild.getKey());
			}

			for (Map.Entry<String, Map<String, URLTreeNode>> entry : urlTreeNodesMap.entrySet()) {
				LOGGER.debug("rootnod : ----------------" + entry.getKey() + "------------------");
				ControllerConfig.trasvel(entry.getValue());
			}
			transformFromURLNodesToXMLNodes();
			for (Map.Entry<String, Map<String, XMLTreeNode>> entry : xmlTreeNodesMap.entrySet()) {
				LOGGER.debug("rootnod : ----------------" + entry.getKey() + "------------------");
				ControllerConfig.trasvelXML(entry.getValue()); 
			}
//			String path="/home/ishadow/Desktop/test1.xml";
			structureTreeNode(path);
		} catch (Exception e) {
			// TODO: handle exception
			LOGGER.error(e.toString());;
		}
		
		return true;
	}

	/**
	 * 在tree下面生成treenode元素 由参数生成xml中treenode的重复代码提取
	 * 
	 * @param document
	 * @param name
	 *            树节点的名称
	 * @param parent
	 *            树的父节点
	 * @param child
	 *            树的子节点集合
	 * @param grandson
	 *            树的孙子节点集合
	 * @return Element 类型的节点
	 */
	public  Element addTreeNode(Document document, String rootNodeName,String name, String parent, String child, String grandson) {

		Element treenode = document.createElement(TREENODE);
		
		Attr treeRootNode = document.createAttribute(ROOTNODE);
		Attr treeNodeName = document.createAttribute(NAME);
		Attr treeNodeParent = document.createAttribute(PARENT);
		Attr treeNodeChild = document.createAttribute(CHILD);
		Attr treeNodeGrandson = document.createAttribute(GRANDSON);
		
		treeRootNode.setValue(rootNodeName);
		treeNodeName.setValue(name);
		treeNodeParent.setValue(parent);
		treeNodeChild.setValue(child);
		treeNodeGrandson.setValue(grandson);

		treenode.setAttributeNode(treeRootNode);
		treenode.setAttributeNode(treeNodeName);
		treenode.setAttributeNode(treeNodeParent);
		treenode.setAttributeNode(treeNodeChild);
		treenode.setAttributeNode(treeNodeGrandson);

		return treenode;
	}

	/**
	 * 添加属性节点
	 * 
	 * @param document
	 *            全局document
	 * @param rootName
	 *            根节点的名称
	 * @return Attr类型的属性节点
	 */
	public static Attr addRootNode(Document document, String rootName) {
		Attr rootNodeName = document.createAttribute(ROOTNODE);
		rootNodeName.setValue(rootName);
		return rootNodeName;
	}

	/**
	 * 整体整合xml
	 */
	public  void structureTreeNode(String path) {

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.newDocument();
			Element config = document.createElement(CONFIG);

			/**
			 * 添加第一个树
			 */
			document.appendChild(config);
			
			
			for (Map.Entry<String, Map<String, XMLTreeNode>> entry : xmlTreeNodesMap.entrySet()) {
				Element tree = document.createElement(TREE);
				config.appendChild(tree);
				tree.setAttributeNode(addRootNode(document,entry.getKey()));
				LOGGER.debug("rootnod : ----------------" + entry.getKey() + "------------------");
				for (Map.Entry<String, XMLTreeNode> xmlnode : entry.getValue().entrySet()) {
					tree.appendChild(addTreeNode(document, xmlnode.getValue().getRootnode(),xmlnode.getValue().getName(),xmlnode.getValue().getParent(),xmlnode.getValue().getChild(),xmlnode.getValue().getGrandson()));
					LOGGER.debug("rootnod : ----------------" + xmlnode.getKey() + "------------------");
				}
			}
			try {
				FileOutputStream fos = new FileOutputStream(new File(path));

				try {
					((org.apache.crimson.tree.XmlDocument) document).write(fos);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				try {
					fos.flush();
					fos.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @Title: trasvelUrlTree @Description: TODO 由根节点遍历整个树 @param rootNodeName
	 *         根节点名称 @param rootnode @throws
	 */
	public  void trasvelUrlTree(String rootNodeName, URLTreeNode rootnode) {
		if (rootnode.getChild() != null && rootnode.getChild().size() > 0) {
			for (Map.Entry<String, URLTreeNode> entrychild : rootnode.getChild().entrySet()) {
				LOGGER.debug(entrychild.getKey() + "  ---  " + rootNodeName);
				urlTreeNodesMap.get(rootNodeName).put(entrychild.getKey(), entrychild.getValue());
				trasvelUrlTree(rootNodeName, entrychild.getValue());
				LOGGER.debug(entrychild.getKey());
			}
		}
	}

	public  void transformFromURLNodesToXMLNodes() {
		for (Map.Entry<String, Map<String, URLTreeNode>> urlTreeNodes : urlTreeNodesMap.entrySet()) {

			if (!xmlTreeNodesMap.containsKey(urlTreeNodes.getKey())) {
				xmlTreeNodesMap.put(urlTreeNodes.getKey(), new ConcurrentHashMap<String, XMLTreeNode>());
			}

			for (Map.Entry<String, URLTreeNode> entry : urlTreeNodes.getValue().entrySet()) {
				XMLTreeNode temple = new XMLTreeNode();
				// 设置根节点信息rootnode
				temple.setRootnode(urlTreeNodes.getKey());
				// 设置节点名称name
				StringBuffer output = new StringBuffer();
				output.append("node:[" + entry.getValue().getName() + ";");
				temple.setName(entry.getValue().getName());
				// 设置父节点信息parent

				if (entry.getValue().getParent() != null) {
					temple.setParent(entry.getValue().getParent().getName());
					output.append(entry.getValue().getParent().getName() + ";");
				} else {
					output.append("null;");
					temple.setParent("null");
				}
				// 设置子节点集合的信息
				StringBuffer childString = new StringBuffer();
				if (entry.getValue().getChild().entrySet().size() > 0) {
					for (Map.Entry<String, URLTreeNode> entrychild : entry.getValue().getChild().entrySet()) {
						childString.append(entrychild.getValue().getName() + " ");
						output.append(entrychild.getValue().getName() + " ");
					}
				} else {
					output.append("null");
					childString.append("null");
				}
				temple.setChild(childString.toString().trim());

				output.append(";");

				// 设置孙子节点集合的信息
				StringBuffer grandsonString = new StringBuffer();
				if (entry.getValue().getGrandson().entrySet().size() > 0) {
					for (Map.Entry<String, URLTreeNode> entrygrandson : entry.getValue().getGrandson().entrySet()) {
						grandsonString.append(entrygrandson.getValue().getName() + " ");
						output.append(entrygrandson.getValue().getName() + " ");
					}
				} else {
					grandsonString.append("null");
					output.append("null");
				}
				temple.setGrandson(grandsonString.toString().trim());
				output.append("]");

				LOGGER.debug(output.toString());
				xmlTreeNodesMap.get(urlTreeNodes.getKey()).put(temple.getName(), temple);
			}
		}
	}

	public  Map<String, XMLTreeNode> transformFromUrltreeToXmlTree(Map<String, URLTreeNode> outputNodes) {
		for (Map.Entry<String, URLTreeNode> entry : outputNodes.entrySet()) {
			StringBuffer output = new StringBuffer();
			output.append("node:[" + entry.getValue().getName() + ";");
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
		return null;
	}
}
