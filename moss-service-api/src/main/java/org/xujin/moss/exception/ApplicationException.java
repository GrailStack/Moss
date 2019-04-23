package org.xujin.moss.exception;

/**
 * 自定义异常
 *
 * @author xujin
 * @version 1.0
 */
public class ApplicationException extends RuntimeException {

	private static final long serialVersionUID = -3426833209473506363L;

	public ApplicationException() {
		super();
	}

	public ApplicationException(String message, Throwable cause) {
		super(message, cause);
	}

	public ApplicationException(String message) {
		super(message);
	}

	public ApplicationException(Throwable cause) {
		super(cause);
	}

}
