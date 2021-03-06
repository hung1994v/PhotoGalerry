package bsoft.com.lib_filter.filter.gpu.father;

import android.graphics.PointF;
import android.opengl.GLES20;
import com.google.android.gms.cast.TextTrackStyle;
import java.nio.FloatBuffer;
import java.util.LinkedList;

import bsoft.com.lib_filter.filter.gpu.util.OpenGlUtils;


public class GPUImageFilter {
    public static final String NO_FILTER_FRAGMENT_SHADER = "varying highp vec2 textureCoordinate;\n \nuniform sampler2D inputImageTexture;\n \nvoid main()\n{\n     gl_FragColor = texture2D(inputImageTexture, textureCoordinate);\n}";
    public static final String NO_FILTER_VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\n \nuniform mat4 transformMatrix;\n\nvarying vec2 textureCoordinate;\n \nvoid main()\n{\n    gl_Position = transformMatrix * vec4(position.xyz, 1.0);\n    textureCoordinate = inputTextureCoordinate.xy;\n}";
    private final String mFragmentShader;
    protected int mGLAttribPosition;
    protected int mGLAttribTextureCoordinate;
    protected int mGLProgId;
    protected int mGLUniformTexture;
    private boolean mIsInitialized;
    protected float mMix;
    protected int mMixLocation;
    public int mOutputHeight;
    public int mOutputWidth;
    private final LinkedList<Runnable> mRunOnDraw;
    protected float[] mTransform;
    protected int mTransformLocation;
    private final String mVertexShader;

    public GPUImageFilter() {
        this(NO_FILTER_VERTEX_SHADER, NO_FILTER_FRAGMENT_SHADER);
    }

    public GPUImageFilter(String vertexShader, String fragmentShader) {
        this.mMix = TextTrackStyle.DEFAULT_FONT_SCALE;
        this.mTransform = new float[]{TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, 0.0f, 0.0f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, 0.0f, 0.0f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE, 0.0f, 0.0f, 0.0f, 0.0f, TextTrackStyle.DEFAULT_FONT_SCALE};
        this.mRunOnDraw = new LinkedList();
        this.mVertexShader = vertexShader;
        this.mFragmentShader = fragmentShader;
    }

    public final void init() {
        onInit();
        onInitialized();
    }

    public void onInit() {
        this.mGLProgId = OpenGlUtils.loadProgram(this.mVertexShader, this.mFragmentShader);
        this.mGLAttribPosition = GLES20.glGetAttribLocation(this.mGLProgId, "position");
        this.mGLUniformTexture = GLES20.glGetUniformLocation(this.mGLProgId, "inputImageTexture");
        this.mGLAttribTextureCoordinate = GLES20.glGetAttribLocation(this.mGLProgId, "inputTextureCoordinate");
        this.mMixLocation = GLES20.glGetUniformLocation(getProgram(), "mixturePercent");
        this.mTransformLocation = GLES20.glGetUniformLocation(getProgram(), "transformMatrix");
        this.mIsInitialized = true;
    }

    public void onInitialized() {
        setMix(this.mMix);
        setTransform(this.mTransform);
    }

    public final void destroy() {
        this.mIsInitialized = false;
        GLES20.glDeleteProgram(this.mGLProgId);
        onDestroy();
    }

    public void onDestroy() {
    }

    public void setMix(float mix) {
        this.mMix = mix;
        setFloat(this.mMixLocation, this.mMix);
    }

    public void setTransform(float[] matrix) {
        this.mTransform = matrix;
        setUniformMatrix4f(this.mTransformLocation, matrix);
    }

    public void onOutputSizeChanged(int width, int height) {
        this.mOutputWidth = width;
        this.mOutputHeight = height;
    }

    public void onDraw(int textureId, FloatBuffer cubeBuffer, FloatBuffer textureBuffer) {
        GLES20.glUseProgram(this.mGLProgId);
        runPendingOnDrawTasks();
        if (this.mIsInitialized) {
            cubeBuffer.position(0);
            GLES20.glVertexAttribPointer(this.mGLAttribPosition, 2, 5126, false, 0, cubeBuffer);
            GLES20.glEnableVertexAttribArray(this.mGLAttribPosition);
            textureBuffer.position(0);
            GLES20.glVertexAttribPointer(this.mGLAttribTextureCoordinate, 2, 5126, false, 0, textureBuffer);
            GLES20.glEnableVertexAttribArray(this.mGLAttribTextureCoordinate);
            if (textureId != -1) {
                GLES20.glActiveTexture(33984);
                GLES20.glBindTexture(3553, textureId);
                GLES20.glUniform1i(this.mGLUniformTexture, 0);
            }
            onDrawArraysPre();
            GLES20.glDrawArrays(5, 0, 4);
            GLES20.glDisableVertexAttribArray(this.mGLAttribPosition);
            GLES20.glDisableVertexAttribArray(this.mGLAttribTextureCoordinate);
            GLES20.glBindTexture(3553, 0);
        }
    }

    protected void onDrawArraysPre() {
    }

    protected void runPendingOnDrawTasks() {
        synchronized (this.mRunOnDraw) {
            while (!this.mRunOnDraw.isEmpty()) {
                ((Runnable) this.mRunOnDraw.removeFirst()).run();
            }
        }
    }

    public boolean isInitialized() {
        return this.mIsInitialized;
    }

    public int getOutputWidth() {
        return this.mOutputWidth;
    }

    public int getOutputHeight() {
        return this.mOutputHeight;
    }

    public int getProgram() {
        return this.mGLProgId;
    }

    public int getAttribPosition() {
        return this.mGLAttribPosition;
    }

    public int getAttribTextureCoordinate() {
        return this.mGLAttribTextureCoordinate;
    }

    public int getUniformTexture() {
        return this.mGLUniformTexture;
    }

    protected void setInteger(final int location, final int intValue) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform1i(location, intValue);
            }
        });
    }

    protected void setFloat(final int location, final float floatValue) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform1f(location, floatValue);
            }
        });
    }

    protected void setFloatVec2(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform2fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatVec3(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform3fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatVec4(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform4fv(location, 1, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setFloatArray(final int location, final float[] arrayValue) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform1fv(location, arrayValue.length, FloatBuffer.wrap(arrayValue));
            }
        });
    }

    protected void setPoint(final int location, final PointF point) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniform2fv(location, 1, new float[]{point.x, point.y}, 0);
            }
        });
    }

    protected void setUniformMatrix3f(final int location, final float[] matrix) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniformMatrix3fv(location, 1, false, matrix, 0);
            }
        });
    }

    protected void setUniformMatrix4f(final int location, final float[] matrix) {
        runOnDraw(new Runnable() {
            public void run() {
                GLES20.glUniformMatrix4fv(location, 1, false, matrix, 0);
            }
        });
    }

    protected void runOnDraw(Runnable runnable) {
        synchronized (this.mRunOnDraw) {
            this.mRunOnDraw.addLast(runnable);
        }
    }


}
