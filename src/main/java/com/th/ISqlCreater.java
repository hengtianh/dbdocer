package com.th;

public interface ISqlCreater extends Runnable {

	/**
	 * 获得插入数据表语句的前缀
	 * @return
	 */
	String getInsertTableDefPrefix();
	/**
	 * 获得连接字段值得前缀
	 * @return
	 */
	String getTableValueDefPrefix();
	/**
	 * 获得插入数据表语句的后缀
	 * @return
	 */
	String getInsertSuffix();

	/**
	 * 设置sql文件内容
	 * @param content
	 */
	void setFileContent(String content);

	/**
	 * 创建sql语句默认实现方法
	 * @param values
	 * @return
	 */
	default String createTableDefSqlStatement(String values) {
		StringBuilder sb = new StringBuilder(getInsertTableDefPrefix());
		sb.append(getTableValueDefPrefix())
		  .append(values)
		  .append(getInsertSuffix());
//		System.out.println(sb.toString());
		return sb.toString();
	}
}
