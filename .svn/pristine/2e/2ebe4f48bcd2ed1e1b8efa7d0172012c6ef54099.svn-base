package bsoft.com.lib_filter.filter.gpu.normal;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PointF;
import android.opengl.GLES20;
import com.google.android.gms.cast.TextTrackStyle;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

import bsoft.com.lib_filter.filter.gpu.father.GPUImageFilter;


public class GPUImageTestFilter extends GPUImageFilter {
    public static final String TONE_CURVE_FRAGMENT_SHADER = " varying highp vec2 textureCoordinate;\n uniform sampler2D inputImageTexture;\n uniform sampler2D toneCurveTexture;\n uniform lowp float mixturePercent;\n const highp vec4 kRGBToYPrime = vec4 (0.299, 0.587, 0.114, 0.0);\n void main()\n {\n     lowp vec4 textureColor = texture2D(inputImageTexture, textureCoordinate);\n     lowp float redCurveValue = texture2D(toneCurveTexture, vec2(textureColor.r, 0.0)).r;\n     lowp float greenCurveValue = texture2D(toneCurveTexture, vec2(textureColor.g, 0.0)).g;\n     lowp float blueCurveValue = texture2D(toneCurveTexture, vec2(textureColor.b, 0.0)).b;\n     lowp vec4 textureColor2 = vec4(redCurveValue,greenCurveValue,blueCurveValue,textureColor.a);\n      lowp float YPrime = dot(textureColor, kRGBToYPrime);      if(YPrime < 0.5 && YPrime > 0.2){ textureColor2 = textureColor;}\n   gl_FragColor = vec4(mix(textureColor.rgb, textureColor2.rgb, textureColor2.a*mixturePercent), textureColor.a);\n }";
    private String fileType = "acv";
    private PointF[] mBlueControlPoints;
    private ArrayList<Float> mBlueCurve;
    private PointF[] mGreenControlPoints;
    private ArrayList<Float> mGreenCurve;
    private float mMix;
    private int mMixLocation;
    private PointF[] mRedControlPoints;
    private ArrayList<Float> mRedCurve;
    private PointF[] mRgbCompositeControlPoints;
    private ArrayList<Float> mRgbCompositeCurve;
    private int[] mToneCurveTexture = new int[]{-1};
    private int mToneCurveTextureUniformLocation;
    private double k;
    private double[] dArr;

    class C02001 implements Runnable {
        C02001() {
        }

        public void run() {
            GLES20.glActiveTexture(33987);
            GLES20.glBindTexture(3553, GPUImageTestFilter.this.mToneCurveTexture[0]);
            if (GPUImageTestFilter.this.mRedCurve.size() >= 256 && GPUImageTestFilter.this.mGreenCurve.size() >= 256 && GPUImageTestFilter.this.mBlueCurve.size() >= 256 && GPUImageTestFilter.this.mRgbCompositeCurve.size() >= 256) {
                byte[] toneCurveByteArray = new byte[1024];
                for (int currentCurveIndex = 0; currentCurveIndex < 256; currentCurveIndex++) {
                    toneCurveByteArray[currentCurveIndex * 4] = (byte) (((int) Math.min(Math.max(((Float) GPUImageTestFilter.this.mRgbCompositeCurve.get(currentCurveIndex)).floatValue() + (((float) currentCurveIndex) + ((Float) GPUImageTestFilter.this.mRedCurve.get(currentCurveIndex)).floatValue()), 0.0f), 255.0f)) & 255);
                    toneCurveByteArray[(currentCurveIndex * 4) + 1] = (byte) (((int) Math.min(Math.max(((Float) GPUImageTestFilter.this.mRgbCompositeCurve.get(currentCurveIndex)).floatValue() + (((float) currentCurveIndex) + ((Float) GPUImageTestFilter.this.mGreenCurve.get(currentCurveIndex)).floatValue()), 0.0f), 255.0f)) & 255);
                    toneCurveByteArray[(currentCurveIndex * 4) + 2] = (byte) (((int) Math.min(Math.max(((Float) GPUImageTestFilter.this.mRgbCompositeCurve.get(currentCurveIndex)).floatValue() + (((float) currentCurveIndex) + ((Float) GPUImageTestFilter.this.mBlueCurve.get(currentCurveIndex)).floatValue()), 0.0f), 255.0f)) & 255);
                    toneCurveByteArray[(currentCurveIndex * 4) + 3] = (byte) -1;
                }
                GLES20.glTexImage2D(3553, 0, 6408, 256, 1, 0, 6408, 5121, ByteBuffer.wrap(toneCurveByteArray));
            }
        }
    }

