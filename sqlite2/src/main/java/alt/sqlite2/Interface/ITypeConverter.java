package alt.sqlite2.Interface;

/**
 * Created by lawrence on 7/19/15.
 */
public interface ITypeConverter {
    byte[] convertToByteArray(short[] array);
    byte[] convertToByteArray(int[] array);
    byte[] convertToByteArray(long[] array);
    byte[] convertToByteArray(float[] array);
    byte[] convertToByteArray(double[] array);
    short[] convertToShortArray(byte[] array);
    int[] convertToIntArray(byte[] array);
    long[] convertToLongArray(byte[] array);
    float[] convertToFloatArray(byte[] array);
    double[] convertToDoubleArray(byte[] array);

}
