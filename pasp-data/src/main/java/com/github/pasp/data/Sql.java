package com.github.pasp.data;

import java.util.List;

public class Sql {
	private String statement;
	private Object[] args;

	private List<Object[]> listArgs;

	public Sql(String statement, Object[] args) {
		super();
		this.statement = statement;
		this.args = args;
	}

	public Sql(String statement, List<Object[]> listArgs) {
		super();
		this.statement = statement;
		this.listArgs = listArgs;
	}

	public String getStatement() {
		return statement;
	}

	public void setStatement(String statement) {
		this.statement = statement;
	}

	public Object[] getArgs() {
		return args;
	}

	public void setArgs(Object[] args) {
		this.args = args;
	}

	public List<Object[]> getListArgs() {
		return listArgs;
	}

	public void setListArgs(List<Object[]> listArgs) {
		this.listArgs = listArgs;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{statement: ").append(statement);
		if (args != null) {
			sb.append(", args: ");
			visitArray(sb, args);
		}
		if (listArgs != null) {
			sb.append(", list args: [");
			int i = 0;
			for (Object[] arr : listArgs) {
				if (i++ != 0) {
					sb.append(", ");
				}
				if (arr != null) {
					visitArray(sb, arr);
				}
			}
			sb.append("]");
		}
		return sb.append("}").toString();
	}

	private void visitArray(StringBuilder sb, Object[] args) {
		sb.append("[");
		for (int i = 0; i < args.length; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(args[i]);
		}
		sb.append("]");
	}

}