    class C02012 implements Comparator<PointF> {
        C02012() {
        }

        public int compare(PointF point1, PointF point2) {
            if (point1.x < point2.x) {
                return -1;
            }
            if (point1.x > point2.x) {
                return 1;
            }
            return 0;
        }
    }

    public GPUImageTestFilter() {
        super(GPUImageFilter.NO_FILTER_VERTEX_SHADER, TONE_CURVE_FRAGMENT_SHADER);
        PointF[] defaultCurvePoints = new PointF[]{new PointF(0.0f, 0.0f), new PointF(0.5f, 0.5f), new PointF(TextTrackStyle.DEFAULT_FONT_SCALE, TextTrackStyle.DEFAULT_FONT_SCALE)};
        this.mRgbCompositeControlPoints = defaultCurvePoints;
        this.mRedControlPoints = defaultCurvePoints;
        this.mGreenControlPoints = defaultCurvePoints;
        this.mBlueControlPoints = defaultCurvePoints;
        this.mMix = TextTrackStyle.DEFAULT_FONT_SCALE;
    }

    public void setFileType(String type) {
        this.fileType = type;
    }

    public void onInit() {
        super.onInit();
        this.mMixLocation = GLES20.glGetUniformLocation(getProgram(), "mixturePercent");
        this.mToneCurveTextureUniformLocation = GLES20.glGetUniformLocation(getProgram(), "toneCurveTexture");
        GLES20.glActiveTexture(33987);
        GLES20.glGenTextures(1, this.mToneCurveTexture, 0);
        GLES20.glBindTexture(3553, this.mToneCurveTexture[0]);
        GLES20.glTexParameteri(3553, 10241, 9729);
        GLES20.glTexParameteri(3553, 10240, 9729);
        GLES20.glTexParameteri(3553, 10242, 33071);
        GLES20.glTexParameteri(3553, 10243, 33071);
    }

    public void onInitialized() {
        super.onInitialized();
        if (this.fileType == "acv") {
            this.mRgbCompositeCurve = createSplineCurve(this.mRgbCompositeControlPoints);
            this.mRedCurve = createSplineCurve(this.mRedControlPoints);
            this.mGreenCurve = createSplineCurve(this.mGreenControlPoints);
            this.mBlueCurve = createSplineCurve(this.mBlueControlPoints);
        }
        setMix(this.mMix);
        updateToneCurveTexture();
    }

    public void setMix(float mix) {
        this.mMix = mix;
        setFloat(this.mMixLocation, this.mMix);
    }

    protected void onDrawArraysPre() {
        if (this.mToneCurveTexture[0] != -1) {
            GLES20.glActiveTexture(33987);
            GLES20.glBindTexture(3553, this.mToneCurveTexture[0]);
            GLES20.glUniform1i(this.mToneCurveTextureUniformLocation, 3);
        }
    }

