package alt.sqlite2;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import alt.sqlite2.Interface.IDataBase;
import alt.sqlite2.Interface.IDataBaseAnalyzer;
import alt.sqlite2.Interface.IDataBaseCreator;
import alt.sqlite2.Interface.IEntity;
import alt.sqlite2.Interface.ISQLiteDBSchemaEntityCreator;
import alt.sqlite2.Interface.RelationshipType;

/**
 * Created by lawrence on 7/14/15.
 */
public class SQLiteDataBaseCreator implements IDataBaseCreator ,IDataBaseAnalyzer{
    private  IDataBase mDataBase;
    List<IEntity> mEntityList = new ArrayList<IEntity>();
    Map<IEntity,Boolean> mEntityCreatedMap = new HashMap<>();
    ISQLiteDBSchemaEntityCreator mSQLiteDBSchemaEntityCreator;

    public SQLiteDataBaseCreator(IDataBase dataBase){
        this.mDataBase = dataBase;
    }
    @Override
    public void createDataBase(IDataBase database) {
        for(int i=0;i<mEntityList.size();i++)
            mEntityCreatedMap.put(mEntityList.get(i),false);
        for(int i=0;i<mEntityList.size();i++) {
            this.createEntity(mEntityList.get(i));
            mEntityCreatedMap.put(mEntityList.get(i),true);
        }
    }

    @Override
    public void createEntity(IEntity entity) {
        if(null==mSQLiteDBSchemaEntityCreator)
            mSQLiteDBSchemaEntityCreator = new SQLiteDBSchemaEntityCreator(mDataBase,entity);
        else {
            if (entity instanceof JoinTableModel) {
                JoinTableModel jt = (JoinTableModel) entity;
                mSQLiteDBSchemaEntityCreator.addSQLiteEntityCreator(new SQLiteDBSchemaJoinTableCreator(mDataBase,this.mSQLiteDBSchemaEntityCreator, jt.getEntity1(),jt.getEntity2()));
            }else
            mSQLiteDBSchemaEntityCreator.addSQLiteEntityCreator(new SQLiteDBSchemaEntityCreatorDecorator(mDataBase,entity,this.mSQLiteDBSchemaEntityCreator));
        }
    }

    @Override
    public void createJoinTable(ISQLiteDBSchemaEntityCreator creator,IEntity entity1, IEntity entity2) {
        ISQLiteDBSchemaEntityCreator c = new SQLiteDBSchemaJoinTableCreator(mDataBase,creator,entity1,entity2);
        creator.addSQLiteEntityCreator(creator);
    }

    @Override
    public String generateDBSchema() {
        this.analyzeDataBase(mDataBase);
        this.createDataBase(mDataBase);
        this.mSQLiteDBSchemaEntityCreator.generateDBSchema();
        return this.mSQLiteDBSchemaEntityCreator.getDDLStatement();
    }

    @Override
    public boolean isOnDataBaseCreationList(IEntity entity) {
        for(int i=0;i<mEntityList.size();i++)
            if(mEntityList.get(i).equals(entity))
                return true;
        return false;
    }


    @Override
    public void analyzeDataBase(IDataBase dataBase) {
        mEntityList = dataBase.getAllEntities();
        for(int i=0;i<mEntityList.size();i++)
        if(mEntityList.get(i).getConnectingEntities().size()!=0)
            for(int k=0;k<mEntityList.get(i).getConnectingEntities().size();k++) {
                IEntity entity2 = mEntityList.get(i).getConnectingEntities().get(k);
                if (mEntityList.get(i).getRelationship(entity2).getRelationshipType() == RelationshipType.ManyToMany ) {
                    JoinTableModel jt = new JoinTableModel(mEntityList.get(i), entity2);
                    if(!isOnDataBaseCreationList(jt))
                    mEntityList.add(jt);
                }
            }
    }

    @Override
    public void setDataBase(IDataBase dataBase) {
        this.mDataBase = dataBase;
    }

    @Override
    public IDataBase getDataBase() {
        return this.mDataBase;
    }
}
