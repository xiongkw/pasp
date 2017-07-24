/**
 * 
 */
package com.github.pasp.core;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 通用基础Dao接口
 * </p>
 * 
 * @author xiongkw
 *
 * @param <T>
 * @param <ID>
 */
public interface IBaseDao<T extends Entity<ID>, ID extends Serializable> extends IEntityAware {

	/**
	 * 新增记录
	 * <p>
	 * 插入记录到数据库，返回主键值，目前只支持自增的Long和Integer类型
	 * </p>
	 * 
	 * @param entity
	 * @return 主键
	 */
	ID insert(T entity);

	/**
	 * 根据主键查询记录
	 * <p>
	 * 根据主键查询对象实体。主键是通过在主键字段上面的注解标识的，主键名称可以不是id字符串
	 * </p>
	 * 
	 * @param entity
	 * @return 查询到的记录，可能为null
	 */
	@UseCache(key = "#entity.getId()")
	T selectByPrimaryKey(T entity);

	/**
	 * 根据主键更新
	 * <p>
	 * 根据主键更新并返回所影响的记录行数，若传入字段值为null，则数据库字段会被置为null(即使int类型有缺省值)
	 * </p>
	 * 
	 * @param entity
	 * @return 影响的记录行数
	 */
	@FlushCache(key = "#entity.getId()")
	int updateByPrimaryKey(T entity);

	/**
	 * 根据主键删除记录
	 * <p>
	 * 根据主键删除并返回所影响的记录行数
	 * </p>
	 * 
	 * @param entity
	 * @return 影响的记录行数
	 */
	@FlushCache(key = "#entity.getId()")
	int deleteByPrimaryKey(T entity);

	/**
	 * 根据主键选择性更新
	 * <p>
	 * 根据主键更新有值字段的记录，即只更新entity有值的字段，并返回所影响的记录行数。 <br>
	 * 若传入字段值为null，则数据库字段不会被更新为null
	 * </p>
	 * 
	 * @param entity
	 * @return 影响的记录行数
	 */
	@FlushCache(key = "#entity.getId()")
	int updateSelectiveByPrimaryKey(T entity);

	/**
	 * 根据条件查询
	 * <p>
	 * 根据条件查询记录，并返回记录列表，查询条件是entity设置有值的字段
	 * </p>
	 * 
	 * @param entity
	 * @return 记录列表，可能为null
	 */
	@UseCache
	List<T> selectByExample(T entity);

	/**
	 * 根据条件查询记录行数
	 * 
	 * <p>
	 * 查询记录总数。 entity有值的字段就是查询条件
	 * </p>
	 * 
	 * @param entity
	 * @return 返回记录行数
	 */
	int count(Entity<? extends ID> entity);

	/**
	 * 查询符合条件的第一条记录
	 * <p>
	 * 根据条件查询第一条记录，查询条件是entity设置有值的字段
	 * </p>
	 * 
	 * @param entity
	 * @return 第一条符合条件的记录
	 */
	@UseCache
	T selectFirstByExample(T entity);

	/**
	 * 批量插入记录
	 * <p>
	 * 批量插入记录，比循环执行insert高效<br>
	 * 每个entity有值的字段和有值字段数可不相同，因为是根据entity生成全量字段插入<br>
	 * 若某个entity的某个字段值是null，则该记录的该字段会被插入null
	 * </p>
	 * 
	 * @param entitys
	 * @return 影响的记录行数数组
	 */
	int[] batchInsert(List<T> entitys);

