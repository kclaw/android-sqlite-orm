package alt.sqlite2.Interface;

/**
 * Created by root on 7/11/15.
 */
public interface IEntityFieldManager extends IFieldManager{
    void setValue(IEntity self,Object object, Object value) throws IllegalAccessException;
    Class getType(IEntity self);
    String getDataType(IEntity self);
    String getIDNameFromEntity();
    boolean isEntity();
    boolean isEntityCollection(Class collectionClass);
    boolean isEntityArray();
    IEntity getEntityTypeInGenericType();
}
