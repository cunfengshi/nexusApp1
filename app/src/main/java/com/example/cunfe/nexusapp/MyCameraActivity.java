package com.example.cunfe.nexusapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.ImageReader;
import android.os.HandlerThread;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.Size;
import android.view.MotionEvent;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;

import java.util.Arrays;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class MyCameraActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_camera);
        initLooper();
        initUIAndListener();
    }
    @Override
    protected void onStop() {
        if(activeCamera!=null)activeCamera.close();
        super.onStop();
    }
    @Override
    protected void onDestroy() {
        if(activeCamera!=null)activeCamera.close();
        super.onDestroy();
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        initLooper();
        initUIAndListener();
        onSurfaceTextureAvailable(mPreviewView.getSurfaceTexture(), mPreviewSize.getWidth(), mPreviewSize.getHeight());
    }

    private TextureView mPreviewView;
    private Handler mHandler;
    private HandlerThread mThreadHandler;
    private Size mPreviewSize;
    private CaptureRequest.Builder mPreviewBuilder;
    String  mCameraId = ""+CameraCharacteristics.LENS_FACING_FRONT;
    private CameraManager cameraManager;

    //很多过程都变成了异步的了，所以这里需要一个子线程的looper
    private void initLooper() {
        mThreadHandler = new HandlerThread("CAMERA2");
        mThreadHandler.start();
        mHandler = new Handler(mThreadHandler.getLooper());
    }
    //可以通过TextureView或者SurfaceView
    private void initUIAndListener() {
        mPreviewView = (TextureView) findViewById(R.id.surfaceViewMyCamera);
        mPreviewView.setSurfaceTextureListener(this);
    }

    @SuppressWarnings("ResourceType")
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        try {
            //获得CameraManager
            if(cameraManager==null) {
                cameraManager = (CameraManager) this.getSystemService(Context.CAMERA_SERVICE);
                //获得属性
                CameraCharacteristics characteristics = cameraManager.getCameraCharacteristics(mCameraId);
                //支持的STREAM CONFIGURATION
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
                //显示的size
                mPreviewSize = map.getOutputSizes(SurfaceTexture.class)[0];
            }
            //打开相机
            cameraManager.openCamera(mCameraId, mCameraDeviceStateCallback, mHandler);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        return false;
    }

    //TextureView.SurfaceTextureListener
    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
    private CameraDevice activeCamera;
    private CameraDevice.StateCallback mCameraDeviceStateCallback = new CameraDevice.StateCallback() {

        @Override
        public void onOpened(CameraDevice camera) {
            try {
                activeCamera=camera;
                startPreview(camera);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onDisconnected(CameraDevice camera) {
            camera.close();
        }

        @Override
        public void onError(CameraDevice camera, int error) {
            camera.close();
        }
    };

    private  SurfaceTexture texture;
    //开始预览，主要是camera.createCaptureSession这段代码很重要，创建会话
    private void startPreview(CameraDevice camera) throws CameraAccessException {
        if (texture==null){
            texture = mPreviewView.getSurfaceTexture();
        }
        texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        Surface surface = new Surface(texture);
        try {
            mPreviewBuilder = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        mPreviewBuilder.addTarget(surface);
        camera.createCaptureSession(Arrays.asList(surface), mSessionStateCallback, mHandler);
    }

    private CameraCaptureSession.StateCallback mSessionStateCallback = new CameraCaptureSession.StateCallback() {

        @Override
        public void onConfigured(CameraCaptureSession session) {
            try {
                updatePreview(session);
            } catch (CameraAccessException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onConfigureFailed(CameraCaptureSession session) {

        }
    };

    private void updatePreview(CameraCaptureSession session) throws CameraAccessException {
        session.setRepeatingRequest(mPreviewBuilder.build(), null, mHandler);
    }
    ///切换前后摄像头
    public void ChangeCamera(View view)
    {
        if(mCameraId.equals(""+CameraCharacteristics.LENS_FACING_FRONT))
        {
            mCameraId=""+CameraCharacteristics.LENS_FACING_BACK;
        }else {
            mCameraId=""+CameraCharacteristics.LENS_FACING_FRONT;
        }
        activeCamera.close();

        onSurfaceTextureAvailable(mPreviewView.getSurfaceTexture(), mPreviewSize.getWidth(), mPreviewSize.getHeight());
    }

    boolean isClosed =false;
    public void MyTakePhot(View view) throws CameraAccessException {
        if(isClosed)
        {
            onSurfaceTextureAvailable(mPreviewView.getSurfaceTexture(), mPreviewSize.getWidth(), mPreviewSize.getHeight());
        }else
        {
            activeCamera.close();
        }
        isClosed=!isClosed;
    }
}
