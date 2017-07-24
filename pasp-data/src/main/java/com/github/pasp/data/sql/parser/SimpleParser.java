package com.github.pasp.data.sql.parser;

import java.util.regex.Pattern;

import com.github.pasp.data.IDialectParser;
import com.github.pasp.data.sql.Dialect;

public abstract class SimpleParser implements IDialectParser {
    private static Pattern p = Pattern.compile("order[\\s]+by[\\s]+([\\w\\._]+([\\s]+(asc|desc))?[\\s]*,[\\s]*)*[\\w\\._]+([\\s]+(asc|desc))?", Pattern.CASE_INSENSITIVE | Pattern.MULTILINE);
    private static IDialectParser parse = null;

    public static IDialectParser getParse(Dialect dialect) {
        if (parse == null) {
            if (dialect == Dialect.mysql) {
                if (parse == null) {
                    parse = new MySqlParser();
                }
            } else if (dialect == Dialect.oracle) {
                if (parse == null) {
                    parse = new OracleParser();
                }
            }
        }
        return parse;
    }

    @Override
    public String getCountSql(String sql) {
        sql = normalize(sql);
        StringBuffer countSql = new StringBuffer();
        countSql.append("select count(1) from (");
        countSql.append(p.matcher(sql).replaceAll(""));
        countSql.append(") count_temp");
        return countSql.toString();
    }

    protected String normalize(String sql) {
        sql = sql.trim();
        if(sql.endsWith(";")){
            sql = sql.substring(0, sql.length()-1);
        }
        return sql;
    }

}
