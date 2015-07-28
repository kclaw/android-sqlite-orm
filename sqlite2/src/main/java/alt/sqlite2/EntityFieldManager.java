package alt.sqlite2;

import android.content.ContentValues;
import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import alt.sqlite2.Annotation.Entity;
import alt.sqlite2.Annotation.EntityId;
import alt.sqlite2.Interface.IClassManager;
import alt.sqlite2.Interface.IDataBase;
import alt.sqlite2.Interface.IEntity;
import alt.sqlite2.Interface.IEntityCheck;
import alt.sqlite2.Interface.IEntityFieldManager;
import alt.sqlite2.Interface.IEntityMapper;
import alt.sqlite2.Interface.IRelationship;
import alt.sqlite2.Interface.ITypeConverter;
import alt.sqlite2.Interface.RelationshipType;

/**
 * Created by root on 7/11/15.
 */

public class EntityFieldManager extends FieldManager implements IEntityFieldManager,IEntityMapper {
    protected IDataBase mDatabase;

    private EntityFieldManager(Field field) {
        super(field);
    }
    public EntityFieldManager(IDataBase dataBase,Field field){
        super(field);
        this.mDatabase = dataBase;
    }

    @Override
    public String getDataType() {
        if(mField.getType().isAnnotationPresent(Entity.class))
            return mField.getName()+" "+mapToEntity();
        else if(mField.isAnnotationPresent(EntityId.class))
            return mField.getName()+" "+mapToEntityId();
        return super.getDataType();
    }

    @Override
    public <T> T getValue(Object object, Class<T> tClass) throws IllegalAccessException {
        if(mField.getType().isAnnotationPresent(Entity.class)) {
            IEntity entity = mDatabase.findEntityByClass(object.getClass());
            T t = (T)mField.get(object);
            if(null!=t) {
                IEntity innerEntity = mDatabase.findEntityByClass(mField.getType());
                Field field = innerEntity.findFieldByFieldName(mDatabase.getIDNameFromEntity(innerEntity));
                return (T) field.get(t);
            }
        }
        if(mField.isAnnotationPresent(EntityId.class)){
            return (T) mField.get(object);
        }
        if(isEntityCollection(List.class)){
            ClassManager classManager = new ClassManager();
            Class cx = classManager.getTypeArgumentInGenericTypeFieldInClass(mField);
            IEntity innerEntity = mDatabase.findEntityByName(cx.getSimpleName());
           // IEntity entity = mDatabase.findEntityByClass(cx);
            Relationship relationship = (Relationship)mDatabase.findRelationshipBetween(mField,innerEntity.findConnectingEnityByField(mField),innerEntity);

                List list = (List)mField.get(object);
                if(null!=list){

                    if(relationship.getRelationshipType()==RelationshipType.OneToMany) {
                        long[] result = new long[list.size()];
                        byte[] resultByte = new byte[0];
                        for (int i = 0; i < list.size(); i++) {
                            Field field = innerEntity.findFieldByFieldName(mDatabase.getIDNameFromEntity(innerEntity));
                            result[i] = (long) field.get(list.get(i));
                            ITypeConverter typeConverter = new TypeConverter();
                            resultByte = typeConverter.convertToByteArray(result);
                            return (T) resultByte;
                        }
                    }else if(relationship.getRelationshipType()==RelationshipType.ManyToMany){
                        JoinTable[] joinTable = new JoinTable[list.size()];
                        for(int i=0;i<list.size();i++) {
                            Field field = innerEntity.findFieldByFieldName(mDatabase.getIDNameFromEntity(innerEntity));
                            //  result[i] = (long) field.get(list.get(i));
                            joinTable[i] = new JoinTable(0, 0, (long) field.get(list.get(i)), "");
                            joinTable[i].entity2name = innerEntity.getEntityName();
                        }
                        return (T) joinTable;
                    }
                }
           }
        if(isEntityArray()){
            Class cx =  mField.getType().getComponentType();
                if(null==mField.get(object))
                    return null;
                int length = Array.getLength(mField.getType().cast(mField.get(object)));
                long[] result = new long[length];
                byte[] resultByte = new byte[0];
                for (int i = 0; i < length; i++) {
                    IEntity componentEntity = mDatabase.findEntityByClass(cx);
                    Field field = componentEntity.findFieldByFieldName(mDatabase.getIDNameFromEntity(componentEntity));
                    result[i] = (long) field.get(Array.get(mField.getType().cast(mField.get(object)), i));
                    ITypeConverter typeConverter = new TypeConverter();
                    resultByte = typeConverter.convertToByteArray(result);
                }

            return (T)resultByte;
        }
        return super.getValue(object, tClass);
    }

