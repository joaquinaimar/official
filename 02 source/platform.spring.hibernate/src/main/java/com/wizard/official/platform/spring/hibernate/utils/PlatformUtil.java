package com.wizard.official.platform.spring.hibernate.utils;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

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

}
