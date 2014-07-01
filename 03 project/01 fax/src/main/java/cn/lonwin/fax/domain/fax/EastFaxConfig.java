package cn.lonwin.fax.domain.fax;

import java.io.File;
import java.util.Date;
import java.util.List;

public class EastFaxConfig {

	private String account = null;

	private String subject = null;

	private String comments = null;

	private String[] faxFlowAccounts = null;

	private String serialNo = null;

	private Integer priority = null;

	private String emailResult = null;

	private List<EastFaxReceiver> receiverList = null;

	private Integer sendLine = null;

	private Date sendTime = null;

	private Boolean eastFax = null;

	private List<File> sendFiles = null;

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public String[] getFaxFlowAccounts() {
		return faxFlowAccounts;
	}

	public void setFaxFlowAccounts(String[] faxFlowAccounts) {
		this.faxFlowAccounts = faxFlowAccounts;
	}

	public String getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(String serialNo) {
		this.serialNo = serialNo;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public String getEmailResult() {
		return emailResult;
	}

	public void setEmailResult(String emailResult) {
		this.emailResult = emailResult;
	}

	public List<EastFaxReceiver> getReceiverList() {
		return receiverList;
	}

	public void setReceiverList(List<EastFaxReceiver> receiverList) {
		this.receiverList = receiverList;
	}

	public Integer getSendLine() {
		return sendLine;
	}

	public void setSendLine(Integer sendLine) {
		this.sendLine = sendLine;
	}

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Boolean getEastFax() {
		return eastFax;
	}

	public void setEastFax(Boolean eastFax) {
		this.eastFax = eastFax;
	}

	public List<File> getSendFiles() {
		return sendFiles;
	}

	public void setSendFiles(List<File> sendFiles) {
		this.sendFiles = sendFiles;
	}

}
