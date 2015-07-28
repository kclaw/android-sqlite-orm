package alt.sqlite2.Interface;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by root on 7/11/15.
 */
public interface IEntity {
    void setEntityName(String name);
    void setEntityClass(Class cx);
    void setId(long id);
    long getId();
    void addConnectingEntity(IEntity entity);
    void removeConnectingEntity(IEntity entity);
    void addRelationShip(Field field,IEntity connectingEntity,IRelationship relationShip);
    void removeAllRelationship(IEntity connectingEntity);
    String getEntityName();
    Class getEntityClass();
    List<IEntity> getConnectingEntities();
    IRelationship getRelationship(IEntity connectingEntity);
    Field findConnectingEntityFieldByFieldName(String fieldName);
    Field findFieldByFieldName(String fieldName);
    IRelationship getRelationship(Field field);
    IEntity findConnectingEnityByField(Field field);
    IEntity findConnectingEntityByClass(Class cx);
}
