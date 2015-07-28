package alt.sqlite2.Interface;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by root on 7/13/15.
 */
public interface IFileManager {
    List<String> scanJarFileForClass(File file) throws IOException;
    List<Class<?>> findClassesInJarFile(File file, ClassLoader loader) throws IOException;
}
