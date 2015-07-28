import java.util.List;

import alt.sqlite2.Annotation.Entity;
import alt.sqlite2.Annotation.EntityId;

/**
 * Created by root on 7/13/15.
 */
@Entity
public class Test1 {
    @EntityId(isAutoIncrement = true)
    public long id;
    public List<Test2> mtest2list;
}
