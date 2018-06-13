package org.weather.api;

public class Response<T> {

	private String status;
	private T response;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public T getResponse() {
		return response;
	}

	public void setResponse(T response) {
		this.response = response;
	}

}
