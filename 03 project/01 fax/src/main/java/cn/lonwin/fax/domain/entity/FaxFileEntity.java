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
@Table(name = "T_FAX_FILE")
public class FaxFileEntity extends EntityTemplate {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 4114950243761626044L;

	@Id
	@GenericGenerator(name = "generator", strategy = "uuid")
	@GeneratedValue(generator = "generator")
	@Column(name = "ID", length = 36)
	private String id = null;

	@Column(name = "FAX_ID", length = 36)
	private String faxId = null;

	@Column(name = "FAX_FILE_PATH", length = 255)
	private String faxFilePath = null;

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

	public String getFaxFilePath() {
		return faxFilePath;
	}

	public void setFaxFilePath(String faxFilePath) {
		this.faxFilePath = faxFilePath;
	}

}
