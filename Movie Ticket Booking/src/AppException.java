package com.zoho.booktickets.exception;

public class AppException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	public final ErrorCode err;

	public AppException(ErrorCode error) {
		super(error.getMessage());
		err = error;
	}

	public AppException(ErrorCode error, Exception e) {
		super(error.getMessage(), e);
		err = error;
		// System.out.println(e.getCause().getMessage());
	}
}
