package alt.sqlite2;

import alt.sqlite2.Annotation.*;
import alt.sqlite2.Interface.IDataBase;
import alt.sqlite2.Interface.IEntity;
import alt.sqlite2.Interface.ISQLiteDBSchemaEntityCreator;

/**
 * Created by root on 7/9/15.
 */
public class SQLiteDBSchemaEntityCreatorDecorator extends AbsSQLiteDBSchemaEntityCreator {

    private ISQLiteDBSchemaEntityCreator mSQLiteDBSchemaEntityCreator;

    public SQLiteDBSchemaEntityCreatorDecorator(IDataBase dataBase,IEntity entity,ISQLiteDBSchemaEntityCreator creator){
        super(dataBase,entity);
        this.mSQLiteDBSchemaEntityCreator = creator;
    }
    @Override
    public String createEntity() {
        String newSql=  mSQLiteDBSchemaEntityCreator.getDDLStatement() + super.createEntity();
        this.mSQLiteDBSchemaEntityCreator.setDDLStatement(newSql);
        return newSql;
    }


}
