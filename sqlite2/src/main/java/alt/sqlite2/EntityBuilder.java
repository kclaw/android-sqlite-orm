package alt.sqlite2;

import android.database.Cursor;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import alt.sqlite2.Interface.IEntity;
import alt.sqlite2.Interface.IEntityBuilder;
import alt.sqlite2.Interface.IEntityOperation;
import alt.sqlite2.Interface.IRelationship;
import alt.sqlite2.Interface.ISQLiteConnection;

/**
 * Created by root on 7/12/15.
 */
public class EntityBuilder implements IEntityBuilder{

    private ISQLiteConnection mSqlLiteConnection;
    //private List<IEntity> mEntitieList;

    @Override
    public void createEntity(File file) {

    }

    @Override
    public void createEntity(ISQLiteConnection sqliteConnection) {
        Cursor cursor = null;
        if(!sqliteConnection.isOpen()) {
            cursor = sqliteConnection.getOpenedDatabase().rawQuery("Select name from sqlite_master where type='table';", null);
            while (cursor.moveToNext()) {
               IEntity entity;
               /*mEntitieList.add(entity = new Entity(cursor.getString(0)));
               List<IEntity> connectingEntityList = findAllConnectingEntity(entity);
               for(int i=0;i<connectingEntityList.size();i++)
                    entity.addConnectingEntity(connectingEntityList.get(0));
                */
            }
        }
    }

}
