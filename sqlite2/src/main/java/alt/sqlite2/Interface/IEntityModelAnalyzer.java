package alt.sqlite2.Interface;

/**
 * Created by lawrence on 7/14/15.
 */
public interface IEntityModelAnalyzer {
    void analyzeEntity(IEntity entity);
    void setEntity(IEntity entity);
    IEntity getEntity();
}
