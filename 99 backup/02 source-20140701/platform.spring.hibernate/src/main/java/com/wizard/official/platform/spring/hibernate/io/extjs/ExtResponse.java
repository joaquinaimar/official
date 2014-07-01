package com.wizard.official.platform.spring.hibernate.io.extjs;

public class ExtResponse<T> {

	private boolean success = false;

	private String message = null;

	private T data = null;

	public ExtResponse(boolean success) {
		this.success = success;
	}

	public ExtResponse(boolean success, T data) {
		this.success = success;
		this.data = data;
	}

	public ExtResponse(boolean success, String message) {
		this.success = success;
		this.message = message;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

}
