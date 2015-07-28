package alt.sqlite2;

import alt.sqlite2.Interface.IDataBase;
import alt.sqlite2.Interface.IEntity;
import alt.sqlite2.Interface.ISQLiteDBSchemaEntityCreator;

/**
 * Created by root on 7/9/15.
 */
public class SQLiteDBSchemaEntityCreator extends AbsSQLiteDBSchemaEntityCreator {

    public SQLiteDBSchemaEntityCreator(IDataBase dataBase,IEntity entity){
        super(dataBase,entity);
    }

    @Override
    public void generateDBSchema() {
        super.generateDBSchema();
        for(ISQLiteDBSchemaEntityCreator c:mSQLiteDBSchemaEntityCreatorList) {
            c.generateDBSchema();
        }
    }
}
