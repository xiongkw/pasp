package com.github.pasp.data.sql.parser;

import java.util.ArrayList;
import java.util.List;

public class MySqlParser extends SimpleParser {
    
    @Override
    public String getPageSql(String sql) {
        sql = normalize(sql);
        StringBuilder sqlBuilder = new StringBuilder(sql.length() + 14);
        sqlBuilder.append(sql);
        sqlBuilder.append(" limit ?,?");
        return sqlBuilder.toString();
    }
    
    @Override
    public List<Object> getPageParam(int page, int pageSize) {
        List<Object> params = new ArrayList<Object>();
        params.add((page - 1) * pageSize);
        params.add(pageSize);
        return params;
    }
    
}
