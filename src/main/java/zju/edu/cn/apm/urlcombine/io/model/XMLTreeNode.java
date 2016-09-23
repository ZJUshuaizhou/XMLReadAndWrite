package zju.edu.cn.apm.urlcombine.io.model;
/**
 * 
 * @ClassName: XMLTreeNode
 * @Description: TODO(对照xml生成treenode节点)
 * @author ishadow comegogo@yeah.net
 * @date Sep 21, 2016 6:05:34 PM
 */
public class XMLTreeNode {
	private String rootnode;
	private String name;
	private String parent;
	private String child;
	private String grandson;
	
	public XMLTreeNode() {
		// TODO Auto-generated constructor stub
	}
	
	public XMLTreeNode(String name,String parent,String child,String grandson) {
		this.name=name;
		this.parent=parent;
		this.child=child;
		this.grandson=grandson;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParent() {
		return parent;
	}

	public void setParent(String parent) {
		this.parent = parent;
	}

	public String getChild() {
		return child;
	}

	public void setChild(String child) {
		this.child = child;
	}

	public String getGrandson() {
		return grandson;
	}

	public void setGrandson(String grandson) {
		this.grandson = grandson;
	}
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "[xmlTreeNode rootnode : "+rootnode+" ; name : "+name+" ; parent : "+parent+" ; child : "+child+" ; grandson : "+grandson+";]";
	}

	public String getRootnode() {
		return rootnode;
	}

	public void setRootnode(String rootnode) {
		this.rootnode = rootnode;
	}
}
