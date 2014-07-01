package cn.lonwin.fax.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

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
@Table(name = "T_RECEIVE_USER")
public class ReceiveUserEntity extends EntityTemplate {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 7336024286004666527L;

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", length = 36)
	private String id = null;

	@Column(name = "FAX_ID", length = 36)
	private String faxId = null;

	@Column(name = "RECEIVE_CODE", length = 30)
	private String receiveCode = null;

	@Column(name = "TYPE", length = 1)
	private Integer type = null;

	@OneToOne(fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.DELETE })
	@JsonIgnore
	@JoinColumn(name = "RECEIVE_CODE", insertable = false, updatable = false)
	private PersonEntity person = null;

	@OneToOne(fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.DELETE })
	@JsonIgnore
	@JoinColumn(name = "RECEIVE_CODE", insertable = false, updatable = false)
	private OrgEntity org = null;

	@OneToOne(fetch = FetchType.LAZY)
	@Cascade(value = { CascadeType.DELETE })
	@JsonIgnore
	@JoinColumn(name = "RECEIVE_CODE", insertable = false, updatable = false)
	private AddressListEntity addressList = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getFaxId() {
		return faxId;
	}

	public void setFaxId(String faxId) {
		this.faxId = faxId;
	}

	public String getReceiveCode() {
		return receiveCode;
	}

	public void setReceiveCode(String receiveCode) {
		this.receiveCode = receiveCode;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public PersonEntity getPerson() {
		return person;
	}

	public void setPerson(PersonEntity person) {
		this.person = person;
	}

	public OrgEntity getOrg() {
		return org;
	}

	public void setOrg(OrgEntity org) {
		this.org = org;
	}

	public AddressListEntity getAddressList() {
		return addressList;
	}

	public void setAddressList(AddressListEntity addressList) {
		this.addressList = addressList;
	}

}
