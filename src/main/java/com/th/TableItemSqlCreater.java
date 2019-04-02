package com.th;

/**
 * @author tianheng
 */
public class TableItemSqlCreater extends BaseSqlCreater implements ISqlCreater {

    private String tableName;

    public TableItemSqlCreater(String tableName) {
        this.tableName = tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableName() {
        return tableName;
    }

    @Override
    public String getInsertTableDefPrefix() {
        return " insert into doc_table_item(descriptions, name, note, tableName) ";
    }

    @Override
    public String getTableValueDefPrefix() {
        return " VALUES( ";
    }

    @Override
    public String getInsertSuffix() {
        return " ); ";
    }

    @Override
    public String getFileName() {
        return "C:/Users/Administrator/Desktop/insert.sql";
    }

    @Override
    public void setFileContent(String content) {
        super.setContent(content);
    }

    @Override
    public String createTableDefSqlStatement(String values) {
        StringBuilder sb = new StringBuilder(getInsertTableDefPrefix());
        sb.append(getTableValueDefPrefix())
                .append(values)
                .append(", '")
                .append(tableName)
                .append("'")
                .append(getInsertSuffix());
        return sb.toString();
    }
}
