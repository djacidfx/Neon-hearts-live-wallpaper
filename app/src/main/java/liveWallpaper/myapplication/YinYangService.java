package liveWallpaper.myapplication;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.service.wallpaper.WallpaperService;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.widget.Toast;

import com.demo.lovelivewallpaper.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.Buffer;
import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;


public final class YinYangService extends WallpaperService {
    private Engine mEngine;

    @Override 
    public final Engine onCreateEngine() {
        WallpaperEngine wallpaperEngine = new WallpaperEngine();
        this.mEngine = wallpaperEngine;
        return wallpaperEngine;
    }

    
    private final class WallpaperEngine extends Engine {
        private YinYangSurfaceView mGLSurfaceView;

        private WallpaperEngine() {
            super();
        }

        @Override 
        public void onCreate(SurfaceHolder surfaceHolder) {
            super.onCreate(surfaceHolder);
            this.mGLSurfaceView = new YinYangSurfaceView();
            setTouchEventsEnabled(true);
        }

        @Override 
        public final void onDestroy() {
            super.onDestroy();
            this.mGLSurfaceView.onDestroy();
            this.mGLSurfaceView = null;
        }

        @Override 
        public final void onTouchEvent(MotionEvent motionEvent) {
            this.mGLSurfaceView.onTouch(motionEvent);
        }

        @Override 
        public final void onVisibilityChanged(boolean z) {
            super.onVisibilityChanged(z);
            if (z) {
                this.mGLSurfaceView.onResume();
                this.mGLSurfaceView.requestRender();
                return;
            }
            this.mGLSurfaceView.onPause();
        }
    }

    
    private final class YinYangSurfaceView extends GLSurfaceView implements GLSurfaceView.Renderer, Runnable {
        private final float[] mAspectRatio;
        private int mHeight;
        private ByteBuffer mScreenVertices;
        private final boolean[] mShaderCompilerSupported;
        private int mShaderProgram;
        private SurfaceHolder mSurfaceHolder;
        private boolean mTouchFollow;
        private final float[] mTouchPositions;
        private long mTouchTime;
        private int mWidth;

        private YinYangSurfaceView() {
            super(YinYangService.this);
            this.mAspectRatio = new float[2];
            this.mShaderCompilerSupported = new boolean[1];
            this.mShaderProgram = -1;
            this.mTouchFollow = false;
            this.mTouchPositions = new float[4];
            setEGLContextClientVersion(2);
            setRenderer(this);
            setRenderMode(0);
            onPause();
            ByteBuffer allocateDirect = ByteBuffer.allocateDirect(8);
            this.mScreenVertices = allocateDirect;
            allocateDirect.put(new byte[]{-1, 1, -1, -1, 1, 1, 1, -1}).position(0);
        }

        @Override 
        public final SurfaceHolder getHolder() {
            if (this.mSurfaceHolder == null) {
                this.mSurfaceHolder = YinYangService.this.mEngine.getSurfaceHolder();
            }
            return this.mSurfaceHolder;
        }

        private final int loadProgram(String str, String str2) throws Exception {
            int loadShader = loadShader(35633, str);
            int loadShader2 = loadShader(35632, str2);
            int glCreateProgram = GLES20.glCreateProgram();
            if (glCreateProgram != 0) {
                GLES20.glAttachShader(glCreateProgram, loadShader);
                GLES20.glAttachShader(glCreateProgram, loadShader2);
                GLES20.glLinkProgram(glCreateProgram);
                int[] iArr = new int[1];
                GLES20.glGetProgramiv(glCreateProgram, 35714, iArr, 0);
                if (iArr[0] != 1) {
                    String glGetProgramInfoLog = GLES20.glGetProgramInfoLog(glCreateProgram);
                    GLES20.glDeleteProgram(glCreateProgram);
                    throw new Exception(glGetProgramInfoLog);
                }
            }
            return glCreateProgram;
        }

        private final String loadRawResource(int i) throws Exception {
            InputStream openRawResource = getResources().openRawResource(i);
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            byte[] bArr = new byte[1024];
            while (true) {
                int read = openRawResource.read(bArr);
                if (read != -1) {
                    byteArrayOutputStream.write(bArr, 0, read);
                } else {
                    return byteArrayOutputStream.toString();
                }
            }
        }

        private final int loadShader(int i, String str) throws Exception {
            int glCreateShader = GLES20.glCreateShader(i);
            if (glCreateShader != 0) {
                GLES20.glShaderSource(glCreateShader, str);
                GLES20.glCompileShader(glCreateShader);
                int[] iArr = new int[1];
                GLES20.glGetShaderiv(glCreateShader, 35713, iArr, 0);
                if (iArr[0] == 0) {
                    String glGetShaderInfoLog = GLES20.glGetShaderInfoLog(glCreateShader);
                    GLES20.glDeleteShader(glCreateShader);
                    throw new Exception(glGetShaderInfoLog);
                }
            }
            return glCreateShader;
        }

