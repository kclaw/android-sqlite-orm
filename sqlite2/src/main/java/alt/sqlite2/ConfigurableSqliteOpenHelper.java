package alt.sqlite2;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import alt.sqlite2.Interface.ISQLiteDbConfiguration;

/**
 * Created by root on 15年3月27日.
 */
public class ConfigurableSqliteOpenHelper extends SQLiteOpenHelper {
    protected ISQLiteDbConfiguration mSqliteDbConfiguration;

    public ConfigurableSqliteOpenHelper(Context context, ISQLiteDbConfiguration sqlitedbconfiguration){
        super(context,sqlitedbconfiguration.getDataBaseName(),null,sqlitedbconfiguration.getDatabaseVersion());
        this.mSqliteDbConfiguration = sqlitedbconfiguration;
    }
    public ConfigurableSqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public ConfigurableSqliteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //db.setForeignKeyConstraintsEnabled(true);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(this.getClass().getSimpleName(), "onCreate(),db" + db.getPath());
        if(null== mSqliteDbConfiguration){
            Log.w(this.getClass().getSimpleName(),"onCreate(SQLiteDatabase),SqlLiteSavable is null");
            return;
        }
        for(String sql:mSqliteDbConfiguration.getDataDescriptionSQL()){
            db.execSQL(sql);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(this.getClass().getSimpleName(),"onUpgrade(SQLiteDatabase,int,int),"+
                "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + mSqliteDbConfiguration.getDataBaseName());
        onCreate(db);
    }
}