	/**
	 * 根据主键批量更新记录
	 * <p>
	 * 根据主键批量更新记录，每个entity有值的字段和有值的字段数可不相同，因为是根据entity生成全量字段更新sqlByPrimaryKey
	 * <br>
	 * 若某个entity的某个字段值是null，则该记录的该字段会被更新为null，并返回所影响行数数组，若全部更新成功，则返回[1,1,1…]数组，
	 * <br>
	 * 如批量更新3条记录都成功，第1、3条成功，第2条失败，则返回[1,0,1]。若传入字段值为null，则数据库字段会被置为null( *
	 * 即使int类型有缺省值)
	 * </p>
	 * 
	 * @param values
	 * @return 影响的记录行数数组
	 */
	@FlushCache()
	int[] batchUpdateByPrimaryKey(List<T> values);

	/**
	 * 根据主键批量删除记录
	 * <p>
	 * 根据主键批量删除记录，返回删除行数
	 * </p>
	 * 
	 * @param entitys
	 * @return 影响的记录行数数组
	 */
	@FlushCache()
	int[] batchDeleteByPrimary(List<T> entitys);

	/**
	 * 根据主键查询
	 * <p>
	 * 根据主键查询记录，参数即为主键
	 * </p>
	 * 
	 * @param id
	 *            主键
	 * @return T 查询到的记录，可能为null
	 */
	@UseCache(key = "#id")
	T selectByPrimaryKey(ID id);

	/**
	 * 通过主键删除记录
	 * <p>
	 * 根据主键删除记录，参数即为主键
	 * </p>
	 * 
	 * @param id
	 *            主键
	 * @return 删除的记录行数
	 */
	@FlushCache(key = "#id")
	int deleteByPrimaryKey(ID id);

	/**
	 * 分页条件查询
	 * <p>
	 * 按条件查询分页记录，返回分页包装对象
	 * </p>
	 * 
	 * @param entity
	 *            条件
	 * @param pageIndex
	 *            起始页码
	 * @param pageSize
	 *            每页行数
	 * @return pageInfo 分页对象
	 */
	@UseCache
	Page<T> jdbcFindPageInfo(T entity, int pageIndex, int pageSize);

	/**
	 * 查询指定查询sql记录的行数
	 * 
	 * @param sql e.g. select id,name from user where name like 'xx';
	 * @param args
	 * @return 记录行数
	 */
	int count(final String sql, final Object[] args);
	
	/**
	 * 根据sqlId查询单个实体
	 * <p>
	 * 根据sqlId查询单个实体，为ftl语法的sql拼装方式
	 * </p>
	 * 
	 * @param sqlId
	 *            sql id
	 * @param model
	 *            用于sql语句模板转换的对象，用于ftl语法的sql拼装方式
	 * @return 查询到的记录，可能为null
	 */
	T getBySqlId(String sqlId, Object model);

	/**
	 * 根据sqlId查询单个实体
	 * <p>
	 * 根据sqlId查询单个实体，为占位符(?)的查询方式
	 * </p>
	 * 
	 * @param sqlId
	 *            sql id
	 * @param args
	 *            参数，用于占位符(?)的查询方式
	 * @return 查询到的记录，可能为null
	 */
	T getBySqlId(String sqlId, Object[] args);

	/**
	 * 根据sqlId查询单个实体
	 * <p>
	 * 根据sqlId查询单个实体，用于ftl拼装和占位符的混合使用
	 * </p>
	 * 
	 * @param sqlId
	 *            sql id
	 * @param model
	 *            用于sql语句模板转换的对象，用于ftl语法的sql解析方式
	 * @param args
	 *            参数，用于占位符(?)的查询方式
	 * @return 查询到的记录，可能为null
	 */
	T getBySqlId(String sqlId, Object model, Object[] args);

	/**
	 * 根据sqlId查询多个记录
	 * <p>
	 * 根据sqlId查询多个记录，为ftl语法的sql拼装方式
	 * </p>
	 * 
	 * @param sqlId
	 *            sql id
	 * @param model
	 *            用于sql语句模板转换的对象，用于ftl语法的sql拼装方式
	 * @return 查询到的记录列表
	 */
	List<T> queryBySqlId(String sqlId, Object model);

