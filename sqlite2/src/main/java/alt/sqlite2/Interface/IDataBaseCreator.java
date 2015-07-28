package alt.sqlite2.Interface;

/**
 * Created by lawrence on 7/14/15.
 */
public interface IDataBaseCreator {
    void createDataBase(IDataBase database);
    void createEntity(IEntity entity);
    void createJoinTable(ISQLiteDBSchemaEntityCreator creator,IEntity entity1,IEntity entity2);
    String generateDBSchema();
    boolean isOnDataBaseCreationList(IEntity entity);
}
