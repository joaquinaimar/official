package com.wizard.official.platform.spring.hibernate.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.wizard.official.platform.spring.hibernate.database.EntityTemplate;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "T_SYS_USER_INFO")
public class UserInfoEntity extends EntityTemplate {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1984864350593342427L;

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", length = 36, nullable = false, unique = true)
	private String id = null;

	@Column(name = "USERNAME", length = 50)
	private String username = null;

	@Column(name = "PASSWORD", length = 50)
	private String password = null;

	@Column(name = "ROLE_NAME", length = 50)
	private String rolename = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRolename() {
		return rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}

}
