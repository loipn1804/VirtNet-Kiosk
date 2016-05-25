package com.kiosk.view;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.PreviewCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.kiosk.activity.PlanarYUVLuminanceSource;
import com.kiosk.staticfunction.StaticFunction;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Hashtable;
import java.util.List;
import java.util.Vector;

public class Barcode2CameraSurfaceView extends SurfaceView implements SurfaceHolder.Callback {

    public interface CameraSurfaceCallback {
        void getBarcodeSuccess(String barcode);
    }

    private CameraSurfaceCallback cameraSurfaceCallback;

    private Activity context;
    private Camera camera;
    private SurfaceHolder surfaceHolder;

    String strResult = "";
    private final MultiFormatReader multiFormatReader;

    private final Hashtable<DecodeHintType, Object> hints;

    Vector<BarcodeFormat> decodeFormats;

    Vector<BarcodeFormat> QR_CODE_FORMATS = null;

    public int Width = 0;//640; //le. //640;
    public int Height = 0;//480;// le. //480;

    public Barcode2CameraSurfaceView(Activity context, CameraSurfaceCallback cameraSurfaceCallback) {
        super(context);
        this.context = context;
        this.cameraSurfaceCallback = cameraSurfaceCallback;
        surfaceHolder = this.getHolder();
        surfaceHolder.addCallback(this);
        surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


        QR_CODE_FORMATS = new Vector<BarcodeFormat>(1);
        QR_CODE_FORMATS.add(BarcodeFormat.CODABAR);


        decodeFormats = new Vector<BarcodeFormat>();

        decodeFormats.addAll(QR_CODE_FORMATS);

        hints = new Hashtable<DecodeHintType, Object>(1);
        hints.put(DecodeHintType.PURE_BARCODE, decodeFormats);

        multiFormatReader = new MultiFormatReader();

        multiFormatReader.setHints(hints);

        setWillNotDraw(false); // them cai nay moi ve len onDraw() dc

        DisplayMetrics metrics = new DisplayMetrics();

        context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        try {
            // open the camera
            //            camera = Camera.open();
            camera = openFrontFacingCameraGingerbread();
        } catch (RuntimeException e) {

            return;
        }
        Camera.Parameters param;
        param = camera.getParameters();
        List<Size> supportedPictureSizes = camera.getParameters().getSupportedPictureSizes();
        List<Size> sizeList = param.getSupportedPreviewSizes();
        if (sizeList.size() > 0) {
            Size size = getOptimalPreviewSize(sizeList, StaticFunction.getScreenWidth(context), StaticFunction.getScreenHeight(context));
            Size sizePicture = null;
            for (int i = 0; i < sizeList.size(); i++) {
                if (sizeList.get(i).width == size.width && sizeList.get(i).height == size.height) {
                    sizePicture = supportedPictureSizes.get(i);
                    break;
                }
            }
            if (size != null) {
                Width = size.width;
                Height = size.height;
                param.setPreviewSize(size.width, size.height);
                param.setPictureSize(sizePicture.width, sizePicture.height);

            }
        }
        //        param.setPreviewSize(sizeList.get(0).width, sizeList.get(0).height);
        //        param.setPictureSize(supportedPictureSizes.get(0).width, supportedPictureSizes.get(0).height);
        //        param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        camera.setParameters(param);
        camera.setPreviewCallback(previewCallback);
        try {
            // The Surface has been created, now tell the camera where to draw
            // the preview.
            camera.setPreviewDisplay(surfaceHolder);
            camera.startPreview();
        } catch (Exception e) {
            // check for exceptions
            //System.err.println(e);
            return;
        }
    }

    private Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) h / w;

        if (sizes == null)
            return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        // TODO Auto-generated method stub
        refreshCamera();
    }

    private void refreshCamera() {
        if (surfaceHolder.getSurface() == null) {
            // preview surface does not exist
            return;
        }

        // stop preview before making changes
        try {
            camera.setPreviewCallback(null);
            camera.stopPreview();
        } catch (Exception e) {
            // ignore: tried to stop a non-existent preview
        }


        // set preview size and make any resize, rotate or
        // reformatting changes here
        // start preview with new settings
        try {
            camera.setPreviewDisplay(surfaceHolder);

            camera.setPreviewCallback(previewCallback);
            camera.startPreview();
        } catch (Exception e) {

        }

        //txt1.setText(context.getResources().getConfiguration().orientation + "");
    }

    private PreviewCallback previewCallback = new PreviewCallback() {

        @Override
        public void onPreviewFrame(byte[] data, Camera camera) {
            Rect rect = new Rect(0, 0, Width, Height);
            //

            Result rawResult = null;
            PlanarYUVLuminanceSource source = new PlanarYUVLuminanceSource(data, Width, Height, rect.left, rect.top, rect.width(), rect.height(), false);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            try {
                rawResult = multiFormatReader.decodeWithState(bitmap);
            } catch (Exception re) {
                // continue
                Log.e("", "");
            } finally {
                multiFormatReader.reset();
            }
            //


            if (rawResult != null) {
                strResult = rawResult.getText();
//                Toast.makeText(context, strResult, Toast.LENGTH_SHORT).show();
                cameraSurfaceCallback.getBarcodeSuccess(strResult);
            } else {
                strResult = "Loading...";
            }


        }
    };


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        // TODO Auto-generated method stub
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            //            Toast.makeText(context, "released camera", Toast.LENGTH_SHORT).show();
            camera = null;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //        Paint myPaint = new Paint();
        //        myPaint.setColor(Color.BLUE);
        //        myPaint.setStyle(Paint.Style.STROKE);
        //        myPaint.setStrokeWidth(3);
        //
        //        Paint p = new Paint();
        //        p.setColor(Color.GREEN);
        //        p.setStrokeWidth(0);
        //        p.setTextSize(20);
        //
        //        canvas.drawText("Row 1", 10, 20, p);
        //        canvas.drawText("Row 1", 100, 20, p);
        //        canvas.drawText("Row 1", 200, 20, p);
        //        canvas.drawText("Row 4", 10, 40, p);
        //        canvas.drawText("Row 5", 10, 60, p);
    }

    private Camera openFrontFacingCameraGingerbread() {
        int Count = 0;
        Camera cam = null;
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
        Count = Camera.getNumberOfCameras();
        for (int camIdx = 0; camIdx < Count; camIdx++) {
            Camera.getCameraInfo(camIdx, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                try {
                    cam = Camera.open(camIdx);
                } catch (RuntimeException e) {

                }
            }
        }

        return cam;
    }

}
