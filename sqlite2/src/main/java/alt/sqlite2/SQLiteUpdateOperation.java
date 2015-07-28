package alt.sqlite2;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import alt.sqlite2.Interface.ISQLiteCommandAdapter;
import alt.sqlite2.Interface.ISQLiteConnection;
import alt.sqlite2.Interface.ISQLiteUpdateOperation;
import alt.sqlite2.Interface.ISQLiteUpdateProcedure;

/**
 * Created by lawrence on 7/27/15.
 */
public class SQLiteUpdateOperation implements ISQLiteUpdateOperation,ISQLiteUpdateProcedure {
    protected ISQLiteCommandAdapter mSQLiteCommandAdapter;
    protected ISQLiteConnection mSQLiteConnection;
    protected ContentValues mContentValues = new ContentValues();
    protected JoinTable[] mJoinTables;
    protected String mWhere;
    protected String[] mWhereArgs;

    public SQLiteUpdateOperation(ISQLiteConnection connection,ISQLiteCommandAdapter commandAdapter){
        this.mSQLiteConnection = connection;
        this.mSQLiteCommandAdapter = commandAdapter;
    }
    @Override
    public EntityResult execute() {
        loopColumnName(mSQLiteCommandAdapter);
        long result = executeUpdate(mSQLiteCommandAdapter.getObjectClass().getSimpleName(),mContentValues,mWhere,mWhereArgs);
        Map<String, long[]> innerEntityMap = new HashMap<>();
        if(result!=-1 && null!=mJoinTables) {
            mSQLiteConnection.getOpenedDatabase().delete(mJoinTables[0].getName(),mJoinTables[0].entity1name+"ID = ?",new String[]{new  Long(mJoinTables[0].entity1Id).toString()});
            long[] jointableResult = executeJoinTableUpdate(result, mJoinTables);
            innerEntityMap.put(mJoinTables[0].getEntity2name(), jointableResult);
        }
        return new EntityResult(SQLiteOperationType.UPDATE, result, innerEntityMap);
    }

    @Override
    public void loopColumnName(ISQLiteCommandAdapter commandAdapter) {
        String columnName[] = commandAdapter.getColumnName();
        for(int i=0;i<columnName.length;i++){
            try {
                if(columnName[i].equals(commandAdapter.getIDColumnName())) {
                    handleIdField(commandAdapter,columnName[i],mContentValues);
                    continue;
                }
                if(commandAdapter.getTypeByColumnName(columnName[i])!=JoinTable[].class)
                    handleField(commandAdapter,columnName[i],mContentValues);
                else
                    handleJoinTable(commandAdapter,columnName[i],mJoinTables);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean handleIdField(ISQLiteCommandAdapter commandAdapter, String columnName, ContentValues contentValues) throws IllegalAccessException {
        mWhere = columnName + " = ?";
        Class columnType = commandAdapter.getTypeByColumnName(columnName);
        Object value = commandAdapter.getValueByColumnName(columnName,columnType);
        mWhereArgs = new String[]{value.toString()};
        return handleField(commandAdapter,columnName,contentValues);
    }

    @Override
    public boolean handleJoinTable(ISQLiteCommandAdapter commandAdapter, String columnName, JoinTable[] joinTables) throws IllegalAccessException {
        joinTables = (JoinTable[]) commandAdapter.getValueByColumnName(columnName, commandAdapter.getTypeByColumnName(columnName));
        for(int k=0;k<joinTables.length;k++) {
            joinTables[k].entity1name = commandAdapter.getObjectClass().getSimpleName();
            joinTables[k].name = "JOINTABLE_"+new Integer(Math.abs(joinTables[k].entity1name.hashCode()+joinTables[k].entity2name.hashCode())).toString();
        }
        mJoinTables = joinTables;
        return true;
    }

    @Override
    public boolean handleField(ISQLiteCommandAdapter commandAdapter, String columnName, ContentValues contentValues) throws IllegalAccessException {
        Class columnType = commandAdapter.getTypeByColumnName(columnName);
        Object value = commandAdapter.getValueByColumnName(columnName,columnType);
        putInContentValues(contentValues,columnType,columnName,value);
        return true;
    }

    @Override
    public long executeUpdate(String tableName, ContentValues contentValues,String where,String whereArgs[]) {
        return mSQLiteConnection.getOpenedDatabase().update(tableName,contentValues,where,whereArgs);
    }

    //not in use
    public boolean handleUpdateInManyToManyRelationship(String joinTableName,String entity1Name,String entity2Name,long entity1Id,Set<Long> newEntity2IdSet){
        String joinTableColumnNames[] = new String[]{"id",entity1Name+"ID",entity2Name+"ID"};
        Cursor cursor = mSQLiteConnection.getOpenedDatabase().query(joinTableName,joinTableColumnNames,entity1Name+"ID = ?",new String[]{new Long(entity1Id).toString()},null,null,null);
        HashSet<Long> oldentity2IdSet = new HashSet<>();
        for(cursor.moveToFirst();!cursor.isAfterLast();cursor.moveToNext()){
            oldentity2IdSet.add(cursor.getLong(cursor.getColumnIndex(entity2Name+"ID")));
        }

        oldentity2IdSet.removeAll(newEntity2IdSet);
        for(Long entity2Id:oldentity2IdSet)
            mSQLiteConnection.getOpenedDatabase().delete(joinTableName,entity2Name+"ID = ?",new String[]{new Long(entity2Id).toString()});

        return true;
    }
    @Override
    public long[] executeJoinTableUpdate(long fkentity1Id, JoinTable[] joinTables) {
        long result[] = new long[joinTables.length];
        for(int i=0;i<joinTables.length;++i){
            joinTables[i].entity1Id = fkentity1Id;
            result[i] = new SQLiteJoinTableCommand(mSQLiteConnection).updateEntity(joinTables[i]);
        }
        return result;
    }

    @Override
    public boolean putInContentValues(ContentValues contentValues, Class columnType, String columnName, Object value) {
        Method method = null;
        if(null!=value) {
            try {
                method = ContentValues.class.getMethod("put", new Class[]{String.class, columnType});
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
                return false;
            }

            try {
                method.invoke(contentValues, columnName, value);
                return true;
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                return false;
            } catch (InvocationTargetException e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }
}
