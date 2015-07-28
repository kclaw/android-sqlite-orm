package alt.sqlite2;
/**
 * Created by root on 7/13/15.
 */

import alt.sqlite2.Annotation.EntityId;

import alt.sqlite2.Annotation.Entity;
@Entity
public class Test3 {
    @EntityId(isAutoIncrement = false)
    public long id;
    public Test2 test2;
}
