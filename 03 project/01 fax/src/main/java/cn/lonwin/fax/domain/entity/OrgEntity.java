package cn.lonwin.fax.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wizard.official.platform.spring.hibernate.database.EntityTemplate;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "T_ORG")
public class OrgEntity extends EntityTemplate {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -6951417015401331196L;

	@Id
	@Column(name = "CODE", length = 30)
	private String code = null;

	@Column(name = "NAME", length = 100)
	private String name = null;

	@Column(name = "PARENT_CODE", length = 30)
	private String parentCode = null;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentCode() {
		return parentCode;
	}

	public void setParentCode(String parentCode) {
		this.parentCode = parentCode;
	}

}
