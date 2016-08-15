package com.byc.tools.patch.exceptions;

public class PublishException extends PatchException {

	static final long serialVersionUID = 888837582814328888L;

	public PublishException() {
		super();
	}

	public PublishException(Throwable cause) {
		super(cause);
	}

	public PublishException(String message) {
		super(message);
	}

	public PublishException(String message, Throwable cause) {
		super(message, cause);
	}

}
