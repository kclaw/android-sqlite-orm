package alt.sqlite2;

import java.util.Map;

/**
 * Created by lawrence on 7/26/15.
 */
public class EntityResult {
    SQLiteOperationType operationType;
    long id;
    Map<String,long[]> innerEntityMap;

    public EntityResult(SQLiteOperationType operationType,long id,Map<String,long[]> innerEntityMap){
        this.operationType = operationType;
        this.id = id;
        this.innerEntityMap = innerEntityMap;
    }

    public SQLiteOperationType getOperationType() {
        return operationType;
    }

    public void setOperationType(SQLiteOperationType operationType) {
        this.operationType = operationType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Map<String, long[]> getInnerEntityMap() {
        return innerEntityMap;
    }

    public void setInnerEntityMap(Map<String, long[]> innerEntityMap) {
        this.innerEntityMap = innerEntityMap;
    }
}
