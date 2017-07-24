package com.github.pasp.data.utils;

public class ColumnUtils {
	 /**
     * 把VO的属性名改对应的数据库表的字段名，如helloWorld->hello_world
     * 
     * @param propertyName
     * @return
     */
    public static String parsePropertyName2ColumnName(String propertyName) {
        StringBuilder result = new StringBuilder();
        if (propertyName != null && propertyName.length() > 0) {
            result.append(propertyName.substring(0, 1).toLowerCase());
            // 循环处理其余字符
            for (int i = 1; i < propertyName.length(); i++) {
                String s = propertyName.substring(i, i + 1);
                // 在大写字母前添加下划线
                if (s.equals(s.toUpperCase()) && !Character.isDigit(s.charAt(0))) {
                    result.append("_");
                }
                result.append(s.toLowerCase());
            }
        }
        return result.toString();
    }
    
    /**
     * 把数据库表的字段名改成对应VO的属性名，如ATTR_CODE改为attrCode
     * 
     * @param columnName
     * @return
     */
    public static String parseColumnName2PropertyName(String columnName) {
        StringBuffer sb = new StringBuffer();
        boolean flag = false;
        columnName = columnName.toLowerCase();
        for (int i = 0; i < columnName.length(); i++) {
            char ch = columnName.charAt(i);
            if (ch == '_') {
                flag = true;
                continue;
            } else {
                if (flag == true) {
                    sb.append(Character.toUpperCase(ch));
                    flag = false;
                } else {
                    sb.append(ch);
                }
            }
        }
        
        return sb.toString();
    }
}
