package alt.sqlite2.Interface;

import android.database.Cursor;

import alt.sqlite2.EntityResult;

/**
 * Created by lawrence on 7/24/15.
 */
public interface ISQLiteReadOperation<T> {
    T read(EntityResult entityResult);
}
