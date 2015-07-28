package alt.sqlite2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import alt.sqlite2.Interface.IEntity;
import alt.sqlite2.Interface.IRelationship;

/**
 * Created by root on 7/12/15.
 */
public class Entity implements IEntity {
    protected String mEntityName;
    protected Class mClass;
    protected long mId;
    protected List<IEntity> mConnectingEntityList = new ArrayList<IEntity>();
    protected Map<Field,IEntity> mFieldEntityMap = new HashMap<>();
    protected Map<Field,IRelationship> mFieldRelationshipMap = new HashMap<>();

    public Entity(String name){
        this.mEntityName = name;
    }

    @Override
    public void setEntityName(String name) {
        this.mEntityName = name;
    }

    @Override
    public void setEntityClass(Class cx) {
        this.mClass = cx;
    }

    @Override
    public void setId(long id) {
        this.mId = id;
    }

    @Override
    public long getId() {
        return this.mId;
    }

    @Override
    public void addConnectingEntity(IEntity entity) {
        this.mConnectingEntityList.add(entity);
    }

    @Override
    public void removeConnectingEntity(IEntity entity) {
        this.mConnectingEntityList.remove(entity);
    }

    @Override
    public void addRelationShip(Field field,IEntity connectingEntity, IRelationship relationShip) {
        if(!mConnectingEntityList.contains(connectingEntity))
            return;
        mFieldEntityMap.put(field,connectingEntity);
        mFieldRelationshipMap.put(field,relationShip);
    }

    @Override
    public void removeAllRelationship(IEntity connectingEntity) {
        connectingEntity.removeAllRelationship(connectingEntity);
    }


    @Override
    public String getEntityName() {
        return this.mEntityName;
    }


    @Override
    public Class getEntityClass() {
        return this.mClass;
    }

    @Override
    public List<IEntity> getConnectingEntities() {
        return this.mConnectingEntityList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity)) return false;

        Entity entity = (Entity) o;

        if (mClass != null ? !mClass.equals(entity.mClass) : entity.mClass != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return mClass != null ? mClass.hashCode() : 0;
    }

    @Override
    public IRelationship getRelationship(IEntity connectingEntity) {
        for(Map.Entry<Field,IEntity> e:mFieldEntityMap.entrySet()){
            if(e.getValue().equals(connectingEntity))
                return mFieldRelationshipMap.get(e.getKey());
        }
        return null;
    }


    @Override
    public Field findConnectingEntityFieldByFieldName(String fieldName) {
        for(Map.Entry<Field,IEntity> e:mFieldEntityMap.entrySet()){
            if(e.getKey().getName().equals(fieldName))
                return e.getKey();
        }
        return null;
    }

    @Override
    public Field findFieldByFieldName(String fieldName) {
        Field[] fields = this.mClass.getFields();
        for(int i=0;i<fields.length;i++)
            if(fields[i].getName().equals(fieldName))
                return fields[i];
        return null;
    }

    @Override
    public IRelationship getRelationship(Field field) {
        return mFieldRelationshipMap.get(field);
    }

    @Override
    public IEntity findConnectingEnityByField(Field field) {
        for(IEntity entity:mConnectingEntityList){
            Field[] fields = entity.getEntityClass().getFields();
            for(int i=0;i<fields.length;i++)
                if(fields[i].getName().equals(field.getName()))
                    return entity;
        }
        return null;
    }

    @Override
    public IEntity findConnectingEntityByClass(Class cx) {
        for(IEntity entity:mConnectingEntityList){
            if(cx.getSimpleName().equals(entity.getEntityClass().getSimpleName()))
                return entity;
        }
        return null;
    }


}
