package com.github.pasp.core;

import java.util.List;

/**
 * bean映射器接口
 * <p>
 * 用于dto和entity之间的属性映射
 * </p>
 * 
 * @author xiongkw
 *
 */
public interface IBeanMapper {

	/**
	 * 映射源对象到指定类型，返回指定类型的实例
	 * 
	 * @param obj
	 * @param cl
	 * @return
	 */
	<T> T map(Object obj, Class<T> cl);

	/**
	 * 映射源对象List到指定类型
	 * 
	 * @param list
	 * @param cl
	 * @return
	 */
	<T> List<T> mapAll(List<?> list, Class<T> cl);

	<T, E> Page<T> mapPageInfo(Page<E> pageInfo, Class<T> cl);
}
