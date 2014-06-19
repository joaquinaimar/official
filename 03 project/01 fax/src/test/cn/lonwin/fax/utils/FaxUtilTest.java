package cn.lonwin.fax.utils;

import java.io.File;
import java.util.List;

import junit.framework.Assert;

import org.junit.Test;

public class FaxUtilTest {

	@Test
	public void testGetTemplate() {

		String[] target = { "code", "name", "phoneNo", "email", "orgCode" };

		List<String> template = FaxUtil.getTemplate();

		Assert.assertEquals(target.length, template.size());

		for (int i = 0; i < target.length; i++) {
			Assert.assertEquals(target[i], template.get(i));
		}

	}

	@Test
	public void testConvertOfficeToPdf() {

		FaxUtil.convertOfficeToPdf(new File(
				"D:\\工作文档\\张立智——龙网公司终端普查表格式1.xls"), new File(
				"D:\\工作文档\\张立智——软件技术中心3-4月份工作总结.pdf"));

	}

}
