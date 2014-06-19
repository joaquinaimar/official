package cn.lonwin.fax.utils;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Properties;

import cn.lonwin.fax.domain.fax.EastFaxConfig;
import cn.lonwin.fax.domain.fax.EastFaxReceiver;

import com.wizard.official.platform.spring.hibernate.utils.PlatformUtil;

public class EastFaxUtil {

	private final static String GENERAL = "[General]";

	private final static String ACCOUNT = "Account";

	private final static String SUBJECT = "Subject";

	private final static String COMMENTS = "Comments";

	private final static String FAX_FLOW_ACCOUNTS = "FaxFlowAccounts";

	private final static String SERIAL_NO = "SerialNo";

	private final static String PRIORITY = "Priority";

	private final static String EMAIL_RESULT = "EmailResult";

	private final static String RECEIVER_NUMBER = "ReceiverNumber_";

	private final static String RECEIVER_NAME = "ReceiverName_";

	private final static String RECEIVER_COMPANY = "ReceiverCompany_";

	private final static String SEND_LINE = "SendLine";

	private final static String SEND_TIME = "SendTime";

	private final static String EASTFAX = "EastFax";

	public static String getFaxPath() {
		Properties prop = new Properties();
		try {
			prop.load(EastFaxUtil.class.getClassLoader().getResourceAsStream(
					"fax.properties"));
			String path = prop.getProperty("faxPath");
			File p = new File(path);
			if (!p.exists())
				p.mkdirs();
			return path;
		} catch (IOException e) {
			return null;
		}
	}

	public static void creatSendConfigFile(EastFaxConfig config, String name) {

		try {

			int i = 1;

			if (null != config.getSendFiles())
				for (File file : config.getSendFiles()) {
					String[] fileNames = file.getName().split("\\.");
					File faxFile = new File(getFaxPath() + name + "_" + i + "."
							+ fileNames[1]);
					if (faxFile.exists())
						faxFile.delete();

					PlatformUtil.copyFile(file, faxFile);
					i++;
				}

			File configFile = new File(getFaxPath() + "\\" + name + ".epi");

			if (configFile.exists())
				configFile.delete();

			configFile.createNewFile();

			PrintWriter out = new PrintWriter(configFile);

			out.println(GENERAL);
			out.println(createKV(ACCOUNT, config.getAccount()));
			out.println(createKV(SUBJECT, config.getSubject()));
			out.println(createKV(COMMENTS, config.getComments()));

			StringBuffer ffa = new StringBuffer(0);
			if (null != config.getFaxFlowAccounts())
				for (String s : config.getFaxFlowAccounts()) {
					ffa.append(">").append(s);
				}
			if (0 < ffa.length())
				out.println(createKV(FAX_FLOW_ACCOUNTS, ffa.toString()
						.substring(1)));
			else
				out.println(createKV(FAX_FLOW_ACCOUNTS, null));

			out.println(createKV(SERIAL_NO, config.getSerialNo()));
			out.println(createKV(PRIORITY, config.getPriority()));
			out.println(createKV(EMAIL_RESULT, config.getEmailResult()));

			i = 1;
			if (null != config.getReceiverList())
				for (EastFaxReceiver receiver : config.getReceiverList()) {
					out.println(createKV(RECEIVER_NUMBER + i,
							receiver.getNumber()));
					out.println(createKV(RECEIVER_NAME + i, receiver.getName()));
					out.println(createKV(RECEIVER_COMPANY + i,
							receiver.getCompany()));
					i++;
				}

			out.println(createKV(SEND_LINE, config.getSendLine()));
			if (null != config.getSendTime())
				out.println(createKV(SEND_TIME, new SimpleDateFormat(
						"yyyyMMddhhmmss").format(config.getSendTime())));
			else
				out.println(createKV(SEND_TIME, null));

			out.println(createKV(EASTFAX, !config.getEastFax() ? "No" : "Yes"));

			out.flush();
			out.close();

		} catch (IOException e) {
		}
	}

	public static void creatSendConfigFile(EastFaxConfig config) {
		creatSendConfigFile(config, config.getSerialNo());
	}

	private static String createKV(String key, Object value) {
		StringBuffer sb = new StringBuffer();
		sb.append(key).append("=");
		if (null != value)
			sb.append(value);
		return sb.toString();
	}

}
