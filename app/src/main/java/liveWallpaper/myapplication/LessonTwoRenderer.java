package liveWallpaper.myapplication;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.opengl.Matrix;
import android.os.SystemClock;
import android.util.Log;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public class LessonTwoRenderer implements GLSurfaceView.Renderer {
    private static final String TAG = "LessonTwoRenderer";
    private int mColorHandle;
    private final FloatBuffer mCubeColors;
    private final FloatBuffer mCubeNormals;
    private final FloatBuffer mCubePositions;
    private int mLightPosHandle;
    private int mMVMatrixHandle;
    private int mMVPMatrixHandle;
    private int mNormalHandle;
    private int mPerVertexProgramHandle;
    private int mPointProgramHandle;
    private int mPositionHandle;
    private float[] mModelMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];
    private float[] mMVPMatrix = new float[16];
    private float[] mLightModelMatrix = new float[16];
    private final int mBytesPerFloat = 4;
    private final int mPositionDataSize = 3;
    private final int mColorDataSize = 4;
    private final int mNormalDataSize = 3;
    private final float[] mLightPosInModelSpace = {0.0f, 0.0f, 0.0f, 1.0f};
    private final float[] mLightPosInWorldSpace = new float[4];
    private final float[] mLightPosInEyeSpace = new float[4];

    protected String getFragmentShader() {
        return "precision mediump float;       \nvarying vec4 v_Color;          \nvoid main()                    \n{                              \n   gl_FragColor = v_Color;     \n}                              \n";
    }

    protected String getVertexShader() {
        return "uniform mat4 u_MVPMatrix;      \nuniform mat4 u_MVMatrix;       \nuniform vec3 u_LightPos;       \nattribute vec4 a_Position;     \nattribute vec4 a_Color;        \nattribute vec3 a_Normal;       \nvarying vec4 v_Color;          \nvoid main()                    \n{                              \n   vec3 modelViewVertex = vec3(u_MVMatrix * a_Position);              \n   vec3 modelViewNormal = vec3(u_MVMatrix * vec4(a_Normal, 0.0));     \n   float distance = length(u_LightPos - modelViewVertex);             \n   vec3 lightVector = normalize(u_LightPos - modelViewVertex);        \n   float diffuse = max(dot(modelViewNormal, lightVector), 0.1);       \n   diffuse = diffuse * (1.0 / (1.0 + (0.25 * distance * distance)));  \n   v_Color = a_Color * diffuse;                                       \n   gl_Position = u_MVPMatrix * a_Position;                            \n}                                                                     \n";
    }

    public LessonTwoRenderer() {
        FloatBuffer asFloatBuffer = ByteBuffer.allocateDirect(432).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mCubePositions = asFloatBuffer;
        asFloatBuffer.put(new float[]{-1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, 1.0f, -1.0f, -1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, -1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f, 1.0f, -1.0f, 1.0f, -1.0f, -1.0f, 1.0f, -1.0f, -1.0f, -1.0f}).position(0);
        FloatBuffer asFloatBuffer2 = ByteBuffer.allocateDirect(576).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mCubeColors = asFloatBuffer2;
        asFloatBuffer2.put(new float[]{1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f, 1.0f}).position(0);
        FloatBuffer asFloatBuffer3 = ByteBuffer.allocateDirect(432).order(ByteOrder.nativeOrder()).asFloatBuffer();
        this.mCubeNormals = asFloatBuffer3;
        asFloatBuffer3.put(new float[]{0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, -1.0f, 0.0f}).position(0);
    }

    @Override 
    public void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glEnable(2884);
        GLES20.glEnable(2929);
        Matrix.setLookAtM(this.mViewMatrix, 0, 0.0f, 0.0f, -0.5f, 0.0f, 0.0f, -5.0f, 0.0f, 1.0f, 0.0f);
        this.mPerVertexProgramHandle = createAndLinkProgram(compileShader(35633, getVertexShader()), compileShader(35632, getFragmentShader()), new String[]{"a_Position", "a_Color", "a_Normal"});
        this.mPointProgramHandle = createAndLinkProgram(compileShader(35633, "uniform mat4 u_MVPMatrix;      \nattribute vec4 a_Position;     \nvoid main()                    \n{                              \n   gl_Position = u_MVPMatrix   \n               * a_Position;   \n   gl_PointSize = 5.0;         \n}                              \n"), compileShader(35632, "precision mediump float;       \nvoid main()                    \n{                              \n   gl_FragColor = vec4(1.0,    \n   1.0, 1.0, 1.0);             \n}                              \n"), new String[]{"a_Position"});
    }

    @Override 
    public void onSurfaceChanged(GL10 gl10, int i, int i2) {
        GLES20.glViewport(0, 0, i, i2);
        float f = i / i2;
        Matrix.frustumM(this.mProjectionMatrix, 0, -f, f, -1.0f, 1.0f, 1.0f, 10.0f);
    }

    @Override 
    public void onDrawFrame(GL10 gl10) {
        GLES20.glClear(16640);
        float uptimeMillis = ((int) (SystemClock.uptimeMillis() % 10000)) * 0.036f;
        GLES20.glUseProgram(this.mPerVertexProgramHandle);
        this.mMVPMatrixHandle = GLES20.glGetUniformLocation(this.mPerVertexProgramHandle, "u_MVPMatrix");
        this.mMVMatrixHandle = GLES20.glGetUniformLocation(this.mPerVertexProgramHandle, "u_MVMatrix");
        this.mLightPosHandle = GLES20.glGetUniformLocation(this.mPerVertexProgramHandle, "u_LightPos");
        this.mPositionHandle = GLES20.glGetAttribLocation(this.mPerVertexProgramHandle, "a_Position");
        this.mColorHandle = GLES20.glGetAttribLocation(this.mPerVertexProgramHandle, "a_Color");
        this.mNormalHandle = GLES20.glGetAttribLocation(this.mPerVertexProgramHandle, "a_Normal");
        Matrix.setIdentityM(this.mLightModelMatrix, 0);
        Matrix.translateM(this.mLightModelMatrix, 0, 0.0f, 0.0f, -5.0f);
        Matrix.rotateM(this.mLightModelMatrix, 0, uptimeMillis, 0.0f, 1.0f, 0.0f);
        Matrix.translateM(this.mLightModelMatrix, 0, 0.0f, 0.0f, 2.0f);
        Matrix.multiplyMV(this.mLightPosInWorldSpace, 0, this.mLightModelMatrix, 0, this.mLightPosInModelSpace, 0);
        Matrix.multiplyMV(this.mLightPosInEyeSpace, 0, this.mViewMatrix, 0, this.mLightPosInWorldSpace, 0);
        Matrix.setIdentityM(this.mModelMatrix, 0);
        Matrix.translateM(this.mModelMatrix, 0, 4.0f, 0.0f, -7.0f);
        Matrix.rotateM(this.mModelMatrix, 0, uptimeMillis, 1.0f, 0.0f, 0.0f);
        drawCube();
        Matrix.setIdentityM(this.mModelMatrix, 0);
        Matrix.translateM(this.mModelMatrix, 0, -4.0f, 0.0f, -7.0f);
        Matrix.rotateM(this.mModelMatrix, 0, uptimeMillis, 0.0f, 1.0f, 0.0f);
        drawCube();
        Matrix.setIdentityM(this.mModelMatrix, 0);
        Matrix.translateM(this.mModelMatrix, 0, 0.0f, 4.0f, -7.0f);
        Matrix.rotateM(this.mModelMatrix, 0, uptimeMillis, 0.0f, 0.0f, 1.0f);
        drawCube();
        Matrix.setIdentityM(this.mModelMatrix, 0);
        Matrix.translateM(this.mModelMatrix, 0, 0.0f, -4.0f, -7.0f);
        drawCube();
        Matrix.setIdentityM(this.mModelMatrix, 0);
        Matrix.translateM(this.mModelMatrix, 0, 0.0f, 0.0f, -5.0f);
        Matrix.rotateM(this.mModelMatrix, 0, uptimeMillis, 1.0f, 1.0f, 0.0f);
        drawCube();
        GLES20.glUseProgram(this.mPointProgramHandle);
        drawLight();
    }

    private void drawCube() {
        this.mCubePositions.position(0);
        GLES20.glVertexAttribPointer(this.mPositionHandle, 3, 5126, false, 0, (Buffer) this.mCubePositions);
        GLES20.glEnableVertexAttribArray(this.mPositionHandle);
        this.mCubeColors.position(0);
        GLES20.glVertexAttribPointer(this.mColorHandle, 4, 5126, false, 0, (Buffer) this.mCubeColors);
        GLES20.glEnableVertexAttribArray(this.mColorHandle);
        this.mCubeNormals.position(0);
        GLES20.glVertexAttribPointer(this.mNormalHandle, 3, 5126, false, 0, (Buffer) this.mCubeNormals);
        GLES20.glEnableVertexAttribArray(this.mNormalHandle);
        Matrix.multiplyMM(this.mMVPMatrix, 0, this.mViewMatrix, 0, this.mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(this.mMVMatrixHandle, 1, false, this.mMVPMatrix, 0);
        float[] fArr = this.mMVPMatrix;
        Matrix.multiplyMM(fArr, 0, this.mProjectionMatrix, 0, fArr, 0);
        GLES20.glUniformMatrix4fv(this.mMVPMatrixHandle, 1, false, this.mMVPMatrix, 0);
        int i = this.mLightPosHandle;
        float[] fArr2 = this.mLightPosInEyeSpace;
        GLES20.glUniform3f(i, fArr2[0], fArr2[1], fArr2[2]);
        GLES20.glDrawArrays(4, 0, 36);
    }

    private void drawLight() {
        int glGetUniformLocation = GLES20.glGetUniformLocation(this.mPointProgramHandle, "u_MVPMatrix");
        int glGetAttribLocation = GLES20.glGetAttribLocation(this.mPointProgramHandle, "a_Position");
        float[] fArr = this.mLightPosInModelSpace;
        GLES20.glVertexAttrib3f(glGetAttribLocation, fArr[0], fArr[1], fArr[2]);
        GLES20.glDisableVertexAttribArray(glGetAttribLocation);
        Matrix.multiplyMM(this.mMVPMatrix, 0, this.mViewMatrix, 0, this.mLightModelMatrix, 0);
        float[] fArr2 = this.mMVPMatrix;
        Matrix.multiplyMM(fArr2, 0, this.mProjectionMatrix, 0, fArr2, 0);
        GLES20.glUniformMatrix4fv(glGetUniformLocation, 1, false, this.mMVPMatrix, 0);
        GLES20.glDrawArrays(0, 0, 1);
    }

    private int compileShader(int i, String str) {
        int glCreateShader = GLES20.glCreateShader(i);
        if (glCreateShader != 0) {
            GLES20.glShaderSource(glCreateShader, str);
            GLES20.glCompileShader(glCreateShader);
            int[] iArr = new int[1];
            GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
            if (iArr[0] == 0) {
                Log.e(TAG, "Error compiling shader: " + GLES20.glGetShaderInfoLog(glCreateShader));
                GLES20.glDeleteShader(glCreateShader);
                glCreateShader = 0;
            }
        }
        if (glCreateShader != 0) {
            return glCreateShader;
        }
        throw new RuntimeException("Error creating shader.");
    }

    private int createAndLinkProgram(int i, int i2, String[] strArr) {
        int glCreateProgram = GLES20.glCreateProgram();
        if (glCreateProgram != 0) {
            GLES20.glAttachShader(glCreateProgram, i);
            GLES20.glAttachShader(glCreateProgram, i2);
            if (strArr != null) {
                int length = strArr.length;
                for (int i3 = 0; i3 < length; i3++) {
                    GLES20.glBindAttribLocation(glCreateProgram, i3, strArr[i3]);
                }
            }
            GLES20.glLinkProgram(glCreateProgram);
            int[] iArr = new int[1];
            GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
            if (iArr[0] == 0) {
                Log.e(TAG, "Error compiling program: " + GLES20.glGetProgramInfoLog(glCreateProgram));
                GLES20.glDeleteProgram(glCreateProgram);
                glCreateProgram = 0;
            }
        }
        if (glCreateProgram != 0) {
            return glCreateProgram;
        }
        throw new RuntimeException("Error creating program.");
    }
}
