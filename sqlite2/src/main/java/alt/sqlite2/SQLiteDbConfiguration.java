package alt.sqlite2;

import android.content.Context;
import android.os.Environment;
import android.util.Pair;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import alt.sqlite2.Annotation.Entity;
import alt.sqlite2.Annotation.EntityId;
import alt.sqlite2.Interface.IDataBase;
import alt.sqlite2.Interface.IEntityCheck;
import alt.sqlite2.Interface.IFieldManager;
import alt.sqlite2.Interface.ISQLiteDbConfiguration;
import alt.sqlite2.Interface.ISQLiteState;

/**
 * Created by root on 7/8/15.
 */
public final class SQLiteDbConfiguration implements ISQLiteDbConfiguration {
    private int mDbVersion = 1;
    private String mDbName = "Test";
    private File mEntityScanFolder;
    private Context mContext;
    private IDataBase mDataBase;

    public SQLiteDbConfiguration(Context context){
       this.mContext = context;
       this.constructDataBaseModel();
    }
    @Override
    public void setDatabaseVersion(int dbVersion) {
        this.mDbVersion = dbVersion;
    }

    @Override
    public void setDataBaseName(String dbName) {
        this.mDbName = dbName;
    }

    @Override
    public void setEntityScanFolder(File folder) {
        this.mEntityScanFolder = folder;
    }

    @Override
    public int getDatabaseVersion() {
        return mDbVersion;
    }

    @Override
    public String getDataBaseName() {
        return mDbName;
    }

    @Override
    public File getEntityScanFolder() {
        return this.mEntityScanFolder;
    }


    @Override
    public String[] getDataDescriptionSQL() {
        SQLiteDataBaseCreator dbc = new SQLiteDataBaseCreator(mDataBase);
        String sql = dbc.generateDBSchema();
        return sql.split("(?<=;)");
    }

    @Override
    public boolean constructDataBaseModel() {
        mDataBase = new DataBase();
        //try {
            /*String[] paths = mContext.getAssets().list("entity");
            File[] files = new File[paths.length];
            for(int i=0;i<paths.length;i++) {
                InputStream inputStream = mContext.getAssets().open("entity/" + paths[i]);
                byte[] buffer = new byte[inputStream.available()];
                inputStream.read(buffer);

                File targetFile = new File(mContext.getFilesDir(), paths[i]);
                targetFile.createNewFile();
                OutputStream outStream = new FileOutputStream(targetFile);
                outStream.write(buffer);
                files[i] = targetFile;
            }*/
            //mDataBase.createEntities(files);
           // mDataBase.createEntities(new File("/src/main/assets/entity/entity.jar"));
            mDataBase.createEntities(mContext);
            return  true;
        /*} catch (IOException e) {
            e.printStackTrace();
        }*/
        //return false;
    }

    @Override
    public void setDataBaseModel(IDataBase dataBase) {
        this.mDataBase = dataBase;
    }

    @Override
    public IDataBase getDataBaseModel() {
        return this.mDataBase;
    }

    @Override
    public void scanEntityFolder(File folder) {
        IEntityCheck entityCheck = new EntityChecker();
        for(File file:folder.listFiles()){
            if(entityCheck.checkForValidEntity(file.getClass()) && entityCheck.checkForValidEntityId(file.getClass())){
               // mClassHashSet.add(file.getClass());
            }
        }
    }


    @Override
    public Set<Class> getClassSet() {
        //return this.mClassHashSet;
        return null;
    }


}
