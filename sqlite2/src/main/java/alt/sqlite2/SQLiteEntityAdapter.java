package alt.sqlite2;

import android.database.Cursor;

import java.lang.reflect.Field;

import alt.sqlite2.Annotation.*;
import alt.sqlite2.Interface.IDataBase;
import alt.sqlite2.Interface.IEntity;
import alt.sqlite2.Interface.IEntityFieldManager;
import alt.sqlite2.Interface.IFieldManager;
import alt.sqlite2.Interface.ISQLiteEntityAdapter;

/**
 * Created by root on 7/8/15.
 */
public class SQLiteEntityAdapter<T> implements ISQLiteEntityAdapter<T> {

    protected T mObject;
    protected IDataBase mDataBaseModel;
    protected IEntity mEntity;
    public SQLiteEntityAdapter(IDataBase dataBaseModel,T object) {
        this.mDataBaseModel = dataBaseModel;
        this.mObject = object;
        this.mEntity = dataBaseModel.findEntityByName(object.getClass().getSimpleName());
    }

    @Override
    public T getEntity(Cursor cursor) {
        cursor.moveToFirst();
        String[] columnNames = cursor.getColumnNames();
        try {
            for(int i=0;i<columnNames.length;i++) {
                Field field = this.mObject.getClass().getDeclaredField(columnNames[i]);
                IEntityFieldManager fieldManager = new EntityFieldManager(mDataBaseModel,field);
                if(fieldManager.isEntity())
                    handleEntity(fieldManager,columnNames[i],cursor);
                if(fieldManager.isEntityArray())
                    handleEntityArray(fieldManager,columnNames[i],cursor);
                if (field.getType().equals(String.class))
                    handleStringField(fieldManager,columnNames[i],cursor);
                if (fieldManager.isPrimitive())
                    handlePrimitiveField(fieldManager,columnNames[i],cursor);
                if(fieldManager.isArray())
                    handleArrayField(fieldManager,columnNames[i],cursor);
                if(fieldManager.isAssignableFromListInterface())
                    handleListField(fieldManager,columnNames[i],cursor);
            }
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return this.mObject;
    }

    void handleEntity(IEntityFieldManager entityFieldManager,String columnName,Cursor cursor) throws IllegalAccessException {
        entityFieldManager.setValue(mEntity,mObject,cursor.getLong(cursor.getColumnIndex(columnName)));
    }
    void handleEntityArray(IEntityFieldManager entityFieldManager,String columnName,Cursor cursor) throws IllegalAccessException {
        entityFieldManager.setValue(mEntity,mObject,cursor.getBlob(cursor.getColumnIndex(columnName)));
    }
    void handleEntityList(IEntityFieldManager entityFieldManager,String columnName,Cursor cursor) throws IllegalAccessException {
        entityFieldManager.setValue(mEntity,mObject, cursor.getBlob(cursor.getColumnIndex(columnName)));
    }
    void handlePrimitiveField(IFieldManager fieldManager,String columnName,Cursor cursor) throws IllegalAccessException{
        if (fieldManager.getType().equals(Integer.TYPE) || fieldManager.getType().equals(Long.TYPE))
            fieldManager.setValue(mObject, cursor.getInt(cursor.getColumnIndex(columnName)));
        if (fieldManager.getType().equals(Float.TYPE) || fieldManager.getType().equals(Double.TYPE))
            fieldManager.setValue(mObject,cursor.getFloat(cursor.getColumnIndex(columnName)));
    }
    void handleStringField(IFieldManager fieldManager,String columnName,Cursor cursor) throws IllegalAccessException{
        fieldManager.setValue(mObject,cursor.getString(cursor.getColumnIndex(columnName)));
    }
    void handleArrayField(IFieldManager fieldManager,String columnName,Cursor cursor) throws IllegalAccessException {
        fieldManager.setValue(mObject,cursor.getBlob(cursor.getColumnIndex(columnName)));
    }

    void handleListField(IFieldManager fieldManager,String columnName,Cursor cursor) throws IllegalAccessException{
        fieldManager.setValue(mObject,cursor.getBlob(cursor.getColumnIndex(columnName)));
    }


}
