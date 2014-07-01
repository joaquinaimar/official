package cn.lonwin.fax.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.wizard.official.platform.spring.hibernate.database.EntityTemplate;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "T_PERSON")
public class PersonEntity extends EntityTemplate {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4779949685807626102L;

	@Id
	@Column(name = "CODE", length = 30)
	private String code = null;

	@Column(name = "NAME", length = 100)
	private String name = null;

	@Column(name = "EMAIL", length = 20)
	private String email = null;

	@Column(name = "ORG_CODE", length = 20)
	private String orgCode = null;

	@Column(name = "USERNAME", length = 50)
	private String username = null;

	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.DELETE })
	@JsonIgnore
	@JoinColumn(name = "ORG_CODE", insertable = false, updatable = false)
	private OrgEntity org = null;

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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getOrgCode() {
		return orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	public OrgEntity getOrg() {
		return org;
	}

	public void setOrg(OrgEntity org) {
		this.org = org;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