        public final void onDestroy() {
            super.onDetachedFromWindow();
        }

        
        @Override 
        
        public final void onDrawFrame(GL10 gl10) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            GLES20.glClear(16384);
            if (this.mShaderCompilerSupported[0]) {
                if (!this.mTouchFollow) {
                    long uptimeMillis = SystemClock.uptimeMillis();
                    float max = Math.max(0.0f, 1.0f - (((float) (uptimeMillis - this.mTouchTime)) * 0.005f));
                    float[] fArr = this.mTouchPositions;
                    fArr[2] = fArr[0] + ((fArr[2] - fArr[0]) * max);
                    fArr[3] = fArr[1] + ((fArr[3] - fArr[1]) * max);
                    this.mTouchTime = uptimeMillis;
                    if (Math.abs(fArr[0] - fArr[2]) <= 0.001f) {
                        float[] fArr2 = this.mTouchPositions;
                    }
                    requestRender();
                }
                GLES20.glDisable(2884);
                GLES20.glDisable(3042);
                GLES20.glDisable(2929);
                GLES20.glUseProgram(this.mShaderProgram);
                int glGetUniformLocation = GLES20.glGetUniformLocation(this.mShaderProgram, "uAspectRatio");
                int glGetUniformLocation2 = GLES20.glGetUniformLocation(this.mShaderProgram, "uTouchPos");
                int glGetAttribLocation = GLES20.glGetAttribLocation(this.mShaderProgram, "aPosition");
                GLES20.glUniform2fv(glGetUniformLocation, 1, this.mAspectRatio, 0);
                GLES20.glUniform2fv(glGetUniformLocation2, 2, this.mTouchPositions, 0);
                GLES20.glVertexAttribPointer(glGetAttribLocation, 2, 5120, false, 2, (Buffer) this.mScreenVertices);
                GLES20.glEnableVertexAttribArray(glGetAttribLocation);
                GLES20.glDrawArrays(5, 0, 4);
            }
        }

        @Override 
        public final void onSurfaceChanged(GL10 gl10, int i, int i2) {
            this.mWidth = i;
            this.mHeight = i2;
            GLES20.glViewport(0, 0, i, i2);
            float[] fArr = this.mAspectRatio;
            int i3 = this.mWidth;
            fArr[0] = (i3 * 1.1f) / Math.min(i3, this.mHeight);
            float[] fArr2 = this.mAspectRatio;
            int i4 = this.mHeight;
            fArr2[1] = (i4 * 1.1f) / Math.min(this.mWidth, i4);
        }

        @Override 
        public final void onSurfaceCreated(GL10 gl10, EGLConfig eGLConfig) {
            GLES20.glGetBooleanv(36346, this.mShaderCompilerSupported, 0);
            if (!this.mShaderCompilerSupported[0]) {
                new Handler(Looper.getMainLooper()).post(this);
                return;
            }
            try {
                this.mShaderProgram = loadProgram(loadRawResource(R.raw.yinyang_vs), loadRawResource(R.raw.yinyang_fs));
            } catch (Exception e) {
                this.mShaderCompilerSupported[0] = false;
                e.printStackTrace();
            }
        }

        public final void onTouch(MotionEvent motionEvent) {
            this.mTouchTime = SystemClock.uptimeMillis();
            int action = motionEvent.getAction();
            if (action == 0) {
                this.mTouchFollow = true;
                this.mTouchPositions[0] = (((motionEvent.getX() * 2.0f) / this.mWidth) - 1.0f) * this.mAspectRatio[0];
                this.mTouchPositions[1] = (1.0f - ((motionEvent.getY() * 2.0f) / this.mHeight)) * this.mAspectRatio[1];
            } else if (action == 1) {
                this.mTouchFollow = false;
                requestRender();
                return;
            } else if (action != 2) {
                return;
            }
            this.mTouchPositions[2] = (((motionEvent.getX() * 2.0f) / this.mWidth) - 1.0f) * this.mAspectRatio[0];
            this.mTouchPositions[3] = (1.0f - ((motionEvent.getY() * 2.0f) / this.mHeight)) * this.mAspectRatio[1];
            requestRender();
        }

        @Override 
        public final void run() {
            Toast.makeText(YinYangService.this, "error : GLSL shader compiler not supported.", Toast.LENGTH_LONG).show();
        }
    }
}
