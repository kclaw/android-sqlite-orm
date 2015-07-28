package alt.sqlite2;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import alt.sqlite2.Annotation.EntityId;
import alt.sqlite2.Interface.IDataBase;
import alt.sqlite2.Interface.IEntity;
import alt.sqlite2.Interface.IFieldManager;
import alt.sqlite2.Interface.ISQLiteCommandAdapter;

/**
 * Created by root on 7/8/15.
 */
public class SQLiteCommandAdapter<T> implements ISQLiteCommandAdapter {
    protected ISQLiteCommandAdapter mSQLiteCommandAdapter;
    T mObject;
    IDataBase mDataBase;

    public SQLiteCommandAdapter(IDataBase dataBase,T object) {
        this.mObject = object;
        this.mDataBase = dataBase;
    }

    @Override
    public <Y> Y getValueByColumnName(String columnName, Class<Y> yClass) throws IllegalAccessException {
        Field field = null;
        Field[] fieldsInEntity = mObject.getClass().getFields();
        for(int i=0;i<fieldsInEntity.length;i++){
            if(fieldsInEntity[i].getName().equals(columnName)){
                field = fieldsInEntity[i];
                break;
            }
        }
        if(null!=field){
            EntityFieldManager fieldManager = new EntityFieldManager(mDataBase,field);
            return (Y)fieldManager.getValue(mObject,yClass);
        }
        return null;
    }

    @Override
    public Class getTypeByColumnName(String columnName)
    {
        Field field = null;
        Field[]  fieldsInEntity = mObject.getClass().getFields();
        for(int i=0;i<fieldsInEntity.length;i++){
            field = fieldsInEntity[i];
            if(field.getName().equals(columnName)){
                EntityFieldManager fieldManager = new EntityFieldManager(mDataBase,field);
                return fieldManager.getType(mDataBase.findEntityByName(mObject.getClass().getSimpleName()));
            }
        }
        return null;
    }

    @Override
    public Class getObjectClass() {
        return mObject.getClass();
    }

    @Override
    public String getTableName() {
        if(mObject.getClass().getSimpleName().equals(JoinTable.class.getSimpleName()))
            return ((JoinTable)mObject).name;
        return mObject.getClass().getSimpleName();
    }

    @Override
    public String getIDColumnName() {
        Field field = new ClassManager().findFieldAnnotatedWith(mObject.getClass(), EntityId.class);
        if(null!=field)
            return field.getName();
        return null;
    }

    @Override
    public String[] getColumnName() {
        Field[] fields = mObject.getClass().getFields();
        String[] result = new String[fields.length];
        for(int i=0;i<fields.length;i++){
            result[i] = fields[i].getName();
        }
        return result;
    }

    @Override
    public String[] getEntityColumnName() {
        Field[] fields = mObject.getClass().getFields();
        List<String> validFieldList = new ArrayList<>();
        for(int i=0;i<fields.length;i++){
            EntityFieldManager fieldManager = new EntityFieldManager(mDataBase,fields[i]);
            if(null!=fieldManager.getDataType(mDataBase.findEntityByName(mObject.getClass().getSimpleName())))
                validFieldList.add(fields[i].getName());
        }
        String[] validFieldArray = new String[validFieldList.size()];
        return validFieldList.toArray(validFieldArray);
    }

    @Override
    public String getWhereClause() {
        return null;
    }

    @Override
    public String getSelectArgs() {
        return null;
    }

    @Override
    public String[] getGroupBy() {
        return new String[0];
    }

    @Override
    public String[] getHaving() {
        return new String[0];
    }

    @Override
    public String[] getOrderBy() {
        return new String[0];
    }


}
