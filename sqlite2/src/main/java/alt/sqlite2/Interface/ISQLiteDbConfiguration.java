package alt.sqlite2.Interface;

import android.util.Pair;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Set;

/**
 * Created by root on 15年3月27日.
 */
public interface ISQLiteDbConfiguration {
    void setDatabaseVersion(int dbVersion);
    void setDataBaseName(String dbName);
    void setEntityScanFolder(File folder);
    int getDatabaseVersion();
    String getDataBaseName();
    File getEntityScanFolder();
    void scanEntityFolder(File folder);
    Set<Class> getClassSet();
    String[] getDataDescriptionSQL();
    boolean constructDataBaseModel();
    void setDataBaseModel(IDataBase dataBase);
    IDataBase getDataBaseModel();
}
