package alt.sqlite2;

import alt.sqlite2.Interface.IEntity;
import alt.sqlite2.Interface.IJoinTable;

/**
 * Created by lawrence on 7/15/15.
 */
public class JoinTableModel extends Entity implements IJoinTable{
    protected IEntity mEntity1;
    protected IEntity mEntity2;
    public JoinTableModel(IEntity entity1, IEntity entity2) {
        super(new Integer(Math.abs(entity1.getEntityName().hashCode()+entity2.getEntityName().hashCode())).toString());
        this.mEntity1 = entity1;
        this.mEntity2 = entity2;
    }

    @Override
    public IEntity getEntity1() {
        return this.mEntity1;
    }

    @Override
    public void setEntity1(IEntity entity1) {
        this.mEntity1 = entity1;
    }

    @Override
    public IEntity getEntity2() {
        return this.mEntity2;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof JoinTableModel)) return false;

        JoinTableModel joinTableModel = (JoinTableModel) o;

        if (!(mEntity1.equals(joinTableModel.mEntity1)||mEntity1.equals(joinTableModel.mEntity2))) return false;
        if (!(mEntity2.equals(joinTableModel.mEntity2)||mEntity2.equals(joinTableModel.mEntity1))) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = mEntity1.hashCode();
        result = 31 * result + mEntity2.hashCode();
        return result;
    }

    @Override
    public void setEntity2(IEntity entity2) {
        this.mEntity2 = entity2;

    }
}
