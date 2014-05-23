package com.wizard.official.platform.spring.hibernate.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class PlatformUtil {

	@SuppressWarnings("unchecked")
	public static List<FileItem> getFileItem(final HttpServletRequest request,
			final long sizeMax) {
		ServletFileUpload fileupload = new ServletFileUpload(
				new DiskFileItemFactory());
		fileupload.setSizeMax(sizeMax <= 0 ? -1 : sizeMax);
		try {
			return fileupload.parseRequest(request);
		} catch (FileUploadException e) {
			return null;
		}
	}

	public static List<FileItem> getFileItem(final HttpServletRequest request) {
		return getFileItem(request, 0);
	}

	public static OutputStream getExcelOutputStream(HttpServletRequest request,
			HttpServletResponse response, String fileName) throws IOException {

		fileName = getFileName(request, fileName);

		response.reset();
		response.setContentType("application/vnd.ms-excel;utf-8");
		response.setHeader("Content-Disposition", "attachment; filename="
				+ fileName);

		return response.getOutputStream();

	}

	public static String getFileName(HttpServletRequest request, String fileName)
			throws UnsupportedEncodingException {
		String userAgent = request.getHeader("User-Agent").toLowerCase();

		if (userAgent.indexOf("firefox") > 0) {
			fileName = new String(fileName.getBytes("UTF-8"), "ISO8859-1");
		} else {
			fileName = URLEncoder.encode(fileName, "UTF-8");
		}
		return fileName;
	}

}
