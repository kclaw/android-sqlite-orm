package alt.sqlite2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import alt.sqlite2.Annotation.Entity;
import alt.sqlite2.Interface.IDataBase;
import alt.sqlite2.Interface.IEntity;
import alt.sqlite2.Interface.IEntityCheck;
import alt.sqlite2.Interface.IEntityModelAnalyzer;
import alt.sqlite2.Interface.IFieldManager;
import alt.sqlite2.Interface.ISQLiteDBSchemaEntityCreator;

/**
 * Created by root on 7/9/15.
 */
public abstract class AbsSQLiteDBSchemaEntityCreator implements ISQLiteDBSchemaEntityCreator ,IEntityModelAnalyzer{
    List<ISQLiteDBSchemaEntityCreator> mSQLiteDBSchemaEntityCreatorList = new ArrayList<ISQLiteDBSchemaEntityCreator>();
    protected IEntity mEntity;
    protected IDataBase mDataBase;
    protected String mEntityName;
    private IEntityCheck mEntityChecker = new EntityChecker();
    private List<Field> mFieldList = new ArrayList<Field>();
    public String mSQlDDLStatement = "";

    protected AbsSQLiteDBSchemaEntityCreator(IDataBase dataBase,IEntity entity){
        this.mDataBase = dataBase;
        this.mEntity = entity;
    }

    @Override
    public void analyzeEntity(IEntity entity) {
        this.mEntityName = entity.getEntityName();
        Field[] fields = entity.getEntityClass().getFields();
        mFieldList.clear();
        for(int i=0;i<fields.length;i++) {
            mFieldList.add(fields[i]);
        }
    }

    @Override
    public void setEntity(IEntity entity) {
        this.mEntity = entity;
    }

    @Override
    public IEntity getEntity() {
        return this.mEntity;
    }

    @Override
    public String createEntity(){
        String sql ="";
        sql += "create table "+mEntity.getEntityClass().getSimpleName()+" (";
        for(int i=0;i<mFieldList.size();i++) {
                sql += createData(mFieldList.get(i))==null?"":createData(mFieldList.get(i));
                if (i != mFieldList.size() - 1 && null !=createData(mFieldList.get(i+1)) && null !=createData(mFieldList.get(i)))
                    sql += ",";

        }
        sql += ");";
        return sql;
    }

    @Override
    public String createData(Field field) {
        EntityFieldManager fieldManager = new EntityFieldManager(mDataBase,field);
        return fieldManager.getDataType(mEntity);
    }

    @Override
    public boolean isJoinTable() {
        return false;
    }

    @Override
    public void generateDBSchema() {
        if(!isJoinTable())
            this.analyzeEntity(mEntity);
        this.mSQlDDLStatement = this.createEntity();
    }

    @Override
    public void setDDLStatement(String statement) {
        this.mSQlDDLStatement = statement;
    }

    @Override
    public String getDDLStatement() {
        return mSQlDDLStatement;
    }

    @Override
    public void addSQLiteEntityCreator(ISQLiteDBSchemaEntityCreator creator) {
        this.mSQLiteDBSchemaEntityCreatorList.add(creator);
    }

    @Override
    public void removeSQLiteEntityCreator(ISQLiteDBSchemaEntityCreator creator) {
        this.mSQLiteDBSchemaEntityCreatorList.remove(creator);
    }

    @Override
    public String getEntityName() {
        return this.mEntityName;
    }

    @Override
    public void setEntityName(String name) {
        this.mEntityName = name;
    }


}
