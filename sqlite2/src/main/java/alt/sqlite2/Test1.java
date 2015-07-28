package alt.sqlite2;
import java.util.List;

import alt.sqlite2.Annotation.EntityId;

import alt.sqlite2.Annotation.Entity;


/**
 * Created by root on 7/13/15.
 */
@Entity
public class Test1 {
    @EntityId(isAutoIncrement = true)
    public long id;
    public List<Test2> mtest2list;
}
