package alt.sqlite2;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import alt.sqlite2.Interface.IEntityCheck;
import alt.sqlite2.Interface.IFieldManager;
import alt.sqlite2.Interface.IFileManager;

/**
 * Created by root on 7/13/15.
 */
public class FileManager implements IFileManager{

    @Override
    public List<String> scanJarFileForClass(File file) throws IOException {

        if (file == null || !file.exists())
            throw new IllegalArgumentException("Invalid jar-file to scan provided");
        if (file.getName().endsWith(".jar"))
        {
            List<String> foundClasses = new ArrayList<String>();
            JarFile jarFile = new JarFile(file);

                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements())
                {
                    JarEntry entry = entries.nextElement();
                    if (entry.getName().endsWith(".class"))
                    {
                        String name = entry.getName();
                        name = name.substring(0,name.lastIndexOf(".class"));
                        if (name.indexOf("/")!= -1)
                            name = name.replaceAll("/", ".");
                       // if (name.indexOf("\\")!= -1)
                           // name = name.replaceAll("\\", ".");
                        foundClasses.add(name);
                    }
                }

            return foundClasses;
        }else if(file.isDirectory()){
            List<String> foundClasses = new ArrayList<>();
            File files[] = file.listFiles();
            for(int i=0;i<files.length;i++)
            if(files[i].getName().endsWith(".class")){
                foundClasses.add(files[i].getName());
            }
            return foundClasses;
        }else if(file.isFile() && file.getName().endsWith(".class")){

        }
        throw new IllegalArgumentException("No jar-file provided");
    }

    @Override
    public List<Class<?>> findClassesInJarFile(File file, ClassLoader loader) throws IOException {
        List<Class<?>> implementingClasses = new ArrayList<Class<?>>();
        // scan the jar file for all included classes
        for (String classFile : scanJarFileForClass(file))
        {
            Class<?> clazz;
            try
            {
                // now try to load the class
                if (loader == null)
                    clazz = Class.forName(classFile);
                else
                    clazz = Class.forName(classFile, true, loader);

                // and check if the class implements the provided interface
                //if (iface.isAssignableFrom(clazz) && !clazz.equals(iface))
                    implementingClasses.add(clazz);
            }
            catch (ClassNotFoundException e)
            {
                e.printStackTrace();
            }
        }
        return implementingClasses;
    }

}
