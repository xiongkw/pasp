package com.github.pasp.data;

import java.util.List;

/**
 * <p>
 * Interface of sql dialect parser
 * </p>
 * 
 * @author xiongkw
 *
 */
public interface IDialectParser {

	/**
	 * <p>
	 * 解析count sql
	 * </p>
	 * 
	 * @param sql
	 * @return
	 */
	public String getCountSql(String sql);

	/**
	 * <p>
	 * 解析分页sql
	 * </p>
	 * 
	 * @param sql
	 * @return
	 */
	public String getPageSql(String sql);

	/**
	 * <p>
	 * 构造分页参数
	 * </p>
	 * 
	 * @param page
	 * @param pageSize
	 * @return
	 */
	public List<Object> getPageParam(int page, int pageSize);
}
