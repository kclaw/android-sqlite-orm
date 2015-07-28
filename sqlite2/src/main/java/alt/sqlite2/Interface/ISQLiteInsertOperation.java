package alt.sqlite2.Interface;

import android.content.ContentValues;

import java.util.Map;

import alt.sqlite2.JoinTable;

/**
 * Created by lawrence on 7/24/15.
 */
public interface ISQLiteInsertOperation<T> {
    alt.sqlite2.EntityResult execute();
}
