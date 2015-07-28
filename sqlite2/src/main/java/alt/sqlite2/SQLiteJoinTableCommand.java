package alt.sqlite2;

import android.content.ContentValues;

import java.util.List;

import alt.sqlite2.Interface.ISQLiteConnection;
import alt.sqlite2.Interface.ISQLiteEntityAdapter;
import alt.sqlite2.Interface.ISQLiteEntityCommand;
import alt.sqlite2.JoinTable;
/**
 * Created by lawrence on 7/22/15.
 */
public class SQLiteJoinTableCommand extends SQLiteCommand implements ISQLiteEntityCommand<alt.sqlite2.JoinTable> {

    public SQLiteJoinTableCommand(ISQLiteConnection connection){
        super(connection);
    }

    @Override
    public alt.sqlite2.JoinTable insertEntity(alt.sqlite2.JoinTable joinTable) {
        //ISQLiteEntityAdapter sqliteEntityAdapter;
        ContentValues contentValues = new ContentValues();
        contentValues.put(joinTable.getEntity1name()+"ID",joinTable.getEntity1Id());
        contentValues.put(joinTable.getEntity2name()+"ID",joinTable.getEntity2Id());
        long result = this.mSqLiteConnection.getOpenedDatabase().insert(joinTable.getName(),null,contentValues);
        joinTable.id = result;
        return joinTable;
    }

    @Override
    public long updateEntity(alt.sqlite2.JoinTable joinTable) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(joinTable.getEntity1name()+"ID",joinTable.getEntity1Id());
        contentValues.put(joinTable.getEntity2name()+"ID",joinTable.getEntity2Id());
        long result = this.mSqLiteConnection.getOpenedDatabase().insert(joinTable.getName(),null,contentValues);
        joinTable.id = result;
        return joinTable.getId();
    }

    @Override
    public boolean deleteEntity(long id) {
        return false;
    }

    @Override
    public alt.sqlite2.JoinTable readEntity(long id) {
        return null;
    }

    @Override
    public List<alt.sqlite2.JoinTable> readAllEntity() {
        return null;
    }

    @Override
    public int count() {
        return 0;
    }
}
