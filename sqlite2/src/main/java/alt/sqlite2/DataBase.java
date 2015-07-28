package alt.sqlite2;

import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import alt.sqlite2.Annotation.EntityId;
import alt.sqlite2.Interface.IDataBase;
import alt.sqlite2.Interface.IEntity;
import alt.sqlite2.Interface.IEntityCheck;
import alt.sqlite2.Interface.IFileManager;
import alt.sqlite2.Interface.IRelationship;
import alt.sqlite2.Interface.ISQLiteConnection;
import alt.sqlite2.Interface.RelationshipType;
import dalvik.system.DexFile;

/**
 * Created by root on 7/12/15.
 */
 public class DataBase implements IDataBase{
    private IEntityCheck entityCheck = new EntityChecker();
    protected List<IEntity> mEntityList = new ArrayList<IEntity>();
    protected Map<IEntity,List<IRelationship>> mEntityRelationshipMap;

    @Override
    public IEntity findEntityByName(String name) {
        for(int i=0;i<mEntityList.size();i++)
            if(mEntityList.get(i).getEntityName().equals(name))
                return mEntityList.get(i);
        return null;
    }

    @Override
    public IEntity findEntityByClass(Class cx) {
        for(int i=0;i<mEntityList.size();i++)
            if(mEntityList.get(i).getEntityClass().getSimpleName().equals(cx.getSimpleName()))
                return mEntityList.get(i);
        return null;
    }


    @Override
    public IRelationship findRelationshipBetween(Field entity1Field ,IEntity entity1, IEntity entity2) {
        /*
        * if type of entity1Field is List of entity2 and entity2 have List of entity1 type of its any fields,
        * return Relationshiptype Many-to-Many
        * if type of entity1Field is Array of entity2 and entity2 have Array of entity1 type of its any fields,
        * return Relationshiptype Many-to-Many
        * if type of entity1Field is List of entity2 and entity2 do not have any fields with List of entity1 type,
        * return Relationshiptype One-to-Many
        * * if type of entity1Field is ArraY of entity2 and entity2 do not have any fields with Array of entity1 type,
        * return Relationshiptype One-to-Many
        * if type of entity1Field is entity2, return Relationshiptype One-to-One
        * */

        FieldManager fieldManager = new FieldManager(entity1Field);
        ClassManager classManager = new ClassManager();
        if(fieldManager.isAssignableFromListInterface() && fieldManager.getClassInGenericType().getSimpleName().equals(entity2.getEntityClass().getSimpleName()))
            if(classManager.isGenericTypeFieldExists(entity2.getEntityClass(),List.class,entity1.getEntityClass()))
                return new Relationship(entity1,entity2,RelationshipType.ManyToMany);

        if(fieldManager.isArray()&&entity1Field.getType().getComponentType().getSimpleName().equals(entity2.getEntityClass().getSimpleName()))
             if(entity2.getEntityClass().isArray() && classManager.isArrayFieldExists(entity2.getEntityClass(),entity1.getEntityClass()))
                 return new Relationship(entity1,entity2,RelationshipType.ManyToMany);

        if(fieldManager.isAssignableFromListInterface() && fieldManager.getClassInGenericType().getSimpleName().equals(entity2.getEntityClass().getSimpleName()))
            if(!classManager.isGenericTypeFieldExists(entity2.getEntityClass(),List.class,entity1.getEntityClass()))
                return new Relationship(entity1,entity2,RelationshipType.OneToMany);

        if(fieldManager.isArray() && entity1Field.getType().getComponentType().getSimpleName().equals(entity2.getEntityClass().getSimpleName()))
            if(!(entity2.getEntityClass().isArray() && classManager.isArrayFieldExists(entity2.getEntityClass(),entity1.getEntityClass())))
                return new Relationship(entity1,entity2,RelationshipType.OneToMany);

        if(entity1Field.getType().getSimpleName().equals(entity2.getEntityClass().getSimpleName()))
                return new Relationship(entity1,entity2,RelationshipType.OneToOne);
        return null;
    }

    @Override
    public List<IEntity> findAllConnectingEntity(IEntity entity) {
        ClassManager classManager = new ClassManager();
        List<IEntity> matchEntityList = new ArrayList<IEntity>();

        for(int i=0;i<mEntityList.size();i++) {
            Class cx = mEntityList.get(i).getEntityClass();
            if (classManager.isFieldExist(cx,entity.getEntityClass())||classManager.isGenericTypeFieldExists(cx,List.class,entity.getEntityClass())||classManager.isArrayFieldExists(cx,entity.getEntityClass())) {
                matchEntityList.add(mEntityList.get(i));
            }
        }
        return matchEntityList;
    }

    @Override
    public IEntity findConnectingEntityByName(IEntity entity,String name) {
        return null;
    }

    public JoinTableModel findJoinTableEntity(IEntity entity1,IEntity entity2){
        for(int i=0;i<mEntityList.size();i++)
            if(mEntityList.get(i) instanceof JoinTableModel){
                JoinTableModel joinTableModel = (JoinTableModel)mEntityList.get(i);
                if(joinTableModel.getEntity1().equals(entity1) && joinTableModel.getEntity2().equals(entity2))
                    return (JoinTableModel)mEntityList.get(i);
                if(joinTableModel.getEntity1().equals(entity2) && joinTableModel.getEntity2().equals(entity1))
                    return (JoinTableModel)mEntityList.get(i);
            }
        return null;
    }
    @Override
    public List<IEntity> getAllEntities() {
        return this.mEntityList;
    }

    @Override
    public Map<IEntity, List<IRelationship>> getAllEntityRelationshipMap() {
        return this.mEntityRelationshipMap;
    }

    @Override
    public JoinTableModel findJoinTable(IEntity entity1, IEntity entity2) {
        return findJoinTable(entity1,entity2);
    }

    @Override
    public String getIDNameFromEntity(IEntity entity) {
        Field[] fields = entity.getEntityClass().getFields();
        for(int i=0;i<fields.length;i++){
            if(fields[i].isAnnotationPresent(EntityId.class))
                return fields[i].getName();
        }
        return null;
    }

    @Override
    public void createEntities(File file) {
        IFileManager fileManager = new FileManager();
        List<Class<?>> classList = null;
        try {
           classList = fileManager.findClassesInJarFile(file,null);

            for(int i=0;i<classList.size();i++) {
                IEntity entity;
                mEntityList.add(entity = new Entity(classList.get(i).getSimpleName()));
                entity.setEntityClass(classList.get(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        for(int i=0;i<mEntityList.size();i++){
            IEntity entity = mEntityList.get(i);
            List<IEntity> connectingEntity = findAllConnectingEntity(entity);
            for(int k=0;k<connectingEntity.size();k++) {
                entity.addConnectingEntity(connectingEntity.get(k));
                for(int o=0;o<entity.getEntityClass().getFields().length;o++)
                    if(null!=findRelationshipBetween(entity.getEntityClass().getFields()[o],entity,connectingEntity.get(k)))
                         entity.addRelationShip(entity.getEntityClass().getFields()[o],connectingEntity.get(k),findRelationshipBetween(entity.getEntityClass().getFields()[o],entity,connectingEntity.get(k)));
            }
        }
    }

    @Override
    public void createEntities(File[] files) {
        List<Class<?>> implementedClasses = new ArrayList<>();
        for(int i=0;i<files.length;i++)
            if(files[i].getName().endsWith(".class")){
                try {
                    Class cx = Class.forName(files[i].getName().substring(0,files[i].getName().lastIndexOf(".class")));
                    if(null!=cx)
                        implementedClasses.add(cx);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        for(int i=0;i<implementedClasses.size();i++) {
            IEntity entity;
            mEntityList.add(entity = new Entity(implementedClasses.get(i).getSimpleName()));
            entity.setEntityClass(implementedClasses.get(i));
        }
        for(int i=0;i<mEntityList.size();i++){
            IEntity entity = mEntityList.get(i);
            List<IEntity> connectingEntity = findAllConnectingEntity(entity);
            for(int k=0;k<connectingEntity.size();k++) {
                entity.addConnectingEntity(connectingEntity.get(k));
                for(int o=0;o<entity.getEntityClass().getFields().length;o++)
                    if(null!=findRelationshipBetween(entity.getEntityClass().getFields()[o],entity,connectingEntity.get(k)))
                        entity.addRelationShip(entity.getEntityClass().getFields()[o],connectingEntity.get(k),findRelationshipBetween(entity.getEntityClass().getFields()[o],entity,connectingEntity.get(k)));
            }
        }
    }

    @Override
    public void createEntities(ISQLiteConnection sqliteConnection) {

    }

    @Override
    public void createEntities(Context context) {
        List<String> matchList = new ArrayList<>();
        List<Class<?>> implementClassList = new ArrayList<>();
        try {
            DexFile df = new DexFile(context.getPackageCodePath());
            for (Enumeration<String> iter = df.entries(); iter.hasMoreElements();) {
                String s = iter.nextElement();
                if(s.matches("alt\\.entity\\.\\w+"))
                    matchList.add(s);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for(int i=0;i<matchList.size();i++) {
            try {
                Class cx = Class.forName(matchList.get(i));
                implementClassList.add(cx);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        for(int i=0;i<implementClassList.size();i++) {
            IEntityCheck entityCheck = new EntityChecker();
            IEntity entity = null;
            Class cx = implementClassList.get(i);
            if(entityCheck.checkForValidEntity(cx) && entityCheck.checkForValidEntityId(cx)) {
                mEntityList.add(entity = new Entity(implementClassList.get(i).getSimpleName()));
                entity.setEntityClass(implementClassList.get(i));
            }
        }
        for(int i=0;i<mEntityList.size();i++){
            IEntity entity = mEntityList.get(i);
            List<IEntity> connectingEntity = findAllConnectingEntity(entity);
            for(int k=0;k<connectingEntity.size();k++) {
                entity.addConnectingEntity(connectingEntity.get(k));
                for(int o=0;o<entity.getEntityClass().getFields().length;o++)
                    if(null!=findRelationshipBetween(entity.getEntityClass().getFields()[o],entity,connectingEntity.get(k)))
                        entity.addRelationShip(entity.getEntityClass().getFields()[o],connectingEntity.get(k),findRelationshipBetween(entity.getEntityClass().getFields()[o],entity,connectingEntity.get(k)));
            }
        }
    }
}
