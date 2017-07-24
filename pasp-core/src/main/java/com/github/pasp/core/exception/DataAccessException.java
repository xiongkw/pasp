package com.github.pasp.core.exception;

/**
 * <p>
 * 数据处理异常
 * </p>
 * 
 * @author xiongkw
 *
 */
public class DataAccessException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DataAccessException(String msg) {
		super(msg);
	}

	public DataAccessException(String msg, Throwable t) {
		super(msg, t);
	}

}
