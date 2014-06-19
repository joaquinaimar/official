package cn.lonwin.fax.domain.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.GenericGenerator;

import com.wizard.official.platform.spring.hibernate.database.EntityTemplate;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "T_FAX")
public class FaxEntity extends EntityTemplate {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -543753425077406948L;

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", length = 36)
	private String id = null;

	@Column(name = "TITLE", length = 50)
	private String title = null;

	@Column(name = "CONTENT", length = 1000)
	private String content = null;

	@Column(name = "SEND_USER", length = 50)
	private String sendUser = null;

	@Column(name = "RECEIVE_USER", length = 50)
	private String receiveUser = null;

	@Column(name = "TYPE", length = 1)
	private Integer type = null;

	@Column(name = "STATUS", length = 5)
	private Integer status = null;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "SAVE_TIME")
	private Date saveTime = null;

	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.DELETE })
	@JsonIgnore
	@JoinColumn(name = "SEND_USER", insertable = false, updatable = false)
	private PersonEntity sendPerson = null;

	@ManyToOne(fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.DELETE })
	@JsonIgnore
	@JoinColumn(name = "RECEIVE_USER", insertable = false, updatable = false)
	private PersonEntity receivePerson = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getSendUser() {
		return sendUser;
	}

	public void setSendUser(String sendUser) {
		this.sendUser = sendUser;
	}

	public String getReceiveUser() {
		return receiveUser;
	}

	public void setReceiveUser(String receiveUser) {
		this.receiveUser = receiveUser;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getSaveTime() {
		return saveTime;
	}

	public void setSaveTime(Date saveTime) {
		this.saveTime = saveTime;
	}

	public PersonEntity getSendPerson() {
		return sendPerson;
	}

	public void setSendPerson(PersonEntity sendPerson) {
		this.sendPerson = sendPerson;
	}

	public PersonEntity getReceivePerson() {
		return receivePerson;
	}

	public void setReceivePerson(PersonEntity receivePerson) {
		this.receivePerson = receivePerson;
	}

}
