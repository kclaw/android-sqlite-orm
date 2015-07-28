package alt.sqlite2;

import android.content.ContentValues;
import android.database.Cursor;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import alt.sqlite2.Interface.IDataBase;
import alt.sqlite2.Interface.IEntity;
import alt.sqlite2.Interface.ISQLiteCommandAdapter;
import alt.sqlite2.Interface.ISQLiteConnection;
import alt.sqlite2.Interface.ISQLiteEntityAdapter;
import alt.sqlite2.Interface.ISQLiteEntityCommand;
import alt.sqlite2.Interface.ISQLiteInsertOperation;
import alt.sqlite2.Interface.ISQLiteReadOperation;
import alt.sqlite2.Interface.ISQLiteUpdateOperation;

/**
 * Created by lawrence on 7/15/15.
 */
public class SQLiteEntityCommand<T> extends SQLiteCommand implements ISQLiteEntityCommand<T> {
    protected ISQLiteCommandAdapter mSQLiteCommandAdapter;
    protected ISQLiteEntityAdapter<T> mSQLiteEntityAdapter;
    static IDataBase sDataBase;


    private SQLiteEntityCommand(ISQLiteConnection sqliteConnection, String command) {
        super(sqliteConnection, command);
    }

    public SQLiteEntityCommand(IDataBase dataBase, ISQLiteConnection sqLiteConnection) {
        super(sqLiteConnection);
        this.sDataBase = dataBase;
    }


    @Override
    public T insertEntity(T t) {

        mSQLiteCommandAdapter = new SQLiteCommandAdapter<>(sDataBase,t);
        mSQLiteEntityAdapter = new SQLiteEntityAdapter<T>(sDataBase,t);
       // HashMap<String,ContentValues> command = new HashMap<>();
        /*JoinTable[] joinTableToBeAdded = null;
        String columnName[] = mSQLiteCommandAdapter.getColumnName();
        ContentValues contentValues = new ContentValues();
        for(int i=0;i<columnName.length;i++){
            Class columnType = mSQLiteCommandAdapter.getTypeByColumnName(columnName[i]);
            try {
                    if(columnName[i].equals(mSQLiteCommandAdapter.getIDColumnName()) && mSQLiteCommandAdapter.getValueByColumnName(columnName[i],columnType).toString().equals("0")) {
                        contentValues.putNull(mSQLiteCommandAdapter.getIDColumnName());
                        continue;
                    }
                    if(mSQLiteCommandAdapter.getTypeByColumnName(columnName[i])!=JoinTable[].class)
                        putInContentValues(contentValues, mSQLiteCommandAdapter.getTypeByColumnName(columnName[i]), columnName[i], mSQLiteCommandAdapter.getValueByColumnName(columnName[i], mSQLiteCommandAdapter.getTypeByColumnName(columnName[i])));
                    else {
                        JoinTable[] joinTable = (JoinTable[]) mSQLiteCommandAdapter.getValueByColumnName(columnName[i], mSQLiteCommandAdapter.getTypeByColumnName(columnName[i]));
                        for(int k=0;k<joinTable.length;k++) {
                            joinTable[k].entity1name = t.getClass().getSimpleName();
                            joinTable[k].name = "JOINTABLE_"+new Integer(Math.abs(joinTable[k].entity1name.hashCode()+joinTable[k].entity2name.hashCode())).toString();
                        }
                        joinTableToBeAdded = joinTable;
                    }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        long result = mSqLiteConnection.getOpenedDatabase().insert(mSQLiteCommandAdapter.getTableName(),null,contentValues);
        if(result!=-1) {
            if(null!= joinTableToBeAdded){
                for(int i=0;i<joinTableToBeAdded.length;i++){
                    joinTableToBeAdded[i].entity1Id = result;
                    new SQLiteJoinTableCommand(mSqLiteConnection).insertEntity(joinTableToBeAdded[i]);
                }
            }
            Cursor cursor = mSqLiteConnection.getOpenedDatabase().query(mSQLiteCommandAdapter.getTableName(),
                    mSQLiteCommandAdapter.getEntityColumnName(), mSQLiteCommandAdapter.getIDColumnName() + " = " + result, null,
                    null, null, null);
            cursor.moveToFirst();
            return mSQLiteEntityAdater.getEntity(cursor);
        }
        return t;*/
        ISQLiteInsertOperation<T> insertOperation = new SQLiteInsertOperation<T>(mSqLiteConnection,mSQLiteCommandAdapter);
        EntityResult entityResult = insertOperation.execute();
        ISQLiteReadOperation<T> readOperation = new SQLiteReadOperation<T>(mSqLiteConnection,mSQLiteCommandAdapter,mSQLiteEntityAdapter);
        T t2 = (T)readOperation.read(entityResult);
        return t2;
    }
    @Override
    public long updateEntity(T t) {
        mSQLiteCommandAdapter = new SQLiteCommandAdapter<>(sDataBase,t);
        mSQLiteEntityAdapter = new SQLiteEntityAdapter<T>(sDataBase,t);
        ISQLiteUpdateOperation updateOperation = new SQLiteUpdateOperation(mSqLiteConnection,mSQLiteCommandAdapter);
        EntityResult entityResult = updateOperation.execute();
        return entityResult.getId();
    }

    @Override
    public boolean deleteEntity(long id) {
        return false;
    }

    @Override
    public T readEntity(long id) {
        return null;
    }

    @Override
    public List readAllEntity() {
        return null;
    }

    @Override
    public int count() {
        return 0;
    }

}
