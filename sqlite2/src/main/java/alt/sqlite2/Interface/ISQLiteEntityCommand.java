package alt.sqlite2.Interface;

import java.util.List;

/**
 * Created by root on 15年3月27日.
 */
public interface ISQLiteEntityCommand<T>{
    T insertEntity(T t);
    long updateEntity(T t);
    boolean deleteEntity(long id);
    T readEntity(long id);
    List<T> readAllEntity();
    int count();
}
