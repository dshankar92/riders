package com.bikegroup.riders.view.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bikegroup.riders.R;
import com.bikegroup.riders.view.control.ImageSurfaceView;
import com.bikegroup.riders.view.utils.AndroidAppUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class CameraFragment extends Fragment implements View.OnClickListener {

    private View rootView;
    private ImageSurfaceView mImageSurfaceView;
    private Camera mCamera;

    private FrameLayout cameraPreviewLayout;
    private ImageView capturedImageHolder;
    private Activity mActivity;
    private int CAMERA_REQ_CODE = 101, WRITE_TO_EXTERNAL_STORAGE_REQ_CODE = 100, PERMISSION_ALL_REQ_CODE = 1001;
    /**
     * \
     * Debuggable TAG
     */
    private String TAG = CameraFragment.class.getSimpleName();

    String[] PERMISSIONS = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CAMERA};
    private int currentCameraId = 0;
    private Button btnSwitch, btnCapture;
    private Boolean boolIsPreview = false;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.camera_preview_layout, container, false);
        mActivity = getActivity();

        cameraPreviewLayout = (FrameLayout) rootView.findViewById(R.id.camera_preview);
        capturedImageHolder = (ImageView) rootView.findViewById(R.id.captured_image);
        btnSwitch = (Button) rootView.findViewById(R.id.btnSwitch);
        btnSwitch.setOnClickListener(this);
        btnCapture = (Button) rootView.findViewById(R.id.btnCapture);
        btnCapture.setOnClickListener(this);
        mCamera = checkDeviceCamera();
        if (mCamera != null) {
            mImageSurfaceView = new ImageSurfaceView(mActivity, mCamera);
            cameraPreviewLayout.addView(mImageSurfaceView);
        }

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();

        if (!hasPermissions(mActivity, PERMISSIONS)) {
            ActivityCompat.requestPermissions(mActivity, PERMISSIONS, PERMISSION_ALL_REQ_CODE);
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ALL_REQ_CODE) {
            if (grantResults.length > 0) {

                boolean boolIsCameraPermission = grantResults[2] == PackageManager.PERMISSION_GRANTED;
                boolean boolISReadExternalStoragePermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                boolean boolISWriteExternalStoragePermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                AndroidAppUtils.showInfoLog(TAG, "boolIsCameraPermission : " + boolIsCameraPermission
                        + "boolISReadExternalStoragePermission : " + boolISReadExternalStoragePermission +
                        "\n boolISWriteExternalStoragePermission : " + boolISWriteExternalStoragePermission);
                if (boolIsCameraPermission) {
                    mCamera = checkDeviceCamera();
                    mImageSurfaceView = new ImageSurfaceView(mActivity, mCamera);
                    cameraPreviewLayout.addView(mImageSurfaceView);
                }
                if (boolISReadExternalStoragePermission && boolISWriteExternalStoragePermission) {

                }
            }
        }
    }

    private Camera checkDeviceCamera() {
        Camera mCamera = null;
        try {
            releaseCameraAndPreview();
            if (currentCameraId == 0) {
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
            } else {
                mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK);
            }
            boolIsPreview = true;
        } catch (Exception e) {
            AndroidAppUtils.showInfoLog(TAG, "failed to open Camera");
            e.printStackTrace();
        }
        return mCamera;
    }

    Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (bitmap == null) {
                AndroidAppUtils.showToast(mActivity, "Captured image is empty\"");
                return;
            }
            String partFilename = currentDateFormat();
            storeCameraPhotoInSDCard(bitmap, partFilename);
            if (mCamera != null) {
//                mCamera.release();
                mCamera.startPreview();
            }
            // display the image from SD Card to ImageView Control
            String storeFilename = "photo_" + partFilename + ".jpg";
            Bitmap mBitmap = getImageFileFromSDCard(storeFilename);
            if(mBitmap!=null) {
                capturedImageHolder.setImageBitmap(scaleDownBitmapImage(mBitmap, 200, 100));
            }
        }
    };


    private Bitmap scaleDownBitmapImage(Bitmap bitmap, int newWidth, int newHeight) {
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, newWidth, newHeight, true);
        return resizedBitmap;
    }

    private void storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate) {
        File outputFile = new File(Environment.getExternalStorageDirectory(), "photo_" + currentDate + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getImageFileFromSDCard(String filename) {
        Bitmap bitmap = null;
        File imageFile = new File(Environment.getExternalStorageDirectory() + filename);
        try {
            if (imageFile.exists()) {
                FileInputStream fis = new FileInputStream(imageFile);
                bitmap = BitmapFactory.decodeStream(fis);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private String currentDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    public static boolean hasPermissions(Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    private void releaseCameraAndPreview() {
        if (mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSwitch:
                if (boolIsPreview) {
                    mCamera.stopPreview();
                }
//NB: if you don't release the current camera before switching, you app will crash
                mCamera.release();

//swap the id of the camera to be used
                if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                } else {
                    currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                }
                mCamera = Camera.open(currentCameraId);

                setCameraDisplayOrientation(mActivity, currentCameraId, mCamera);
                try {

                    mCamera.setPreviewDisplay(mImageSurfaceView.getHolder());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mCamera.startPreview();
                break;

            case R.id.btnCapture:
                if (mCamera != null)
                    mCamera.takePicture(null, null, pictureCallback);
                break;
        }
    }

    public static void setCameraDisplayOrientation(Activity activity,
                                                   int cameraId, android.hardware.Camera camera) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        camera.setDisplayOrientation(result);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mImageSurfaceView != null && mCamera!=null) {
            releaseCameraAndPreview();
        }
    }
}