	/**
	 * 根据sqlId查询多个实体
	 * <p>
	 * 根据sqlId查询多个实体，为占位符(?)的查询方式
	 * </p>
	 * 
	 * @param sqlId
	 *            sql id
	 * @param args
	 *            参数，用于占位符(?)的查询方式
	 * @return 查询到的记录列表
	 */
	List<T> queryBySqlId(String sqlId, Object[] args);

	/**
	 * 根据sqlId查询多个实体
	 * <p>
	 * 根据sqlId查询多个实体，用于ftl拼装和占位符的混合使用
	 * </p>
	 * 
	 * @param sqlId
	 *            sql id
	 * @param model
	 *            用于sql语句模板转换的对象，用于ftl语法的sql解析方式
	 * @param args
	 *            参数，用于占位符(?)的查询方式
	 * @return 查询到的记录列表
	 */
	List<T> queryBySqlId(String sqlId, Object model, Object[] args);

	/**
	 * 根据sqlId查询分页记录
	 * <p>
	 * 根据sqlId查询分页记录，为ftl语法的sql拼装方式
	 * </p>
	 * 
	 * @param sqlId
	 *            sql id
	 * @param pageIndex
	 *            页码，从1开始
	 * @param pageSize
	 *            每页记录数
	 * @param model
	 *            用于sql语句模板转换的对象，用于ftl语法的sql拼装方式
	 * @return 查询到的分页记录
	 */
	Page<T> pageBySqlId(String sqlId, int pageIndex, int pageSize, Object model);

	/**
	 * 根据sqlId查询分页记录
	 * <p>
	 * 根据sqlId查询分页记录，为占位符(?)的查询方式
	 * </p>
	 * 
	 * @param sqlId
	 *            sql id
	 * @param pageIndex
	 *            页码，从1开始
	 * @param pageSize
	 *            每页记录数
	 * @param args
	 *            参数，用于占位符(?)的查询方式
	 * @return 查询到的分页记录
	 */
	Page<T> pageBySqlId(String sqlId, int pageIndex, int pageSize, Object[] args);

	/**
	 * 根据sqlId查询分页记录
	 * <p>
	 * 根据sqlId查询分页记录，用于ftl拼装和占位符的混合使用
	 * </p>
	 * 
	 * @param sqlId
	 *            sql id
	 * @param pageIndex
	 *            页码，从1开始
	 * @param pageSize
	 *            每页记录数
	 * @param model
	 *            用于sql语句模板转换的对象，用于ftl语法的sql拼装方式
	 * @param args
	 *            参数，用于占位符(?)的查询方式
	 * @return 查询到的分页记录
	 */
	Page<T> pageBySqlId(String sqlId, int pageIndex, int pageSize, Object model, Object[] args);

	/**
	 * 根据条件更新
	 * <p>
	 * 根据条件更新，返回影响到的记录行数
	 * </p>
	 * 
	 * @param entity
	 *            更新的实体
	 * @param example
	 *            条件实体
	 * @return 影响的记录行数
	 */
	@FlushCache
	int updateByExample(T entity, T example);

	/**
	 * 根据条件选择性更新
	 * <p>
	 * 根据条件选择性更新（只更新非空字段），返回影响到的行数
	 * </p>
	 * 
	 * @param entity
	 *            更新的实体
	 * @param example
	 *            条件实体
	 * @return 影响的记录行数
	 */
	int updateSelectiveByExample(T entity, T example);

	/**
	 * 根据条件删除
	 * <p>
	 * 根据条件删除，返回影响到的行数
	 * </p>
	 * 
	 * @param entity
	 * @return 影响的记录行数
	 */
	@FlushCache
	int deleteByExample(T entity);
	
	/**
	 * <p>根据sqlId查询记录总数</p>
	 * @param sqlId
	 * @param model
	 * @param args
	 * @return
	 */
	int countBySqlId(String sqlId, Object model, Object[] args);

}
