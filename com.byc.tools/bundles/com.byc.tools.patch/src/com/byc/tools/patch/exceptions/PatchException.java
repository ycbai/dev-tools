package com.byc.tools.patch.exceptions;

public class PatchException extends Exception {

	static final long serialVersionUID = 888837582814323355L;

	public PatchException() {
		super();
	}

	public PatchException(Throwable cause) {
		super(cause);
	}

	public PatchException(String message) {
		super(message);
	}

	public PatchException(String message, Throwable cause) {
		super(message, cause);
	}

}
