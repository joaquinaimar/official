package cn.lonwin.fax.domain.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import cn.lonwin.fax.domain.entity.id.EastFaxStatId;

import com.wizard.official.platform.spring.hibernate.database.EntityTemplate;

@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "FAXSTAT")
public class EastFaxStat extends EntityTemplate {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 3072043691016110983L;

	@Id
	private EastFaxStatId id = null;

	@Column(name = "SEND_ACCOUNT", length = 255)
	private String sendAccount = null;

	@Column(name = "SEND_BEGIN", length = 30)
	private String sendBegin = null;

	@Column(name = "RETRY_TIME")
	private Integer retryTime = null;

	@Column(name = "TOTAL_PAGE")
	private Integer totalPage = null;

	@Column(name = "STATUS")
	private Integer status = null;

	@Column(name = "RECV_NUMBER")
	private String recvNumber = null;

	@Column(name = "RECV_NAME")
	private String recvName = null;

	@Column(name = "RECV_COMPANY")
	private String recvCompany = null;

	@Column(name = "SEND_CNT")
	private Integer sendCnt = null;

	@Column(name = "SENDED_PAGE")
	private Integer sendedPage = null;

	public String getSendAccount() {
		return sendAccount;
	}

	public void setSendAccount(String sendAccount) {
		this.sendAccount = sendAccount;
	}

	public String getSendBegin() {
		return sendBegin;
	}

	public void setSendBegin(String sendBegin) {
		this.sendBegin = sendBegin;
	}

	public Integer getRetryTime() {
		return retryTime;
	}

	public void setRetryTime(Integer retryTime) {
		this.retryTime = retryTime;
	}

	public Integer getTotalPage() {
		return totalPage;
	}

	public void setTotalPage(Integer totalPage) {
		this.totalPage = totalPage;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getOrdNo() {
		if (null != id)
			return id.getOrdNo();
		else
			return null;
	}

	public void setOrdNo(String ordNo) {
		if (null != id)
			id.setOrdNo(ordNo);
	}

	public Integer getRecvIndex() {
		if (null != id)
			return id.getRecvIndex();
		else
			return null;
	}

	public void setRecvIndex(Integer recvIndex) {
		if (null != id)
			id.setRecvIndex(recvIndex);
	}

	public String getRecvNumber() {
		return recvNumber;
	}

	public void setRecvNumber(String recvNumber) {
		this.recvNumber = recvNumber;
	}

	public String getRecvName() {
		return recvName;
	}

	public void setRecvName(String recvName) {
		this.recvName = recvName;
	}

	public String getRecvCompany() {
		return recvCompany;
	}

	public void setRecvCompany(String recvCompany) {
		this.recvCompany = recvCompany;
	}

	public Integer getSendCnt() {
		return sendCnt;
	}

	public void setSendCnt(Integer sendCnt) {
		this.sendCnt = sendCnt;
	}

	public Integer getSendedPage() {
		return sendedPage;
	}

	public void setSendedPage(Integer sendedPage) {
		this.sendedPage = sendedPage;
	}

	public String getMessage() {
		String message = "";
		switch (status) {
		case 0:
			message = "管理员删除或者尚未发送";
			break;
		case 1:
			message = "发送成功";
			break;
		case 3:
			message = "服务端取消";
			break;
		case 4:
			message = "客户端取消";
			break;
		case 11:
			message = "传真文件错误";
			break;
		case 12:
			message = "无人接听或空号";
			break;
		case 13:
			message = "用户帐号错误";
			break;
		case 14:
			message = "用户没有权限";
			break;
		case 21:
			message = "Modem不支持传真操作";
			break;
		case 22:
			message = "初始化 Modem错误";
			break;
		case 23:
			message = "没有拨号音";
			break;
		case 24:
			message = "拨号错误";
			break;
		case 25:
			message = "没有回铃音";
			break;
		case 26:
			message = "长时间静音";
			break;
		case 27:
			message = "协商传真通讯参数错误";
			break;
		case 28:
			message = "错误的传真标识";
			break;
		case 29:
			message = "对方占线";
			break;
		case 31:
			message = "用户取消";
			break;
		case 32:
			message = "超时错误";
			break;
		case 99:
			message = "未知错误";
			break;
		case 100:
			message = "外部打印机转换失败";
			break;
		case 101:
			message = "黑名单禁止发送";
			break;
		}
		return message;
	}

	public String getPage() {
		return sendedPage + "/" + totalPage;
	}

}
