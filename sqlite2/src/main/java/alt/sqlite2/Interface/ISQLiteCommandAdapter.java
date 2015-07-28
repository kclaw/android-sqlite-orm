package alt.sqlite2.Interface;

import java.util.List;

/**
 * Created by root on 15年3月27日.
 */
public interface ISQLiteCommandAdapter {
    <Y> Y getValueByColumnName(String columnName, Class<Y> yClass) throws IllegalAccessException;
    Class getTypeByColumnName(String columnName);
    Class getObjectClass();
    String getTableName();
    String getIDColumnName();
    String[] getColumnName();
    String[] getEntityColumnName();
    String getWhereClause();
    String getSelectArgs();
    String[] getGroupBy();
    String[] getHaving();
    String[] getOrderBy();
}
