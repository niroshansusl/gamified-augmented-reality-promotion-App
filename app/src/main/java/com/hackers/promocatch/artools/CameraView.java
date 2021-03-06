package com.hackers.promocatch.artools;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class CameraView extends SurfaceView {
	Camera camera;
	SurfaceHolder previewHolder;

	// Callback for the surfaceholder
	SurfaceHolder.Callback surfaceHolderListener = new SurfaceHolder.Callback() {
		public void surfaceCreated(SurfaceHolder holder) {

			try {
				camera = Camera.open();
				camera.setDisplayOrientation(270);
				camera.setPreviewDisplay(previewHolder);
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}

		public void surfaceChanged(SurfaceHolder surfaceHolder, int format,
				int w, int h) {
			// Parameters params = camera.getParameters();
			// params.setPreviewSize(w, h);
			// params.setPictureFormat(PixelFormat.JPEG);
			// camera.setParameters(params);
			camera.startPreview();
		}

		public void surfaceDestroyed(SurfaceHolder arg0) {
			camera.stopPreview();
			camera.release();
		}
	};

	@SuppressWarnings("deprecation")
	public CameraView(Context ctx) {
		super(ctx);
		previewHolder = this.getHolder();
		previewHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		previewHolder.addCallback(surfaceHolderListener);
		setBackgroundColor(Color.TRANSPARENT);
	}

	public CameraView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	public void closeCamera() {
		if (camera != null)
			camera.release();
	}

	public void dispatchDraw(Canvas c) {
		super.dispatchDraw(c);
	}
}
