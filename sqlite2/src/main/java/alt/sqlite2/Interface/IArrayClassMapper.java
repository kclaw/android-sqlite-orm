package alt.sqlite2.Interface;

/**
 * Created by root on 7/8/15.
 */
public interface IArrayClassMapper {
    String mapToByteArray();
    String mapToShortArray();
    String mapToIntArray();
    String mapToLongArray();
    String mapToFloatArray();
    String mapToDoubleArray();
    String mapToBooleanArray();
    String mapToCharArray();
    Class mapFromByteArray();
    Class mapFromShortArray();
    Class mapFromIntArray();
    Class mapFromLongArray();
    Class mapFromFloatArray();
    Class mapFromDoubleArray();
    Class mapFromBooleanArray();
    Class mapFromCharArray();
}
