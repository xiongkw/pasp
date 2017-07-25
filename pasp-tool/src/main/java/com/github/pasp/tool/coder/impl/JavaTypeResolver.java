package com.github.pasp.tool.coder.impl;

import java.sql.Types;
import java.util.Date;

public class JavaTypeResolver {

	private static final String BYTE_NAME = Byte.class.getName();
	private static final String FLOAT_NAME = Float.class.getName();
	private static final String DOUBLE_NAME = Double.class.getName();
//	private static final String SHORT_NAME = Short.class.getName();
	private static final String INTEGER_NAME = Integer.class.getName();
//	private static final String BIGDECIMAL_NAME = BigDecimal.class.getName();
	private static final String DATE_NAME = Date.class.getName();
	private static final String STRING_NAME = String.class.getName();
	private static final String BOOLEAN_NAME = Boolean.class.getName();
	private static final String BYTES_NAME = "byte[]";
	private static final String LONG_NAME = Long.class.getName();
	private static final String OBJECT_NAME = Object.class.getName();

	public static String calculateJavaType(int dbType, boolean forceLong) {
		String answer;

		switch (dbType) {
		/*
		 * Object
		 */
		case Types.ARRAY:
			;
		case Types.DATALINK:
			;
		case Types.DISTINCT:
			;
		case Types.JAVA_OBJECT:
			;
		case Types.NULL:
			;
		case Types.OTHER:
			;
		case Types.REF:
			;
		case Types.STRUCT:
			answer = OBJECT_NAME;
			break;
		/*
		 * Long
		 */
		case Types.BIGINT:
			answer = LONG_NAME;
			break;
		/*
		 * byte[]
		 */
		case Types.BINARY:
			;
		case Types.BLOB:
			;
		case Types.LONGVARBINARY:
			;
		case Types.VARBINARY:
			answer = BYTES_NAME;
			break;
		/*
		 * boolean
		 */
		case Types.BIT:
			;
		case Types.BOOLEAN:
			answer = BOOLEAN_NAME;
			break;
		/*
		 * String
		 */
		case Types.CHAR:
			;
		case Types.LONGVARCHAR:
			;
		case Types.CLOB:
			;
		case Types.VARCHAR:
			answer = STRING_NAME;
			break;

		/*
		 * Date
		 */
		case Types.DATE:
			;
		case Types.TIME:
			;
		case Types.TIMESTAMP:
			answer = DATE_NAME;
			break;

		/*
		 * double
		 */
		case Types.DOUBLE:
			;
		case Types.FLOAT:
			answer = DOUBLE_NAME;
			break;
		/*
		 * integer
		 */
		case Types.INTEGER:
			if (forceLong) {
				answer = LONG_NAME;
			} else {
				answer = INTEGER_NAME;
			}
			break;
		/*
		 * float
		 */
		case Types.REAL:
			answer = FLOAT_NAME;
			break;
		/*
		 * short
		 */
		case Types.SMALLINT:
			if (forceLong) {
				answer = LONG_NAME;
			} else {
				answer = INTEGER_NAME;
			}
			break;
		/*
		 * byte
		 */
		case Types.TINYINT:
			answer = BYTE_NAME;
			break;
		/*
		 * deimal
		 */
		case Types.DECIMAL:
			;
		case Types.NUMERIC:
			answer = DOUBLE_NAME;
			break;

		default:
			answer = null;
			break;
		}

		return answer;
	}
}
