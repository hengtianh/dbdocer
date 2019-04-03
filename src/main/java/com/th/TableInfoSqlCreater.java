package com.th;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

public class TableInfoSqlCreater extends BaseSqlCreater implements ISqlCreater {

	@Override
	public void setFileContent(String content) {
		super.setContent(content);
	}

/*
	 * public void createSqlStatement(String values) { StringBuilder sb = new
	 * StringBuilder(INSERT_TABLEDEF_PREFIX); sb.append(VALUES) .append(values)
	 * .append(INSERT_SUFFIX); System.out.println(sb.toString()); }
*/

	@Override
	public String getInsertTableDefPrefix() {
		// TODO Auto-generated method stub
		return " insert into doc_table(descriptions, name) ";
	}

	@Override
	public String getTableValueDefPrefix() {
		// TODO Auto-generated method stub
		return " VALUES( ";
	}

	@Override
	public String getInsertSuffix() {
		// TODO Auto-generated method stub
		return " ); ";
	}

	@Override
	public String getFileName() {
		// TODO Auto-generated method stub
		return "C:/Users/Administrator/Desktop/insert.sql";
	}
}
