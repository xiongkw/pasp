package com.github.pasp.data;

public interface IErrorConstants {

	String SHARDING_BEAN_CONF_ERROR = "没有配置分片注解@ShardingBean {0}";

	String SHARDING_ID_ALL_NULL = "所有分片ID {0} 都为空";

	String SHARDING_ID_INSERT_NULL = "@ShardingId {0}@{1} 的insertRequired必须为true，即insert语句的分片键必须有值";

	String SHARDING_ID_IS_ID = "@ShardingId {0}@{1} 与主键id同个字段时，@ShardingId除了insertRequired为false外，其它三个属性必须全为true";

	String SHARDING_ID_NULL_UAD = "@ShardingId {0} 一个或一个以上分片键的updateRequired(deleteRequired)必须有一个为true，即update(delete)语句的分片键必须其中一个有值";

	String ENTITY_NO_ID_DEF = "实体 {0} 没有定义主键@Id";

	String ENTITY_NO_TABLE_DEF = "实体 {0} 没有定义表@Table";

	String ENTITY_NO_CONSTRUCTOR_DEF = "实体 {0} 没有定义默认构造器";

	String ENTITY_SHARDING_ID_NULL = "分片ID {0}@{1} 为空";

	String ENTITY_ID_NULL = "主键ID {0}@{1} 为空";

	String ERR_SQL_PROCESS = "Sql解析错误 {0}";

	String ERR_SQL_FTL_NOT_FOUND = "Sql模板 {0} 不存在";
}
