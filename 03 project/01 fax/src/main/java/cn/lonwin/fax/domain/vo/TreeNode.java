package cn.lonwin.fax.domain.vo;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TreeNode {

	private String id = null;

	private String code = null;

	private String text = null;

	private String type = null;

	private List<TreeNode> children = null;

	private Boolean checked = null;

	public String getId() {
		id = UUID.randomUUID().toString();
		return id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void addChildren(TreeNode child) {
		if (null == children)
			children = new ArrayList<TreeNode>();
		children.add(child);
	}

	public List<TreeNode> getChildren() {
		return children;
	}

	public void setChildren(List<TreeNode> children) {
		this.children = children;
	}

	public Boolean getLeaf() {
		return !(null != children && 0 != children.size());
	}

	public Boolean getChecked() {
		return checked;
	}

	public void setChecked(Boolean checked) {
		this.checked = checked;
	}

}
