package com.github.atyranovets.homemedicineinventorysystem.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.SurfaceHolder
import android.view.SurfaceView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.util.forEach
import androidx.core.util.isNotEmpty
import com.example.homemedicineinventorysystem.R
import com.github.atyranovets.homemedicineinventorysystem.lib.Constants
import com.github.atyranovets.homemedicineinventorysystem.lib.requestPermissions
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector

class BarcodeActivity : AppCompatActivity() {

    private lateinit var cameraSource: CameraSource;
    private lateinit var barcodeDetector: BarcodeDetector;

    private val surfaceHolderCallback = object: SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder?) {
            if (ActivityCompat.checkSelfPermission(this@BarcodeActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraSource.start(holder);
            }
        }

        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) = Unit;

        override fun surfaceDestroyed(holder: SurfaceHolder?) {
            cameraSource.stop();
        }
    }

    private val barcodeDetectorProcessor = object: Detector.Processor<Barcode> {
        override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
            if(detections?.detectedItems?.isNotEmpty() == true) {
                //TODO Proper handling
                val barcodes = detections.detectedItems;
                barcodes.forEach { _, barcode -> Log.d(Constants.loggerTag, barcode.displayValue); }
            }
        }

        override fun release() = Unit;
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barcode);
        requestPermissions(arrayOf(Manifest.permission.CAMERA), Constants.PermissionRequestId.camera) {
            setup();
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode and Constants.PermissionRequestId.camera != 0 && grantResults.isNotEmpty()) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setup();
            }
            else {
                //TODO Show permision required view/fragment
            }
        }
    }

    private fun setup() {
        val barcodeTypes = Barcode.EAN_13 or Barcode.EAN_8 or Barcode.UPC_A or Barcode.UPC_E;
        barcodeDetector = BarcodeDetector.Builder(this).setBarcodeFormats(barcodeTypes).build();
        barcodeDetector.setProcessor(barcodeDetectorProcessor);
        cameraSource = CameraSource.Builder(this, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true)
                .build();
        findViewById<SurfaceView>(R.id.barcodeCameraSurfaceView).holder.addCallback(surfaceHolderCallback);
    }
}
