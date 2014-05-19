package com.wizard.official.platform.spring.hibernate.io;

import java.util.List;

public class PageResponse<T> {

	private List<T> result = null;

	private long totalCount = 0;

	private int start = 0;

	private int limit = 0;

	public PageResponse(int start, int limit) {
		this.start = start;
		this.limit = limit;
	}

	public PageResponse(PageRequest pageRequest) {
		this(pageRequest.getStart(), pageRequest.getLimit());
	}

	public PageResponse(List<T> result, int start, int limit, long size) {
		this(start, limit);
		if (null == result) {
			return;
		}
		this.result = result;
		this.totalCount = size;
	}

	public PageResponse(List<T> result, PageRequest pageRequest, long size) {
		this(result, pageRequest.getStart(), pageRequest.getLimit(), size);
	}

	public List<T> getResult() {
		return this.result;
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

}
