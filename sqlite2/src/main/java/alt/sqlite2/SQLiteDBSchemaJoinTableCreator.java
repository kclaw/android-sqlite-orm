package alt.sqlite2;

import alt.sqlite2.Annotation.EntityId;
import alt.sqlite2.Interface.IClassManager;
import alt.sqlite2.Interface.IDataBase;
import alt.sqlite2.Interface.IEntity;
import alt.sqlite2.Interface.IFieldManager;
import alt.sqlite2.Interface.ISQLiteDBSchemaEntityCreator;

/**
 * Created by root on 7/10/15.
 */
public class SQLiteDBSchemaJoinTableCreator extends AbsSQLiteDBSchemaEntityCreator {
    final String JOIN_TABLE_PREFIX = "JOINTABLE_";
    private ISQLiteDBSchemaEntityCreator mSQLiteDBSchemeEntityCreator;
    private IEntity mEntity2;

    public SQLiteDBSchemaJoinTableCreator(IDataBase dataBase,ISQLiteDBSchemaEntityCreator creator,IEntity entity1,IEntity entity2){
        super(dataBase,entity1);
        this.mEntity2 = entity2;
        this.mSQLiteDBSchemeEntityCreator = creator;
        this.mEntityName = JOIN_TABLE_PREFIX + new Integer(Math.abs(mEntity.getEntityName().hashCode()+mEntity2.getEntityName().hashCode())).toString();
        //this.mEntityName = mEntity.getEntityClass().getSimpleName()+mEntity2.getEntityClass().getSimpleName()+JOIN_TABLE_SUFFIX;
    }

    @Override
    public String createEntity() {
        IClassManager classManager = new ClassManager();
        String class1Id = classManager.findFieldAnnotatedWith(mEntity.getEntityClass(), EntityId.class).getName();
        String class2Id = classManager.findFieldAnnotatedWith(mEntity2.getEntityClass(), EntityId.class).getName();
        String sql = "create table "+mEntityName+" (";
        sql+= "id INTEGER PRIMARY KEY AUTOINCREMENT,";
        sql+= mEntity.getEntityClass().getSimpleName()+"ID INTERGER,";
        sql+= mEntity2.getEntityClass().getSimpleName()+"ID INTEGER,";
        sql+= "FOREIGN KEY("+mEntity.getEntityClass().getSimpleName()+"ID) REFERENCES "+mEntity.getEntityClass().getSimpleName()+"("+class1Id+"),";
        sql+= "FOREIGN KEY("+mEntity2.getEntityClass().getSimpleName()+"ID) REFERENCES "+mEntity2.getEntityClass().getSimpleName()+"("+class2Id+")";
        sql+=");";
        mSQLiteDBSchemeEntityCreator.setDDLStatement(mSQLiteDBSchemeEntityCreator.getDDLStatement()+sql);
        return sql;
    }

    @Override
    public boolean isJoinTable() {
        return  true;
    }
}
