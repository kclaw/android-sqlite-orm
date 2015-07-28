package alt.sqlite2;

import alt.sqlite2.Annotation.EntityId;
import alt.sqlite2.Annotation.Entity;
/**
 * Created by lawrence on 7/21/15.
 */
@Entity
public class JoinTable {
    public JoinTable(long id,long entity1Id,long entity2Id,String name){
        this.id = id;
        this.entity1Id = entity1Id;
        this.entity2Id = entity2Id;
        this.name = name;
    }
    @EntityId(isAutoIncrement = true)
    public long id;
    public long entity1Id;
    public long entity2Id;
    public String name;
    public String entity1name;
    public String entity2name;

    public String getEntity1name() {
        return entity1name;
    }

    public void setEntity1name(String entity1name) {
        this.entity1name = entity1name;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getEntity1Id() {
        return entity1Id;
    }

    public void setEntity1Id(long entity1Id) {
        this.entity1Id = entity1Id;
    }

    public long getEntity2Id() {
        return entity2Id;
    }

    public void setEntity2Id(long entity2Id) {
        this.entity2Id = entity2Id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEntity2name() {
        return entity2name;
    }

    public void setEntity2name(String entity2name) {
        this.entity2name = entity2name;
    }
}
