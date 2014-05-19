package com.wizard.official.platform.spring.hibernate.io.extjs;

import com.wizard.official.platform.spring.hibernate.io.PageRequest;

public class ExtPageRequest extends PageRequest {

	public int getPageNumber() {
		return start / limit;
	}

	public int getPageSize() {
		return limit;
	}
}
