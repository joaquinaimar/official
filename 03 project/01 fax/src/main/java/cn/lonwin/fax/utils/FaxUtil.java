package cn.lonwin.fax.utils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.artofsolving.jodconverter.DocumentConverter;
import com.artofsolving.jodconverter.openoffice.connection.OpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.connection.SocketOpenOfficeConnection;
import com.artofsolving.jodconverter.openoffice.converter.OpenOfficeDocumentConverter;

public class FaxUtil {

	public static List<String> getTemplate() {

		List<String> template = new ArrayList<String>();
		try {
			InputStream inputStream = FaxUtil.class.getClassLoader()
					.getResourceAsStream("template/person.xml");
			SAXReader saxReader = new SAXReader();

			saxReader.setEncoding("UTF-8");
			Document doc;

			doc = saxReader.read(inputStream);

			Element root = doc.getRootElement();
			Element column = null;
			@SuppressWarnings("unchecked")
			Iterator<Element> it = root.elementIterator("column");
			while (it.hasNext()) {
				column = it.next();
				Integer index = Integer.valueOf(column.element("index")
						.getTextTrim());
				String name = column.element("name").getTextTrim();
				template.add(index, name);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return template;
	}

	public static String getFilePath() {
		Properties prop = new Properties();
		try {
			prop.load(FaxUtil.class.getClassLoader().getResourceAsStream(
					"fax.properties"));
			return prop.getProperty("filePath", "");
		} catch (IOException e) {
		}
		return "";
	}

	public static void convertOfficeToPdf(File from, File to) {
		String OpenOffice_HOME = "";
		try {
			Properties prop = new Properties();
			prop.load(FaxUtil.class.getClassLoader().getResourceAsStream(
					"fax.properties"));
			OpenOffice_HOME = prop.getProperty("OpenOffice_HOME", "");

			String command = "\""
					+ OpenOffice_HOME
					+ "program\\soffice.exe\" -headless -accept=\"socket,host=127.0.0.1,port=8100;urp;\"";
			Process pro = Runtime.getRuntime().exec(command);
			OpenOfficeConnection connection = new SocketOpenOfficeConnection(
					"127.0.0.1", 8100);
			connection.connect();

			DocumentConverter converter = new OpenOfficeDocumentConverter(
					connection);
			converter.convert(from, to);

			connection.disconnect();
			pro.destroy();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
