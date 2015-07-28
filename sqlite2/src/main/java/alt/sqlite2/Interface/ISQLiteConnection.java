package alt.sqlite2.Interface;

import android.database.sqlite.SQLiteDatabase;

/**
 * Created by root on 15年3月27日.
 */
public interface ISQLiteConnection {
    SQLiteDatabase getOpenedDatabase();
    boolean isOpen();
}
