package alt.sqlite2.Interface;

import android.database.Cursor;

/**
 * Created by root on 7/11/15.
 */
public interface ISQLiteCommandOperation {
    Cursor select();
    long insert();
    long delete();
    long update();
}
