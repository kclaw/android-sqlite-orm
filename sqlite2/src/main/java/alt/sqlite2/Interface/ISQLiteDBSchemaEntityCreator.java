package alt.sqlite2.Interface;

import java.lang.reflect.Field;

/**
 * Created by root on 7/9/15.
 */
public interface ISQLiteDBSchemaEntityCreator {
    String createEntity();
    String createData(Field field);
    void generateDBSchema();
    void setDDLStatement(String statement);
    String getDDLStatement();
    void addSQLiteEntityCreator(ISQLiteDBSchemaEntityCreator creator);
    void removeSQLiteEntityCreator(ISQLiteDBSchemaEntityCreator creator);
    String getEntityName();
    void setEntityName(String name);
    boolean isJoinTable();
}
