package XMLReadAndWrite.XMLReadAndWrite;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import zju.edu.cn.apm.urlcombine.io.model.URLTreeNode;
import zju.edu.cn.apm.urlcombine.io.model.XMLTreeNode;

/**
 * 
 * @ClassName: ConfigXmlHandler
 * @Description: TODO(xml配置文件加载辅助类，将配置文件映射成javabean)
 * @author ishadow comegogo@yeah.net
 * @date Sep 21, 2016 5:04:04 PM
 */
public class ConfigXmlHandler extends DefaultHandler {

	private String preTag = null;// 作用是记录解析时的上一个节点名称
	private XMLTreeNode xmlTreeNode = null;

	private static final String TREE = "tree";
	private static final String ROOTNODE = "rootnode";
	private static final String TREENODE = "treenode";
	private static final String NAME = "name";
	private static final String PARENT = "parent";
	private static final String CHILD = "child";
	private static final String GRANDSON = "grandson";

	@Override
	public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
		try {
			if (TREE.equals(qName)) {
				ControllerConfig.getXmlTreeNodesMap().put(attributes.getValue(ROOTNODE),new ConcurrentHashMap<String, XMLTreeNode>());
				ControllerConfig.getUrlTreeNodesMap().put(attributes.getValue(ROOTNODE),new ConcurrentHashMap<String, URLTreeNode>());
			} else if (TREENODE.equals(qName)) {
				xmlTreeNode = new XMLTreeNode();
				xmlTreeNode.setRootnode(attributes.getValue(ROOTNODE));
				xmlTreeNode.setName(attributes.getValue(NAME));
				xmlTreeNode.setParent(attributes.getValue(PARENT));
				xmlTreeNode.setChild(attributes.getValue(CHILD));
				xmlTreeNode.setGrandson(attributes.getValue(GRANDSON));
			}
			preTag = qName;
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}

	@Override
	public void endElement(String uri, String localName, String qName) throws SAXException {
		if (TREENODE.equals(qName)) {
			ControllerConfig.getXmlTreeNodesMap().get(xmlTreeNode.getRootnode()).put(xmlTreeNode.getName(),xmlTreeNode);
			xmlTreeNode = null;
		}
		preTag = null;
	}

	/**
	 * 加载单条记录时用到，在本例中暂时没使用
	 */
	@Override
	public void characters(char[] ch, int start, int length) throws SAXException {
		if (preTag != null) {
			if (length > 0) {
				String content = new String(ch, start, length);
			}
		}
	}
}