    public void setFromAcvCurveFileInputStream(InputStream input) {
        try {
            readShort(input);
            int totalCurves = readShort(input);
            ArrayList<PointF[]> curves = new ArrayList(totalCurves);
            for (int i = 0; i < totalCurves; i++) {
                short pointCount = readShort(input);
                PointF[] points = new PointF[pointCount];
                for (short j = (short) 0; j < pointCount; j++) {
                    points[j] = new PointF(((float) readShort(input)) * 0.003921569f, ((float) readShort(input)) * 0.003921569f);
                }
                curves.add(points);
            }
            input.close();
            this.mRgbCompositeControlPoints = (PointF[]) curves.get(0);
            this.mRedControlPoints = (PointF[]) curves.get(1);
            this.mGreenControlPoints = (PointF[]) curves.get(2);
            this.mBlueControlPoints = (PointF[]) curves.get(3);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private short readShort(InputStream input) throws IOException {
        return (short) ((input.read() << 8) | input.read());
    }

    public void setRgbCompositeControlPoints(PointF[] points) {
        this.mRgbCompositeControlPoints = points;
        this.mRgbCompositeCurve = createSplineCurve(this.mRgbCompositeControlPoints);
        updateToneCurveTexture();
    }

    public void setRedControlPoints(PointF[] points) {
        this.mRedControlPoints = points;
        this.mRedCurve = createSplineCurve(this.mRedControlPoints);
        updateToneCurveTexture();
    }

    public void setGreenControlPoints(PointF[] points) {
        this.mGreenControlPoints = points;
        this.mGreenCurve = createSplineCurve(this.mGreenControlPoints);
        updateToneCurveTexture();
    }

    public void setBlueControlPoints(PointF[] points) {
        this.mBlueControlPoints = points;
        this.mBlueCurve = createSplineCurve(this.mBlueControlPoints);
        updateToneCurveTexture();
    }

    private void updateToneCurveTexture() {
        runOnDraw(new C02001());
    }

    private ArrayList<Float> createSplineCurve(PointF[] points) {
        if (points == null || points.length <= 0) {
            return null;
        }
        int i;
        PointF[] pointsSorted = (PointF[]) points.clone();
        Arrays.sort(pointsSorted, new C02012());
        Point[] convertedPoints = new Point[pointsSorted.length];
        for (i = 0; i < points.length; i++) {
            PointF point = pointsSorted[i];
            convertedPoints[i] = new Point((int) (point.x * 255.0f), (int) (point.y * 255.0f));
        }
        ArrayList<Point> splinePoints = createSplineCurve2(convertedPoints);
        Point firstSplinePoint = (Point) splinePoints.get(0);
        if (firstSplinePoint.x > 0) {
            for (i = firstSplinePoint.x; i >= 0; i--) {
                splinePoints.add(0, new Point(i, 0));
            }
        }
        Point lastSplinePoint = (Point) splinePoints.get(splinePoints.size() - 1);
        if (lastSplinePoint.x < 255) {
            for (i = lastSplinePoint.x + 1; i <= 255; i++) {
                splinePoints.add(new Point(i, 255));
            }
        }
        ArrayList<Float> preparedSplinePoints = new ArrayList(splinePoints.size());
        Iterator it = splinePoints.iterator();
        while (it.hasNext()) {
            Point newPoint = (Point) it.next();
            Point origPoint = new Point(newPoint.x, newPoint.x);
            float distance = (float) Math.sqrt(Math.pow((double) (origPoint.x - newPoint.x), 2.0d) + Math.pow((double) (origPoint.y - newPoint.y), 2.0d));
            if (origPoint.y > newPoint.y) {
                distance = -distance;
            }
            preparedSplinePoints.add(Float.valueOf(distance));
        }
        return preparedSplinePoints;
    }

    private ArrayList<Point> createSplineCurve2(Point[] points) {
        ArrayList<Double> sdA = createSecondDerivative(points);
        int n = sdA.size();
        if (n < 1) {
            return null;
        }
        int i;
        double[] sd = new double[n];
        for (i = 0; i < n; i++) {
            sd[i] = ((Double) sdA.get(i)).doubleValue();
        }
        ArrayList<Point> output = new ArrayList(n + 1);
        for (i = 0; i < n - 1; i++) {
            Point cur = points[i];
            Point next = points[i + 1];
            for (int x = cur.x; x < next.x; x++) {
                double t = ((double) (x - cur.x)) / ((double) (next.x - cur.x));
                double a = 1.0d - t;
                double b = t;
                double h = (double) (next.x - cur.x);
                double y = ((((double) cur.y) * a) + (((double) next.y) * b)) + (((h * h) / 6.0d) * (((((a * a) * a) - a) * sd[i]) + ((((b * b) * b) - b) * sd[i + 1])));
                if (y > 255.0d) {
                    y = 255.0d;
                } else if (y < 0.0d) {
                    y = 0.0d;
                }
                output.add(new Point(x, (int) Math.round(y)));
            }
        }
        if (output.size() != 255) {
            return output;
        }
        output.add(points[points.length - 1]);
        return output;
    }

    private ArrayList<Double> createSecondDerivative(Point[] points) {
        int n = points.length;
        if (n <= 1) {
            return null;
        }
        int i;
        double[][] matrix = (double[][]) Array.newInstance(Double.TYPE, new int[]{n, 3});
        double[] result = new double[n];
        matrix[0][1] = 1.0d;
        matrix[0][0] = 0.0d;
        matrix[0][2] = 0.0d;
        for (i = 1; i < n - 1; i++) {
            Point P1 = points[i - 1];
            Point P2 = points[i];
            Point P3 = points[i + 1];
            matrix[i][0] = ((double) (P2.x - P1.x)) / 6.0d;
            matrix[i][1] = ((double) (P3.x - P1.x)) / 3.0d;
            matrix[i][2] = ((double) (P3.x - P2.x)) / 6.0d;
            result[i] = (((double) (P3.y - P2.y)) / ((double) (P3.x - P2.x))) - (((double) (P2.y - P1.y)) / ((double) (P2.x - P1.x)));
        }
        result[0] = 0.0d;
        result[n - 1] = 0.0d;
        matrix[n - 1][1] = 1.0d;
        matrix[n - 1][0] = 0.0d;
        matrix[n - 1][2] = 0.0d;
        for (i = 1; i < n; i++) {
            double k = matrix[i][0] / matrix[i - 1][1];
            double[] dArr = matrix[i];
            dArr[1] = dArr[1] - (matrix[i - 1][2] * k);
            matrix[i][0] = 0.0d;
            result[i] = result[i] - (result[i - 1] * k);
        }
        for (i = n - 2; i >= 0; i--) {
            k = matrix[i][2] / matrix[i + 1][1];
            dArr = matrix[i];
            dArr[1] = dArr[1] - (matrix[i + 1][0] * k);
            matrix[i][2] = 0.0d;
            result[i] = result[i] - (result[i + 1] * k);
        }
        ArrayList<Double> output = new ArrayList(n);
        for (i = 0; i < n; i++) {
            output.add(Double.valueOf(result[i] / matrix[i][1]));
        }
        return output;
    }

    public void setFromDatCurveFileInputStream(InputStream input) {
        int[] r = new int[256];
        int[] g = new int[256];
        int[] b = new int[256];
        int i = 0;
        int j = 0;
        int k = 0;
        try {
            DataInputStream localDataInputStream = new DataInputStream(new BufferedInputStream(input));
            do {
                r[i] = localDataInputStream.readByte();
                if (r[i] <= 0 && i > 0 && r[i - 1] > 0) {
                    r[i] = r[i] + 256;
                }
                i++;
            } while (i < 256);
            do {
                g[j] = localDataInputStream.readByte();
                if (g[j] <= 0 && j > 0 && g[j - 1] > 0) {
                    g[j] = g[j] + 256;
                }
                j++;
            } while (j < 256);
            do {
                b[k] = localDataInputStream.readByte();
                if (b[k] <= 0 && k > 0 && b[k - 1] > 0) {
                    b[k] = b[k] + 256;
                }
                k++;
            } while (k < 256);
            this.mRgbCompositeCurve = createSplineCurve(this.mRgbCompositeControlPoints);
            this.mRedCurve = createSplineCurve(r);
            this.mGreenCurve = createSplineCurve(g);
            this.mBlueCurve = createSplineCurve(b);
        } catch (Exception e) {
        }
    }

    public void setFromMapCurveFileBitmap(Bitmap map) {
        int[] rgbc = new int[256];
        int[] r = new int[256];
        int[] g = new int[256];
        int[] b = new int[256];
        try {
            int[] pixels = new int[768];
            map.getPixels(pixels, 0, 256, 0, 0, 256, 3);
            for (int i = 0; i < 3; i++) {
                for (int k = 0; k < 256; k++) {
                    int pixel = pixels[(i * 256) + k];
                    if (i == 0) {
                        r[k] = Color.red(pixel);
                    } else if (i == 1) {
                        g[k] = Color.green(pixel);
                    } else {
                        b[k] = Color.blue(pixel);
                    }
                }
            }
            if (3 == 4) {
                this.mRgbCompositeCurve = createSplineCurve(rgbc);
            } else {
                this.mRgbCompositeCurve = createSplineCurve(this.mRgbCompositeControlPoints);
            }
            this.mRedCurve = createSplineCurve(r);
            this.mGreenCurve = createSplineCurve(g);
            this.mBlueCurve = createSplineCurve(b);
        } catch (Exception e) {
        }
    }

    public void setFromMapCurveFileInputStream(InputStream input) {
        setFromMapCurveFileBitmap(BitmapFactory.decodeStream(input));
    }

    private ArrayList<Float> createSplineCurve(int[] color) {
        ArrayList<Float> preparedSplinePoints = new ArrayList(color.length);
        for (int i = 0; i < 256; i++) {
            Point origPoint = new Point(i, i);
            float distance = (float) Math.sqrt(Math.pow((double) (origPoint.x - i), 2.0d) + Math.pow((double) (origPoint.y - color[i]), 2.0d));
            if (origPoint.y > color[i]) {
                distance = -distance;
            }
            preparedSplinePoints.add(Float.valueOf(distance));
        }
        return preparedSplinePoints;
    }
}
