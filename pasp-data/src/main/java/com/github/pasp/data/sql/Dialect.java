package com.github.pasp.data.sql;

//数据库方言 - 使用枚举限制数据库类型
public enum Dialect {
    mysql, mariadb, sqlite, oracle, hsqldb, postgresql
}
