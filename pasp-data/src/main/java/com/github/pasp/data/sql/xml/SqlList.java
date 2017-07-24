package com.github.pasp.data.sql.xml;

import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
class SqlList {
	
	List<Sql> sqlList;

	@XmlElement(name="sql")
	public List<Sql> getSqlList() {
		return sqlList;
	}

	public void setSqlList(List<Sql> sqlList) {
		this.sqlList = sqlList;
	}

}
