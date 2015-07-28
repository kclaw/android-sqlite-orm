package alt.sqlite2.Interface;

import android.content.Context;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import alt.sqlite2.JoinTableModel;

/**
 * Created by root on 7/12/15.
 */
public interface IDataBase {
    void createEntities(File jarfile);
    void createEntities(File[] files);
    void createEntities(ISQLiteConnection sqliteConnection);
    void createEntities(Context context);
    IEntity findEntityByName(String name);
    IEntity findEntityByClass(Class cx);
    IRelationship findRelationshipBetween(Field field,IEntity entity1,IEntity entity2);
    List<IEntity> findAllConnectingEntity(IEntity entity);
    IEntity findConnectingEntityByName(IEntity entity,String name);
    List<IEntity> getAllEntities();
    Map<IEntity,List<IRelationship>> getAllEntityRelationshipMap();
    JoinTableModel findJoinTable(IEntity entity1,IEntity entity2);
    String  getIDNameFromEntity(IEntity entity);
}
