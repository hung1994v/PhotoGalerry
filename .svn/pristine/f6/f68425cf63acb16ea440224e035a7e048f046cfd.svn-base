package bsoft.com.lib_filter.filter.gpu.father;

import android.graphics.Bitmap;
import android.opengl.GLES20;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

import bsoft.com.lib_filter.filter.gpu.util.OpenGlUtils;
import bsoft.com.lib_filter.filter.gpu.util.Rotation;
import bsoft.com.lib_filter.filter.gpu.util.TextureRotationUtil;


public class GPUImageThreeInputFilter extends GPUImageFilter {
    private static final String VERTEX_SHADER = "attribute vec4 position;\nattribute vec4 inputTextureCoordinate;\nattribute vec4 inputTextureCoordinate2;\nattribute vec4 inputTextureCoordinate3;\n \nvarying vec2 textureCoordinate;\nvarying vec2 textureCoordinate2;\nvarying vec2 textureCoordinate3;\n \nvoid main()\n{\n    gl_Position = position;\n    textureCoordinate = inputTextureCoordinate.xy;\n    textureCoordinate2 = inputTextureCoordinate2.xy;\n    textureCoordinate3 = inputTextureCoordinate3.xy;\n}";
    public int filterInputTextureUniform2;
    public int filterInputTextureUniform3;
    public int filterSourceTexture2;
    public int filterSourceTexture3;
    public int filterTextureCoordinateAttribute2;
    public int filterTextureCoordinateAttribute3;
    private Bitmap mBitmap2;
    private Bitmap mBitmap3;
    private ByteBuffer mTextureCoordinatesBuffer2;
    private ByteBuffer mTextureCoordinatesBuffer3;

    public GPUImageThreeInputFilter(String fragmentShader) {
        this(VERTEX_SHADER, fragmentShader);
    }

    public GPUImageThreeInputFilter(String vertexShader, String fragmentShader) {
        super(vertexShader, fragmentShader);
        this.filterSourceTexture2 = -1;
        this.filterSourceTexture3 = -1;
        setRotation(Rotation.NORMAL, false, false);
    }

    public void onInit() {
        super.onInit();
        this.filterTextureCoordinateAttribute2 = GLES20.glGetAttribLocation(getProgram(), "inputTextureCoordinate2");
        this.filterInputTextureUniform2 = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture2");
        GLES20.glEnableVertexAttribArray(this.filterTextureCoordinateAttribute2);
        if (this.mBitmap2 != null) {
            setBitmap2(this.mBitmap2);
        }
        this.filterTextureCoordinateAttribute3 = GLES20.glGetAttribLocation(getProgram(), "inputTextureCoordinate3");
        this.filterInputTextureUniform3 = GLES20.glGetUniformLocation(getProgram(), "inputImageTexture3");
        GLES20.glEnableVertexAttribArray(this.filterTextureCoordinateAttribute3);
        if (this.mBitmap3 != null) {
            setBitmap3(this.mBitmap3);
        }
    }

    public void setBitmap2(final Bitmap bitmap) {
        this.mBitmap2 = bitmap;
        runOnDraw(new Runnable() {
            public void run() {
                if (GPUImageThreeInputFilter.this.filterSourceTexture2 == -1) {
                    GLES20.glActiveTexture(33987);
                    GPUImageThreeInputFilter.this.filterSourceTexture2 = OpenGlUtils.loadTexture(bitmap, -1, false);
                }
            }
        });
    }

    public void setBitmap3(final Bitmap bitmap) {
        this.mBitmap3 = bitmap;
        runOnDraw(new Runnable() {
            public void run() {
                if (GPUImageThreeInputFilter.this.filterSourceTexture3 == -1) {
                    GLES20.glActiveTexture(33988);
                    GPUImageThreeInputFilter.this.filterSourceTexture3 = OpenGlUtils.loadTexture(bitmap, -1, false);
                }
            }
        });
    }

    public void onDestroy() {
        super.onDestroy();
        GLES20.glDeleteTextures(1, new int[]{this.filterSourceTexture2, this.filterSourceTexture3}, 0);
        this.filterSourceTexture2 = -1;
        this.filterSourceTexture3 = -1;
    }

    protected void onDrawArraysPre() {
        GLES20.glEnableVertexAttribArray(this.filterTextureCoordinateAttribute2);
        GLES20.glActiveTexture(33987);
        GLES20.glBindTexture(3553, this.filterSourceTexture2);
        GLES20.glUniform1i(this.filterInputTextureUniform2, 3);
        GLES20.glEnableVertexAttribArray(this.filterTextureCoordinateAttribute3);
        GLES20.glActiveTexture(33988);
        GLES20.glBindTexture(3553, this.filterSourceTexture3);
        GLES20.glUniform1i(this.filterInputTextureUniform3, 4);
        this.mTextureCoordinatesBuffer2.position(0);
        this.mTextureCoordinatesBuffer3.position(0);
        GLES20.glVertexAttribPointer(this.filterTextureCoordinateAttribute2, 2, 5126, false, 0, this.mTextureCoordinatesBuffer2);
        GLES20.glVertexAttribPointer(this.filterTextureCoordinateAttribute3, 2, 5126, false, 0, this.mTextureCoordinatesBuffer3);
    }

    public void setRotation(Rotation rotation, boolean flipHorizontal, boolean flipVertical) {
        float[] buffer = TextureRotationUtil.getRotation(rotation, flipHorizontal, flipVertical);
        ByteBuffer bBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder());
        FloatBuffer fBuffer = bBuffer.asFloatBuffer();
        fBuffer.put(buffer);
        fBuffer.flip();
        this.mTextureCoordinatesBuffer2 = bBuffer;
        float[] buffer2 = TextureRotationUtil.getRotation(rotation, flipHorizontal, flipVertical);
        ByteBuffer cBuffer = ByteBuffer.allocateDirect(32).order(ByteOrder.nativeOrder());
        FloatBuffer dBuffer = bBuffer.asFloatBuffer();
        dBuffer.put(buffer2);
        dBuffer.flip();
        this.mTextureCoordinatesBuffer3 = cBuffer;
    }
}
