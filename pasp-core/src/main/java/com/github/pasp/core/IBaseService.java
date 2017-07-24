package com.github.pasp.core;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 通用基础服务接口
 * </p>
 * 
 * @author xiongkw
 *
 * @param <E>
 * @param <ID>
 * @param <D>
 */
public interface IBaseService<E extends Entity<ID>, ID extends Serializable, D extends DTO> {

	/**
	 * <p>
	 * 保存实体并返回主键id
	 * </p>
	 * 
	 * @param dto
	 * @return
	 */
	ID insert(D dto);

	/**
	 * <p>
	 * 根据主键查询，返回单个实体
	 * </p>
	 * 
	 * @param id
	 * @return
	 */
	D selectByPrimaryKey(ID id);

	/**
	 * <p>
	 * 根据主键查询，返回单个实体
	 * </p>
	 * 
	 * @param dto
	 * @return
	 */
	D selectByPrimaryKey(D dto);

	/**
	 * <p>
	 * 根据主键更新实体，更新所有属性(无论非空与否)
	 * </p>
	 * 
	 * @param dto
	 * @return
	 */
	int updateByPrimaryKey(D dto);

	/**
	 * <p>
	 * 根据主键更新实体，只更新非空属性
	 * </p>
	 * 
	 * @param dto
	 * @return
	 */
	int updateSelectiveByPrimaryKey(D dto);

	/**
	 * <p>
	 * 根据主键删除实体
	 * </p>
	 * 
	 * @param dto
	 * @return
	 */
	int deleteByPrimaryKey(D dto);

	/**
	 * <p>
	 * 根据主键删除实体
	 * </p>
	 * 
	 * @param id
	 * @return
	 */
	int deleteByPrimaryKey(ID id);

	/**
	 * <p>
	 * 根据实体属性查询
	 * </p>
	 * 
	 * @param dto
	 * @return
	 */
	List<D> selectByExample(D dto);

	/**
	 * <p>
	 * 根据实体属性查询分页
	 * </p>
	 * 
	 * @param dto
	 * @param pageIndex
	 * @param pageSize
	 * @return
	 */
	Page<D> queryPage(D dto, int pageIndex, int pageSize);

	/**
	 * <p>
	 * 实体是否存在
	 * </p>
	 * 
	 * @param dto
	 * @return
	 */
	boolean exists(D dto);

	/**
	 * <p>
	 * 批量新增
	 * </p>
	 * 
	 * @param list
	 * @return
	 */
	int[] batchInsert(List<D> list);

	/**
	 * <p>
	 * 批量更新
	 * </p>
	 * 
	 * @param list
	 * @return
	 */
	int[] batchUpdateByPrimaryKey(List<D> list);

	/**
	 * <p>
	 * 批量删除
	 * </p>
	 * 
	 * @param list
	 * @return
	 */
	int[] batchDeleteByPrimaryKey(List<D> list);
}
