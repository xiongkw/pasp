package com.github.pasp.context.service;

import com.github.pasp.context.mapper.SimpleBeanMapper;
import com.github.pasp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ResolvableType;

import javax.annotation.PostConstruct;
import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 抽象Service基类，提供基本的接口
 * </p>
 * 
 * @author xiongkw
 *
 * @param <E>
 * @param <ID>
 * @param <D>
 */
public abstract class BaseService<E extends Entity<ID>, ID extends Serializable, D extends DTO>
		implements IBaseService<E, ID, D> {

	@Autowired
	protected IBaseDao<E, ID> baseDao;

	@Autowired(required = false)
	protected IBeanMapper dtoMapper = SimpleBeanMapper.INSTANCE;

	private Class<E> entityType;

	private Class<D> dtoType;

	@PostConstruct
	@SuppressWarnings("unchecked")
	public void init() {
		ResolvableType type = ResolvableType.forClass(this.getClass()).getSuperType();
		entityType = (Class<E>) type.getGeneric(0).resolve();
		dtoType = (Class<D>) type.getGeneric(2).resolve();
	}

	@Override
	public ID insert(D dto) {
		E e = dtoMapper.map(dto, entityType);
		return baseDao.insert(e);
	}

	@Override
	public D selectByPrimaryKey(ID id) {
		E e = baseDao.selectByPrimaryKey(id);
		return dtoMapper.map(e, dtoType);
	}

	@Override
	public D selectByPrimaryKey(D dto) {
		E e = dtoMapper.map(dto, entityType);
		E result = baseDao.selectByPrimaryKey(e);
		return dtoMapper.map(result, dtoType);
	}

	@Override
	public int updateByPrimaryKey(D dto) {
		E e = dtoMapper.map(dto, entityType);
		return baseDao.updateByPrimaryKey(e);
	}

	@Override
	public int updateSelectiveByPrimaryKey(D dto) {
		E e = dtoMapper.map(dto, entityType);
		return baseDao.updateSelectiveByPrimaryKey(e);
	}

	@Override
	public int deleteByPrimaryKey(D dto) {
		E e = dtoMapper.map(dto, entityType);
		return baseDao.deleteByPrimaryKey(e);
	}

	@Override
	public int deleteByPrimaryKey(ID id) {
		return baseDao.deleteByPrimaryKey(id);
	}

	@Override
	public List<D> selectByExample(D dto) {
		E e = dtoMapper.map(dto, entityType);
		List<E> result = baseDao.selectByExample(e);
		return dtoMapper.mapAll(result, dtoType);
	}

	@Override
	public Page<D> queryPage(D dto, int pageIndex, int pageSize) {
		E e = dtoMapper.map(dto, entityType);
		Page<E> Page = baseDao.jdbcFindPageInfo(e, pageIndex, pageSize);
		return dtoMapper.mapPageInfo(Page, dtoType);
	}

	public IBaseDao<E, ID> getDao() {
		return baseDao;
	}

	@Override
	public boolean exists(D dto) {
		E e = dtoMapper.map(dto, entityType);
		return baseDao.count(e) != 0;
	}

	@Override
	public int[] batchInsert(List<D> list) {
		List<E> entityList = dtoMapper.mapAll(list, entityType);
		return baseDao.batchInsert(entityList);
	}

	@Override
	public int[] batchUpdateByPrimaryKey(List<D> list) {
		List<E> entityList = dtoMapper.mapAll(list, entityType);
		return baseDao.batchUpdateByPrimaryKey(entityList);
	}

	@Override
	public int[] batchDeleteByPrimaryKey(List<D> list) {
		List<E> entityList = dtoMapper.mapAll(list, entityType);
		return baseDao.batchDeleteByPrimary(entityList);
	}

}
