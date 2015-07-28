package alt.sqlite2.Interface;

import java.lang.reflect.Type;

/**
 * Created by root on 15年3月29日.
 */
public interface IFieldManager extends IPrimitiveDataTypeMapper,IStringClassMapper,IArrayClassMapper,IListMapper,ISetMapper{
    String getDataType();
    Class getType();
    <T> T getValue(Object object, Class<T> tClass) throws IllegalAccessException;
    void setValue(Object object, Object value) throws IllegalAccessException;
    boolean isAssignableFromListInterface();
    boolean isAssignableFromSetInterface();
    Type getTypeInGenericType();
    Class getClassInGenericType();
    boolean isPrimitive();
    boolean isArray();
}
