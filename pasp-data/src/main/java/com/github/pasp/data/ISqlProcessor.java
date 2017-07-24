package com.github.pasp.data;

/**
 * <p>
 * Interface of sql processor
 * </p>
 * 用于模板解析sql
 * 
 * @author xiongkw
 *
 */
public interface ISqlProcessor {

	/**
	 * <p>
	 * 解析sql
	 * </p>
	 * 
	 * @param sqlId
	 * @param model
	 * @return
	 */
	String process(String sqlId, Object model);

	/**
	 * <p>
	 * 注册sql模板
	 * </p>
	 * 
	 * @param sqlId
	 * @param text
	 */
	void putTemplate(String sqlId, String text);

}
