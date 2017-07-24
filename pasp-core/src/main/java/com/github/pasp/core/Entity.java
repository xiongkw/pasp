package com.github.pasp.core;

import java.io.Serializable;

/**
 * Entity基类
 * <p>
 * pasp规范下，所有entity都应继承该类
 * </p>
 * 
 * 
 * @author xiongkw
 * @param <ID>
 */
public abstract class Entity<ID extends Serializable> implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public abstract ID getId();

	public abstract void setId(ID id);

}
