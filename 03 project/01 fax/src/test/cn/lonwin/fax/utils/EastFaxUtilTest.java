package cn.lonwin.fax.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

import cn.lonwin.fax.domain.fax.EastFaxConfig;
import cn.lonwin.fax.domain.fax.EastFaxReceiver;

public class EastFaxUtilTest {

	@Test
	public void testCreatSendConfigFile() {
		try {
			EastFaxConfig config = new EastFaxConfig();
			config.setAccount("admin");
			config.setSubject("传真测试");
			config.setComments("测试");
			config.setFaxFlowAccounts(new String[] { "a", "b", "c" });
			config.setSerialNo("1234567890");
			config.setPriority(-1);
			config.setEmailResult("abc@126.com");
			config.setEastFax(true);

			List<EastFaxReceiver> receiverList = new ArrayList<EastFaxReceiver>();
			EastFaxReceiver receiver1 = new EastFaxReceiver();
			receiver1.setNumber("123");
			receiver1.setName("测试人员1");
			receiver1.setCompany("水务局");
			receiverList.add(receiver1);
			EastFaxReceiver receiver2 = new EastFaxReceiver();
			receiver2.setNumber("456");
			receiver2.setName("测试人员2");
			receiver2.setCompany("防汛办");
			receiverList.add(receiver2);
			config.setReceiverList(receiverList);

			config.setSendLine(0);
			config.setSendTime(new Date());
			config.setEastFax(false);

			List<File> sendFiles = new ArrayList<File>();
			sendFiles.add(new File("D:\\python.pdf"));
			sendFiles.add(new File("D:\\test.png"));
			config.setSendFiles(sendFiles);

			EastFaxUtil.creatSendConfigFile(config, "faxtest");
		} catch (Exception e) {
			Assert.fail();
		}
	}
}
