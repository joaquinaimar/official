package com.wizard.official.platform.spring.hibernate.io.extjs;

import java.util.List;

import com.wizard.official.platform.spring.hibernate.io.PageResponse;

public class ExtPageResponse<T> {

	private boolean success = false;

	private String message = null;

	private List<T> result = null;

	private long totalCount = 0;

	private int start = 0;

	private int limit = 0;

	public ExtPageResponse(boolean success) {
		this.success = success;
	}

	public ExtPageResponse(boolean success, PageResponse<T> data) {
		this(success);
		this.result = data.getResult();
		this.totalCount = data.getTotalCount();
		this.start = data.getStart();
		this.limit = data.getLimit();
	}

	public ExtPageResponse(boolean success, String message) {
		this(success);
		this.message = message;
	}

	public boolean getSuccess() {
		return success;
	}

	public String getMessage() {
		return message;
	}

	public long getTotalCount() {
		return totalCount;
	}

	public int getStart() {
		return start;
	}

	public int getLimit() {
		return limit;
	}

	public List<T> getResult() {
		return result;
	}
}
