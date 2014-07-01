package com.wizard.official.platform.spring.hibernate.io.vo;

public class ResponseVo<T> {

	private boolean success = false;

	private String message = null;

	private T data = null;

	public ResponseVo(boolean success) {
		this.success = success;
	}

	public ResponseVo(boolean success, T data) {
		this.success = success;
		this.data = data;
	}

	public ResponseVo(boolean success, String message) {
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
