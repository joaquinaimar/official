package cn.lonwin.fax.domain.entity.id;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.wizard.official.platform.spring.hibernate.database.EntityTemplate;

@Embeddable
public class EastFaxStatId extends EntityTemplate {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -4951206442807676206L;

	@Column(name = "ORD_NO")
	private String ordNo = null;

	@Column(name = "RECV_INDEX")
	private Integer recvIndex = null;

	public String getOrdNo() {
		return ordNo;
	}

	public void setOrdNo(String ordNo) {
		this.ordNo = ordNo;
	}

	public Integer getRecvIndex() {
		return recvIndex;
	}

	public void setRecvIndex(Integer recvIndex) {
		this.recvIndex = recvIndex;
	}

}
