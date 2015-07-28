package alt.sqlite2;

import android.database.Cursor;

import alt.sqlite2.Interface.ISQLiteCommandAdapter;
import alt.sqlite2.Interface.ISQLiteConnection;
import alt.sqlite2.Interface.ISQLiteEntityAdapter;
import alt.sqlite2.Interface.ISQLiteReadOperation;

/**
 * Created by lawrence on 7/26/15.
 */
public class SQLiteReadOperation<T> implements ISQLiteReadOperation<T> {

    ISQLiteConnection mSQLiteConnection;
    ISQLiteCommandAdapter mSQLiteCommandAdapter;
    ISQLiteEntityAdapter<T> mSQLiteEntityAdapter;


    public SQLiteReadOperation(ISQLiteConnection sqliteConnection,ISQLiteCommandAdapter sqliteCommandAdapter,ISQLiteEntityAdapter sqliteEntityAdapter){
        this.mSQLiteConnection = sqliteConnection;
        this.mSQLiteCommandAdapter = sqliteCommandAdapter;
        this.mSQLiteEntityAdapter = sqliteEntityAdapter;
    }
    @Override
    public T read(EntityResult entityResult) {
        Cursor cursor = mSQLiteConnection.getOpenedDatabase().query(mSQLiteCommandAdapter.getTableName(),mSQLiteCommandAdapter.getEntityColumnName(),mSQLiteCommandAdapter.getIDColumnName()+"="+entityResult.getId(),null,null,null,null);
        return (T)mSQLiteEntityAdapter.getEntity(cursor);
    }
}