    @Override
    public void setValue(IEntity self, Object object, Object value) throws IllegalAccessException {
        if(isEntity()){
            IEntity innerEntity = mDatabase.findEntityByName(mField.getType().getSimpleName());
            Object innerEntityObject = null;
            try {
                innerEntityObject = innerEntity.getEntityClass().newInstance();
                try {
                    Field field = innerEntity.getEntityClass().getField(mDatabase.getIDNameFromEntity(innerEntity)) ;
                    field.set(innerEntityObject,value);
                } catch (NoSuchFieldException e) {
                    e.printStackTrace();
                }
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
            mField.set(object,innerEntityObject);
            return;
        }
        if(isEntityArray()){
            byte[] bytes = (byte[]) value;
            Class cx = mField.getType().getComponentType();
            try {
                IEntity innerentity = mDatabase.findEntityByName(cx.getSimpleName());
                Field field = cx.getField(mDatabase.getIDNameFromEntity(innerentity));
                //only support long[] or int[]
                if(field.getType().equals(Long.TYPE)){
                    ITypeConverter typeConverter = new TypeConverter();
                    long[] values = typeConverter.convertToLongArray(bytes);
                    Object[] o = (Object[])Array.newInstance(innerentity.getEntityClass(), values.length);
                    for(int i=0;i<values.length;i++){
                        try {
                            Object innerEntityObject = innerentity.getEntityClass().newInstance();
                            field.set(innerEntityObject,values[i]);
                            Array.set(o,i,innerEntityObject);
                        } catch (InstantiationException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if(field.getType().equals(Integer.TYPE)){
                    ITypeConverter typeConverter = new TypeConverter();
                    int[] values = typeConverter.convertToIntArray(bytes);
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            return;
        }
        super.setValue(object,value);
    }

    @Override
    public Class getType(IEntity self){
        if(mField.getType().isAnnotationPresent(Entity.class)){
            return mapFromEntity();
        }
        if(mField.isAnnotationPresent(EntityId.class)){
            return mapFromEntityId();
        }
        if(isEntityCollection(List.class)){
            return mapFromEntityList(getEntityTypeInGenericType() != null ? getEntityTypeInGenericType().getRelationship(self).getRelationshipType() : null);
        }
        if(isEntityArray()) {
            return mapFromEntityArray(self.getRelationship(mDatabase.findEntityByClass(mField.getType().getComponentType())).getRelationshipType());
        }
        return getType();
    }
    @Override
    public String getDataType(IEntity self){
       if(isEntityCollection(List.class)){
            if(null==mapToEntityList(getEntityTypeInGenericType() != null ? getEntityTypeInGenericType().getRelationship(self).getRelationshipType() : null))
                return null;
           return mField.getName()+" "+mapToEntityList(getEntityTypeInGenericType() != null ? getEntityTypeInGenericType().getRelationship(self).getRelationshipType() : null);
        }
        if(isEntityArray()) {
            if(null==mapToEntityArray(self.getRelationship(mDatabase.findEntityByClass(mField.getType().getComponentType())).getRelationshipType()))
                return null;
            return mField.getName() + " " + mapToEntityArray(self.getRelationship(mDatabase.findEntityByClass(mField.getType().getComponentType())).getRelationshipType());
        }
        if(isEntity())
            return mField.getName() + " " + mapToEntity();
        return getDataType();
    }
    @Override
    public String getIDNameFromEntity(){
         if(isEntity()){
             Field[] fields = mField.getType().getFields();
             for(int i=0;i<fields.length;i++)
                 if(fields[i].isAnnotationPresent(EntityId.class))
                     return fields[i].getName();
         }
         return null;
    }
    @Override
    public boolean isEntity(){
        return mField.getType().isAnnotationPresent(Entity.class);
    }

    @Override
    public boolean isEntityCollection(Class collectionClass){
        ClassManager classManager = new ClassManager();
      //  Log.d(this.getClass().getSimpleName(), classManager.findTypeArgumentInGenericTypeField(mField).toString());
        if(mField.getType().isAssignableFrom(collectionClass) && classManager.findTypeArgumentInGenericTypeField(mField)!=null)
            for(int i=0;i<mDatabase.getAllEntities().size();i++) {
              //  Log.d(this.getClass().getSimpleName(),mDatabase.getAllEntities().get(i).getEntityClass().toString());
              //  Log.d(this.getClass().getSimpleName(),classManager.getTypeArgumentInGenericTypeFieldInClass(mField).getSimpleName());
                if (mDatabase.getAllEntities().get(i).getEntityClass().getSimpleName().equals(classManager.getTypeArgumentInGenericTypeFieldInClass(mField).getSimpleName()));
                    return true;
            }
        return false;
    }

    @Override
    public boolean isEntityArray(){
        if(mField.getType().getName().charAt(0)=='[' && mField.getType().getComponentType().isAnnotationPresent(Entity.class))
            return true;
        return false;
    }

    @Override
    public IEntity getEntityTypeInGenericType(){
        IClassManager classManager = new ClassManager();
        for(int i=0;i<mDatabase.getAllEntities().size();i++)
            if(mDatabase.getAllEntities().get(i).getEntityClass().getSimpleName().equals(classManager.getTypeArgumentInGenericTypeFieldInClass(mField).getSimpleName()))
                return mDatabase.getAllEntities().get(i);
        return null;
    }

    @Override
    public String mapToEntity() {
        return "INTEGER";
    }

    @Override
    public String mapToEntityId() {
        return "INTEGER PRIMARY KEY AUTOINCREMENT";
    }

    @Override
    public String mapToEntityArray(RelationshipType relationshipType) {
        if(relationshipType == RelationshipType.OneToMany)
            return "BLOB";
        if(relationshipType == RelationshipType.ManyToMany)
            return null;
        return "";
    }

    @Override
    public String mapToEntityList(RelationshipType relationshipType) {
        if(relationshipType == RelationshipType.OneToMany)
            return "BLOB";
        if(relationshipType == RelationshipType.ManyToMany)
            return null;
        return "";
    }

    @Override
    public Class mapFromEntity() {
        return Long.class;
    }

    @Override
    public Class mapFromEntityId() {
        return Long.class;
    }

    @Override
    public Class mapFromEntityArray(RelationshipType relationshipType) {
        if(relationshipType==RelationshipType.ManyToMany)
            return Long.class;
        if(relationshipType==RelationshipType.OneToMany)
            return byte[].class;
        return null;
    }

    @Override
    public Class mapFromEntityList(RelationshipType relationshipType) {
         if(relationshipType==RelationshipType.ManyToMany)
             //return Long.class;
             return JoinTable[].class;
         if(relationshipType==RelationshipType.OneToMany)
             return byte[].class;
        return null;
    }


}
