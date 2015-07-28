package alt.sqlite2;

import android.content.ContentValues;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import alt.sqlite2.Interface.ISQLiteCommandAdapter;
import alt.sqlite2.Interface.ISQLiteConnection;
import alt.sqlite2.Interface.ISQLiteInsertOperation;
import alt.sqlite2.Interface.ISQLiteInsertProcedure;

/**
 * Created by lawrence on 7/24/15.
 */
public class SQLiteInsertOperation<T> implements ISQLiteInsertOperation<T>,ISQLiteInsertProcedure{
    protected ISQLiteCommandAdapter mSQLiteCommandAdapter;
    protected ISQLiteConnection mSQLiteConnection;
    protected ContentValues mContentValues = new ContentValues();
    protected JoinTable[] mJoinTables;

    public SQLiteInsertOperation(ISQLiteConnection SQLiteConnection,ISQLiteCommandAdapter SQLiteCommandAdapter){
        this.mSQLiteConnection = SQLiteConnection;
        this.mSQLiteCommandAdapter = SQLiteCommandAdapter;
    }

    @Override
    public void loopColumnName(ISQLiteCommandAdapter commandAdapter) {
        String columnName[] = commandAdapter.getColumnName();
        for(int i=0;i<columnName.length;i++){
            Class columnType = commandAdapter.getTypeByColumnName(columnName[i]);
            try {
                if(columnName[i].equals(commandAdapter.getIDColumnName()) && commandAdapter.getValueByColumnName(columnName[i],columnType).toString().equals("0")) {
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
    public boolean handleIdField(ISQLiteCommandAdapter commandAdapter,String columnName,ContentValues contentValues) throws IllegalAccessException {
        contentValues.putNull(columnName);
        return true;
    }

    @Override
    public boolean handleJoinTable(ISQLiteCommandAdapter commandAdapter,String columnName,JoinTable[] joinTables) throws IllegalAccessException {
        joinTables = (JoinTable[]) commandAdapter.getValueByColumnName(columnName, commandAdapter.getTypeByColumnName(columnName));
        for(int k=0;k<joinTables.length;k++) {
            joinTables[k].entity1name = commandAdapter.getObjectClass().getSimpleName();
            joinTables[k].name = "JOINTABLE_"+new Integer(Math.abs(joinTables[k].entity1name.hashCode()+joinTables[k].entity2name.hashCode())).toString();
        }
        mJoinTables = joinTables;
        return true;
    }

    @Override
    public boolean handleField(ISQLiteCommandAdapter commandAdapter,String columnName,ContentValues contentValues) throws IllegalAccessException {
        Class columnType = commandAdapter.getTypeByColumnName(columnName);
        Object value = commandAdapter.getValueByColumnName(columnName,columnType);
        putInContentValues(contentValues,columnType,columnName,value);
        return true;
    }

    @Override
    public long executeInsert(String tableName, ContentValues contentValues) {
        return mSQLiteConnection.getOpenedDatabase().insert(tableName,null,contentValues);
    }

    @Override
    public long[] executeJoinTableInsert(long fkentityId,JoinTable[] joinTables) {
        long result[] = new long[joinTables.length];
        for(int i=0;i<joinTables.length;++i){
            joinTables[i].entity1Id = fkentityId;
            result[i] = new SQLiteJoinTableCommand(mSQLiteConnection).insertEntity(joinTables[i]).id;
            if(result[i]==-1)
                return null;
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

    @Override
    public EntityResult execute() {
        loopColumnName(mSQLiteCommandAdapter);
        long result = executeInsert(mSQLiteCommandAdapter.getObjectClass().getSimpleName(),mContentValues);
        Map<String, long[]> innerEntityMap = new HashMap<>();
        if(result!=-1 && null!=mJoinTables) {
            long[] jointableResult = executeJoinTableInsert(result, mJoinTables);
            innerEntityMap.put(mJoinTables[0].getEntity2name(), jointableResult);
        }
        return new EntityResult(SQLiteOperationType.INSERT, result, innerEntityMap);
    }
}
