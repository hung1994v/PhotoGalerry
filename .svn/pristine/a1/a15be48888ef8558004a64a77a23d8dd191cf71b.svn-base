package bsoft.com.lib_filter.filter;

import android.content.Context;

public class GPUImageNativeLibrary {
    public static native void YUVtoARBG(byte[] bArr, int i, int i2, int[] iArr);

    public static native void YUVtoRBGA(byte[] bArr, int i, int i2, int[] iArr);

    public static native boolean initGpuNativeLibrary(Context context);


    static {
        System.loadLibrary("gpuimage-library");
    }
}
