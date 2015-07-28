package alt.sqlite2.Interface;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by root on 7/10/15.
 */
public interface IClassManager {
    Method findMethodAnnotatedWith(final Class cx,final Class<? extends Annotation> annotation);
    Field findFieldAnnotatedWith(final Class cx,final Class<? extends Annotation> annotation);
    boolean isFieldNameExists(final Class cx,final String name);
    boolean isFieldExist(final Class cx,final Class fieldType);
    boolean isGenericTypeFieldExists(final Class cx,final Class collectionFieldType,final Class genericType);
    boolean isArrayFieldExists(Class cx,Class array);
    Type findTypeArgumentInGenericTypeField(final Field field);
    Class getTypeArgumentInGenericTypeFieldInClass(Field field);
    List<Field> findFieldExists(final Class cx,final Class fieldType);
}
