package com.wizard.official.platform.spring.hibernate.io;

public class PageRequest {

	public static final int DEFAULT_START = 0;
	public static final int DEFAULT_LIMIT = 20;

	protected int start = 0;
	protected int limit = 0;

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		if (start < 0) {
			this.start = DEFAULT_START;
		} else {
			this.start = start;
		}
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		if (limit <= 0) {
			this.limit = DEFAULT_LIMIT;
		} else {
			this.limit = limit;
		}
	}

	public int getEnd() {
		return start + limit;
	}
}
