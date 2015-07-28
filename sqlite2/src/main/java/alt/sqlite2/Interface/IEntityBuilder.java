package alt.sqlite2.Interface;

import java.io.File;

/**
 * Created by root on 7/11/15.
 */
public interface IEntityBuilder {
    void createEntity(File file);
    void createEntity(ISQLiteConnection sqliteConnection);
}
