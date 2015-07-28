package alt.sqlite2;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import alt.sqlite2.Interface.ISQLiteConnection;
import alt.sqlite2.Interface.ISQLiteConnectionOperation;
import alt.sqlite2.Interface.ISQLiteDbConfiguration;

/**
 * Created by root on 7/8/15.
 */
public final class SQLiteConnection implements ISQLiteConnection,ISQLiteConnectionOperation {

    private SQLiteOpenHelper mSQLiteOpenHelper;
    private SQLiteDatabase mSQLiteDatabase;
    private boolean mIsOpened = false;

    public SQLiteConnection(Context context,ISQLiteDbConfiguration sqLiteDbConfiguration){
        mSQLiteOpenHelper = new ConfigurableSqliteOpenHelper(context,sqLiteDbConfiguration);
    }
    @Override
    public SQLiteDatabase getOpenedDatabase() {
        if(null!=mSQLiteDatabase && mIsOpened==true)
            return mSQLiteDatabase;
        return null;
    }

    @Override
    public boolean isOpen() {
        return mIsOpened;
    }

    @Override
    public void Open() {
        mSQLiteDatabase = mSQLiteOpenHelper.getWritableDatabase();
        mIsOpened = true;
    }

    @Override
    public void Close() {
        mIsOpened = false;
        mSQLiteDatabase.close();
        mSQLiteDatabase = null;
    }
}
