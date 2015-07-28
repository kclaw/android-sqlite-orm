package alt.sqlite2.Interface;

import android.content.ContentValues;

import alt.sqlite2.JoinTable;

/**
 * Created by lawrence on 7/27/15.
 */
public interface ISQLiteUpdateProcedure {
    void loopColumnName(ISQLiteCommandAdapter commandAdapter);
    boolean handleIdField(ISQLiteCommandAdapter commandAdapter,String columnName,ContentValues contentValues) throws IllegalAccessException;
    boolean handleJoinTable(ISQLiteCommandAdapter commandAdapter,String columnName,JoinTable[] joinTables) throws IllegalAccessException;
    boolean handleField(ISQLiteCommandAdapter commandAdapter,String columnName,ContentValues contentValues) throws IllegalAccessException;
    long executeUpdate(String tableName,ContentValues contentValues,String where,String[] whereArgs);
    long[] executeJoinTableUpdate(long fkentity1Id,JoinTable[] joinTables);
    boolean putInContentValues(ContentValues contentValues,Class columnType,String columnName,Object value);
}
