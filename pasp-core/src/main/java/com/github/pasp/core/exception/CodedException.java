package com.github.pasp.core.exception;

/**
 * <p>
 * 编码的异常
 * </p>
 *
 * @author xiongkw
 */
public class CodedException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String code;

    private Object[] args;

    public CodedException(String code, Object... args) {
        super();
        this.code = code;
        this.args = args;
    }

    public CodedException(String code, String message, Object... args) {
        super(message);
        this.code = code;
        this.args = args;
    }

    public CodedException(String code, Throwable t, Object... args) {
        super(t);
        this.code = code;
        this.args = args;
    }

    public CodedException(String code, String message, Throwable t, Object... args) {
        super(message, t);
        this.code = code;
        this.args = args;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

}
