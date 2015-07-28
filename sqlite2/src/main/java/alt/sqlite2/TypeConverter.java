package alt.sqlite2;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

import alt.sqlite2.Interface.ITypeConverter;

/**
 * Created by lawrence on 7/20/15.
 */
public class TypeConverter implements ITypeConverter {
    @Override
    public byte[] convertToByteArray(short[] array) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(array.length * 2);
        ShortBuffer shortBuffer = byteBuffer.asShortBuffer();
        shortBuffer.put(array);
        return byteBuffer.array();
    }

    @Override
    public byte[] convertToByteArray(int[] array) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(array.length * 4);
        IntBuffer intBuffer = byteBuffer.asIntBuffer();
        intBuffer.put(array);
        return byteBuffer.array();
    }

    @Override
    public byte[] convertToByteArray(long[] array) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(array.length * 8);
        LongBuffer longBuffer = byteBuffer.asLongBuffer();
        longBuffer.put(array);
        return byteBuffer.array();
    }

    @Override
    public byte[] convertToByteArray(float[] array) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(array.length * 4);
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        floatBuffer.put(array);
        return byteBuffer.array();
    }

    @Override
    public byte[] convertToByteArray(double[] array) {
       ByteBuffer byteBuffer = ByteBuffer.allocate(array.length*8);
       DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
       doubleBuffer.put(array);
       return byteBuffer.array();
    }

    @Override
    public short[] convertToShortArray(byte[] array) {
        ByteBuffer bb = ByteBuffer.wrap(array);
        ShortBuffer shortBuffer = bb.asShortBuffer();
        short[] s = new short[shortBuffer.capacity()];
        shortBuffer.get(s);
        return s;
    }

    @Override
    public int[] convertToIntArray(byte[] array) {
       ByteBuffer byteBuffer = ByteBuffer.wrap(array);
       IntBuffer intBuffer = byteBuffer.asIntBuffer();
       int i[] = new int[intBuffer.capacity()];
       intBuffer.get(i);
       return i;
    }

    @Override
    public long[] convertToLongArray(byte[] array) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(array);
        LongBuffer longBuffer = byteBuffer.asLongBuffer();
        long l[] = new long[longBuffer.capacity()];
        longBuffer.get(l);
        return l;
    }

    @Override
    public float[] convertToFloatArray(byte[] array) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(array);
        FloatBuffer floatBuffer = byteBuffer.asFloatBuffer();
        float f[] = new float[floatBuffer.capacity()];
        floatBuffer.get(f);
        return f;
    }

    @Override
    public double[] convertToDoubleArray(byte[] array) {
         ByteBuffer byteBuffer = ByteBuffer.wrap(array);
         DoubleBuffer doubleBuffer = byteBuffer.asDoubleBuffer();
        double[] d = new double[doubleBuffer.capacity()];
        doubleBuffer.get(d);
        return d;
    }
}
