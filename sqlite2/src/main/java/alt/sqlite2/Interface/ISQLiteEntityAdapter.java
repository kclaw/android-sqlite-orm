package alt.sqlite2.Interface;

import android.database.Cursor;

/**
 * Created by root on 15年3月27日.
 */
public interface ISQLiteEntityAdapter<T> {
    T getEntity(Cursor cursor);
}
