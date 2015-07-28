package alt.sqlite2;

import alt.sqlite2.Interface.ISQLiteCommand;
import alt.sqlite2.Interface.ISQLiteConnection;

/**
 * Created by root on 7/8/15.
 */
public class SQLiteCommand implements ISQLiteCommand {
    private String mCommand;
    protected ISQLiteConnection mSqLiteConnection;

    public SQLiteCommand(ISQLiteConnection sqliteConnection,String command){
        this.mSqLiteConnection = sqliteConnection;
        this.mCommand = command;
    }
    public SQLiteCommand(ISQLiteConnection sqLiteConnection){
        this.mSqLiteConnection = sqLiteConnection;
    }

    @Override
    public void executeNonQuery() {
        if(null!=mCommand)
            if(null!=mSqLiteConnection)
                mSqLiteConnection.getOpenedDatabase().execSQL(mCommand);
    }

    @Override
    public void setCommand(String command) {
        this.mCommand = command;
    }

    @Override
    public String getCommand() {
        return this.mCommand;
    }

}
