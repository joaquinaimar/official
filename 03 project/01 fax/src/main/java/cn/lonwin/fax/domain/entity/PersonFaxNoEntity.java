package cn.lonwin.fax.domain.entity;

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
@Table(name = "T_PERSON_FAX_NO")
public class PersonFaxNoEntity extends EntityTemplate {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2677653201879972803L;

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", length = 36)
	private String id = null;

	@Column(name = "PERSON_CODE", length = 30)
	private String personCode = null;

	@Column(name = "NO", length = 30)
	private String no = null;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getPersonCode() {
		return personCode;
	}

	public void setPersonCode(String personCode) {
		this.personCode = personCode;
	}

	public String getNo() {
		return no;
	}

	public void setNo(String no) {
		this.no = no;
	}

}
