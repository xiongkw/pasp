package com.github.pasp.data.dao;

import com.github.pasp.core.*;
import com.github.pasp.core.exception.DataAccessException;
import com.github.pasp.core.utils.ListUtils;
import com.github.pasp.data.*;
import com.github.pasp.data.sql.Dialect;
import com.github.pasp.data.sql.ftl.SqlFtlProcessor;
import com.github.pasp.data.sql.parser.SimpleParser;
import com.github.pasp.data.sql.xml.SqlLoaderJaxbImpl;
import com.github.pasp.data.utils.StreamUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ResolvableType;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.io.InputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public abstract class BaseDao<T extends Entity<ID>, ID extends Serializable>
        implements IBaseDao<T, ID>, InitializingBean, BeanFactoryAware {
    private Logger logger = LoggerFactory.getLogger(BaseDao.class);

    @Resource(name = "pasp-jdbcTemplate")
    private JdbcTemplate defaultJdbcTemplate;

    private JdbcTemplate jdbcTemplate;

    @Autowired(required = false)
    private ISqlLoader sqlLoader;

    @Autowired(required = false)
    private ISqlProcessor sqlProcessor;

    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Value("${jdbc.dialect:mysql}")
    private Dialect dialect;

    private IDialectParser dialectParser;

    private String SQL_SUFFIX = ".sql.xml";

    private Map<String, String> sqlMap;

    private String className = this.getClass().getName();

    private Class<T> entityType;

    private String sqlFileName;

    @Autowired(required = false)
    @Qualifier("pasp-cacheManager")
    private ICacheManager cacheManager;

    @Value("${pasp.cache}")
    private boolean useCache;

    @Autowired(required = false)
    private IEntityInfoParser entityInfoParser;

    private EntityInfo entityInfo;

    @Autowired
    private ISqlBuilder sqlBuilder;

    private BeanPropertyRowMapper<T> entityrowMapper;

    private BeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void setSqlFileName(String sqlFileName) {
        this.sqlFileName = sqlFileName;
    }

    public void setDataSource(DataSource dataSource) {
        setJdbcTemplate(new JdbcTemplate(dataSource));
    }

    public void afterPropertiesSet() {
        initJdbcTemplate();

        initEntityInfo();

        initCacheManager();

        initSqlMapper();
    }

    private void initSqlMapper() {
        this.dialectParser = SimpleParser.getParse(dialect);
        if (sqlLoader == null) {
            sqlLoader = SqlLoaderJaxbImpl.INSTANCE;
        }
        if (sqlProcessor == null) {
            sqlProcessor = new SqlFtlProcessor();
        }

        if (StringUtils.isEmpty(sqlFileName)) {
            sqlFileName = this.getClass().getSimpleName() + SQL_SUFFIX;
        }
        loadSqlMap();
    }

    private void initCacheManager() {
        if (!this.useCache) {
            this.cacheManager = null;
            return;
        }
        if (this.cacheManager == null) {
            this.cacheManager = beanFactory.getBean(ICacheManager.class);
            logger.debug("No bean named 'pasp-cacheManager' found, Use default: {}", this.cacheManager.getClass());
        }
    }

    @SuppressWarnings("unchecked")
    private void initEntityInfo() {
        ResolvableType type = ResolvableType.forClass(this.getClass());
        entityType = (Class<T>) type.getSuperType().getGeneric(0).resolve();
        entityrowMapper = BeanPropertyRowMapper.newInstance(entityType);

        if (entityInfoParser == null) {
            entityInfoParser = new EntityInfoParser();
        }
        entityInfo = entityInfoParser.parse(entityType);
    }

    private void initJdbcTemplate() {
        if (jdbcTemplate == null) {
            setJdbcTemplate(defaultJdbcTemplate);
        }
    }

    @Override
    public Class<?> getEntityType() {
        return this.entityType;
    }

    @Override
    public String getEntityName() {
        if (this.entityType != null) {
            return this.entityType.getName();
        }
        return null;
    }

    private void loadSqlMap() {
        Class<?> cl = this.getClass();
        InputStream in = cl.getResourceAsStream(sqlFileName);
        if (in == null) {
            logger.info("Sql mapper {} is not exists, ignored!", sqlFileName);
            return;
        }
        try {
            sqlMap = sqlLoader.load(in);
            if (sqlMap == null) {
                return;
            }
            Iterator<Entry<String, String>> iterator = sqlMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<String, String> next = iterator.next();
                sqlProcessor.putTemplate(getFullSqlId(next.getKey()), next.getValue());
            }
        } finally {
            StreamUtils.closeQuietly(in);
        }
    }

    private String getFullSqlId(String sqlId) {
        return className + "." + sqlId;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ID insert(T entity) {
        final Sql sql = sqlBuilder.buildInsertSql(entity, entityInfo);
        logger.debug("Insert entity sql: {}", sql);

        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(new PreparedStatementCreator() {
            public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
                PreparedStatement ps = connection.prepareStatement(sql.getStatement(),
                        new String[]{entityInfo.getPrimaryKey().getColumnName()});
                Object[] args = sql.getArgs();
                for (int i = 0; i < args.length; i++) {
                    ps.setObject(i + 1, args[i]);
                }
                return ps;
            }
        }, keyHolder);

        Number key = keyHolder.getKey();
        logger.debug("Generated id: {} for entity: {}", key, entityType.getName());
        ID id = (ID) key;
        entity.setId(id);
        return id;
    }

    @Override
    public T selectByPrimaryKey(T entity) {
        try {
            Sql sql = sqlBuilder.buildSelectByPrimaryKeySql(entity, entityInfo);
            logger.debug("Select by primary key sql: {}", sql);
            return this.jdbcTemplate.queryForObject(sql.getStatement(), sql.getArgs(), entityrowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int updateByPrimaryKey(T entity) {
        Sql sql = sqlBuilder.buildUpdateByPrimaryKeySql(entity, entityInfo);
        logger.debug("Update by primary key sql: {}", sql);
        return jdbcTemplate.update(sql.getStatement(), sql.getArgs());
    }

    @Override
    public int deleteByPrimaryKey(T entity) {
        Sql sql = sqlBuilder.buildDeleteByPrimaryKeySql(entity, entityInfo);
        logger.debug("Delete by primary key sql: {}", sql);
        return jdbcTemplate.update(sql.getStatement(), sql.getArgs());
    }

    @Override
    public int updateSelectiveByPrimaryKey(T entity) {
        Sql sql = sqlBuilder.buildUpdateSelectiveByPrimaryKeySql(entity, entityInfo);
        logger.debug("Update selective by primary key sql: {}", sql);
        if (sql.getStatement().matches("SET\\s+WHERE")) {
            logger.info("No update column found in sql: {}, update ignored!", sql.getStatement());
        }
        return jdbcTemplate.update(sql.getStatement(), sql.getArgs());
    }

    @Override
    public List<T> selectByExample(T entity) {
        Sql sql = sqlBuilder.buildSelectByExampleSql(entity, entityInfo);
        logger.debug("Select by example sql: {}", sql);
        try {
            return (List<T>) jdbcTemplate.query(sql.getStatement(), sql.getArgs(), entityrowMapper);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public int count(Entity<? extends ID> entity) {
        Sql sql = sqlBuilder.buildSelectByExampleSql(entity, entityInfo);
        logger.debug("Count by example sql: {}", sql);
        return count(sql.getStatement(), sql.getArgs());
    }

    @Override
    public int count(final String sql, final Object[] args) {
        String totalCountSql = dialectParser.getCountSql(sql);
        return jdbcTemplate.queryForObject(totalCountSql, args, Integer.class);
    }

    @Override
    public T selectFirstByExample(T entity) {
        List<T> list = this.selectByExample(entity);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    private List<T> jdbcFindList(final String sql, final Class<T> elementType, final List<Object> params) {
        Object[] array = null;
        if (params != null) {
            array = new Object[params.size()];
            params.toArray(array);
        }
        List<T> list = this.executeJdbc(sql, elementType, array);
        return list;
    }

    @Override
    public int[] batchInsert(List<T> entitys) {
        final Sql sql = sqlBuilder.buildBatchInsertSql(entitys, entityInfo);
        logger.debug("Batch insert sql: {}", sql);
        return jdbcTemplate.batchUpdate(sql.getStatement(), sql.getListArgs());
    }

    @Override
    public int[] batchUpdateByPrimaryKey(List<T> entitys) {
        final Sql sql = sqlBuilder.buildBatchUpdateByPrimaryKeySql(entitys, entityInfo);
        logger.debug("Batch update by primary key sql: {}", sql);
        return jdbcTemplate.batchUpdate(sql.getStatement(), sql.getListArgs());
    }

    @Override
    public int[] batchDeleteByPrimary(List<T> entitys) {
        final Sql sql = sqlBuilder.buildBatchDeleteByPrimaryKeySql(entitys, entityInfo);
        logger.debug("Batch delete by primary key sql: {}", sql);
        return jdbcTemplate.batchUpdate(sql.getStatement(), sql.getListArgs());
    }

    private List<Object> arrayToList(Object[] args) {
        List<Object> list = new ArrayList<Object>();
        if (args == null) {
            return list;
        }
        for (Object obj : args) {
            list.add(obj);
        }
        return list;
    }

    private Page<T> jdbcFindPage(String sql, Class<T> elementType, Object[] args, int pageIndex, int pageSize) {
        pageSize = pageSize == 0 ? Page.DEFAULT_PAGE_SIZE : pageSize;
        pageIndex = pageIndex == 0 ? 1 : pageIndex;
        int offset = (pageIndex - 1) * pageSize;
        // 构建分页查询sql
        String pageSql = dialectParser.getPageSql(sql);
        List<Object> pageParam = dialectParser.getPageParam(pageIndex, pageSize);
        List<Object> argList = arrayToList(args);
        argList.addAll(pageParam);
        List<T> list = this.jdbcFindList(pageSql, elementType, argList);

        // 查询总量
        final int totalCounts = this.count(sql, args);
        final Page<T> pageInfo = new Page<T>(list, pageIndex, pageSize, totalCounts);
        return pageInfo;
    }

    /**
     * 执行JDBC查询语句，并获得数据列表。
     *
     * @param sql         SQL语句
     * @param elementType 实体Class
     * @param params      参数列表
     * @return 数据列表
     */
    private List<T> executeJdbc(final String sql, final Class<T> elementType, final Object[] params) {
        final List<T> data = new ArrayList<T>();
        final RowMapper<T> rowMapper;
        if (elementType.isAssignableFrom(Long.class) || elementType.isAssignableFrom(String.class)
                || elementType.isAssignableFrom(Integer.class) || elementType.isAssignableFrom(Float.class)
                || elementType.isAssignableFrom(Boolean.class) || elementType.isAssignableFrom(Double.class)) {
            rowMapper = new SingleColumnRowMapper<T>(elementType);
        } else {
            rowMapper = this.entityrowMapper;
        }

        jdbcTemplate.query(sql, params, new ResultSetExtractor<Object>() {
            @Override
            public Object extractData(final ResultSet rs) throws SQLException {
                final List<T> pageItems = data;
                int currentRow = 0;
                while (rs.next()) {
                    T e = rowMapper.mapRow(rs, currentRow);
                    if (e == null) {
                        continue;
                    }
                    pageItems.add(e);
                    currentRow++;
                }
                return data;
            }
        });
        return data;
    }

    public void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        namedParameterJdbcTemplate = new NamedParameterJdbcTemplate(jdbcTemplate);
    }

    public JdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public NamedParameterJdbcTemplate getNamedParameterJdbcTemplate() {
        return namedParameterJdbcTemplate;
    }

    @Override
    public T selectByPrimaryKey(ID id) {
        try {
            T t = entityType.newInstance();
            t.setId(id);
            return this.selectByPrimaryKey(t);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DataAccessException(MessageFormat.format(IErrorConstants.ENTITY_NO_CONSTRUCTOR_DEF, entityType));
        }
    }

    @Override
    public int deleteByPrimaryKey(ID id) {
        try {
            T t = entityType.newInstance();
            t.setId(id);
            return this.deleteByPrimaryKey(t);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new DataAccessException(MessageFormat.format(IErrorConstants.ENTITY_NO_CONSTRUCTOR_DEF, entityType));
        }
    }

    @Override
    public Page<T> jdbcFindPageInfo(T entity, int pageIndex, int pageSize) {
        Sql sql = sqlBuilder.buildSelectByExampleSql(entity, entityInfo);
        return (Page<T>) this.jdbcFindPage(sql.getStatement(), entityType, sql.getArgs(), pageIndex, pageSize);
    }

    public T getBySqlId(String sqlId, Object model, Object[] args) {
        return this.getBySqlId(sqlId, entityType, model, args);
    }

    protected <E> E getBySqlId(String sqlId, Class<E> elementType, Object model, Object[] args) {
        String sql = getSqlStatment(sqlId, model);
        if (ClassUtils.isPrimitiveOrWrapper(elementType)) {
            return jdbcTemplate.queryForObject(sql, args, elementType);
        }
        return jdbcTemplate.queryForObject(sql, new BeanPropertyRowMapper<E>(elementType), args);
    }

    /**
     * <p>
     * 指定sqlId查询单个实体
     * </p>
     *
     * @param sqlId
     * @param rowMapper
     * @param model
     * @param args
     * @return
     */
    public T getBySqlId(String sqlId, RowMapper<T> rowMapper, Object model, Object[] args) {
        String sql = getSqlStatment(sqlId, model);
        return jdbcTemplate.queryForObject(sql, rowMapper, args);
    }

    protected String getSqlStatment(String sqlId, Object model) {
        String sql = null;
        // model为null则认为不需要ftl处理，直接从map中获取
        if (model == null && sqlMap != null) {
            sql = sqlMap.get(sqlId);
        } else {
            sql = sqlProcessor.process(getFullSqlId(sqlId), model);
        }
        logger.debug("Sql statment {}: {}", sqlId, sql);
        return sql;

    }

    public List<T> queryBySqlId(String sqlId, Object model, Object[] args) {
        return this.queryBySqlId(sqlId, entityType, model, args);
    }

    protected <E> List<E> queryBySqlId(String sqlId, Class<E> elementType, Object model, Object[] args) {
        String sql = getSqlStatment(sqlId, model);
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<E>(elementType), args);
    }

    /**
     * <p>
     * 通过slId查询多个实体
     * </p>
     *
     * @param sqlId
     * @param rowMapper
     * @param model
     * @param args
     * @return
     */
    public List<T> queryBySqlId(String sqlId, RowMapper<T> rowMapper, Object model, Object[] args) {
        String sql = getSqlStatment(sqlId, model);
        List<T> list = jdbcTemplate.query(sql, args, rowMapper);
        ListUtils.removeNullElements(list);
        return list;
    }

    public Page<T> pageBySqlId(String sqlId, int pageIndex, int pageSize, Object model, Object[] args) {
        return this.pageBySqlId(sqlId, entityType, pageIndex, pageSize, model, args);
    }

    protected <T> Page<T> pageBySqlId(String sqlId, Class<T> elementType, int pageIndex, int pageSize, Object model,
                                          Object[] args) {
        String sql = getSqlStatment(sqlId, model);
        if (pageSize == 0) {
            pageSize = Page.DEFAULT_PAGE_SIZE;
        }
        if (pageIndex == 0) {
            pageIndex = Page.DEFAULT_PAGE_INDEX;
        }
        int first = (pageIndex - 1) * pageSize;

        args = args == null ? new Object[0] : args;
        Object[] array = args;
        // 构建分页
        String pageSql = dialectParser.getPageSql(sql);
        List<Object> pageParam = dialectParser.getPageParam(pageIndex, pageSize);
        for (Object obj : pageParam) {
            array = ObjectUtils.addObjectToArray(array, obj);
        }
        List<T> list = this.jdbcTemplate.query(pageSql, new BeanPropertyRowMapper<T>(elementType), array);
        // 查询总量
        final int totalCounts = this.count(sql, args);
        final Page<T> pageInfo = new Page<T>((ArrayList<T>) list, pageIndex, pageSize, totalCounts);
        return pageInfo;
    }

    /**
     * <p>
     * 指定sqlId查询分页
     * </p>
     *
     * @param sqlId
     * @param rowMapper
     * @param pageIndex
     * @param pageSize
     * @param model
     * @param args
     * @return
     */
    public Page<T> pageBySqlId(String sqlId, RowMapper<T> rowMapper, int pageIndex, int pageSize, Object model,
                                   Object[] args) {
        String sql = getSqlStatment(sqlId, model);
        if (pageSize == 0) {
            pageSize = Page.DEFAULT_PAGE_SIZE;
        }
        if (pageIndex == 0) {
            pageIndex = 1;
        }
        int first = (pageIndex - 1) * pageSize;

        args = args == null ? new Object[0] : args;
        Object[] array = args;
        // 构建分页
        String pageSql = dialectParser.getPageSql(sql);
        List<Object> pageParam = dialectParser.getPageParam(pageIndex, pageSize);
        for (Object obj : pageParam) {
            array = ObjectUtils.addObjectToArray(array, obj);
        }
        List<T> list = this.jdbcTemplate.query(pageSql, rowMapper, array);
        ListUtils.removeNullElements(list);
        // 查询总量
        final int totalCounts = count(sql, args);
        final Page<T> pageInfo = new Page<T>(list, pageIndex, pageSize, totalCounts);
        return pageInfo;
    }

    /**
     * <p>
     * 指定sqlId更新，需要自己编写缓存操作
     * </p>
     *
     * @param sqlId
     * @param model
     * @param args
     * @return
     */
    protected int updateBySqlId(String sqlId, Object model, Object[] args) {
        String sql = getSqlStatment(sqlId, model);
        return this.jdbcTemplate.update(sql, args);
    }

    /**
     * <p>
     * 指定sqlId更新，需要自己编写缓存操作
     * </p>
     *
     * @param sqlId
     * @param model
     * @param args
     * @return
     */
    protected int deleteBySqlId(String sqlId, Object model, Object[] args) {
        String sql = getSqlStatment(sqlId, model);
        return this.jdbcTemplate.update(sql, args);
    }

    public T getBySqlId(String sqlId, Object model) {
        return this.getBySqlId(sqlId, model, null);
    }

    public T getBySqlId(String sqlId, Object[] args) {
        return this.getBySqlId(sqlId, null, args);
    }

    public List<T> queryBySqlId(String sqlId, Object model) {
        return this.queryBySqlId(sqlId, model, null);
    }

    public List<T> queryBySqlId(String sqlId, Object[] args) {
        return this.queryBySqlId(sqlId, null, args);
    }

    public Page<T> pageBySqlId(String sqlId, int pageIndex, int pageSize, Object model) {
        return this.pageBySqlId(sqlId, pageIndex, pageSize, model, null);
    }

    public Page<T> pageBySqlId(String sqlId, int pageIndex, int pageSize, Object[] args) {
        return this.pageBySqlId(sqlId, pageIndex, pageSize, null, args);
    }

    @Override
    public int updateByExample(T entity, T example) {
        final Sql sql = sqlBuilder.buildUpdateByExampleSql(entity, example, entityInfo);
        logger.debug("Update by example sql: {}", sql);
        return jdbcTemplate.update(sql.getStatement(), sql.getArgs());
    }

    @Override
    public int updateSelectiveByExample(T entity, T example) {
        final Sql sql = sqlBuilder.buildUpdateSelectiveByExampleSql(entity, example, entityInfo);
        logger.debug("Update selective by example sql: {}", sql);
        return jdbcTemplate.update(sql.getStatement(), sql.getArgs());
    }

    @Override
    public int deleteByExample(T entity) {
        final Sql sql = sqlBuilder.buildDeleteByExampleSql(entity, entityInfo);
        logger.debug("Delete by example sql: {}", sql);
        return jdbcTemplate.update(sql.getStatement(), sql.getArgs());
    }

    @Override
    public int countBySqlId(String sqlId, Object model, Object[] args) {
        return this.getBySqlId(sqlId, Integer.class, model, args);
    }

    /**
     * <p>
     * 获取当前实体的缓存，用于在代码中操作缓存
     * </p>
     *
     * @return
     */
    protected ICache getCache() {
        if (cacheManager == null) {
            return null;
        }
        return cacheManager.getCache(this.getEntityName());
    }

    /**
     * <p>
     * 获取指定名称的缓存，用于在代码中操作缓存
     * </p>
     *
     * @param name
     * @return
     */
    protected ICache getCache(String name) {
        if (cacheManager == null) {
            return null;
        }
        return cacheManager.getCache(name);
    }

}
