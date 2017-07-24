package com.github.pasp.web;

import java.io.Serializable;

/**
 * <p>
 * Response，用于响应ajax前端调用
 * </p>
 * 
 * @author xiongkw
 *
 */
public class Response implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static final String MSG_SUCCESS = "success";
	private static final String CODE_OK = "0";
	private String resultCode = CODE_OK;// 0成功，-1失败
	private String resultMsg = MSG_SUCCESS;
	private Object data;

	public static final Response SUCCESS = new Response(CODE_OK, MSG_SUCCESS);

	public Response() {

	}

	public Response(String resultCode, String resultMsg) {
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
	}

	public Response(String resultCode, String resultMsg, Object data) {
		this.resultCode = resultCode;
		this.resultMsg = resultMsg;
		this.data = data;
	}

	public Response(Object data) {
		this.data = data;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getResultCode() {
		return resultCode;
	}

	public void setResultCode(String resultCode) {
		this.resultCode = resultCode;
	}

	public String getResultMsg() {
		return resultMsg;
	}

	public void setResultMsg(String resultMsg) {
		this.resultMsg = resultMsg;
	}

}
