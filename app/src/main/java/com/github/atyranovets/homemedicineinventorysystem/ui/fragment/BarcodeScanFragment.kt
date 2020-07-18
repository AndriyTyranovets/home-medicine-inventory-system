package com.github.atyranovets.homemedicineinventorysystem.ui.fragment

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.core.util.forEach
import androidx.core.util.isNotEmpty
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.fragment.app.commitNow
import com.github.atyranovets.homemedicineinventorysystem.R
import com.github.atyranovets.homemedicineinventorysystem.lib.Constants
import com.github.atyranovets.homemedicineinventorysystem.lib.requestPermissions
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector


class BarcodeScanFragment : Fragment() {

    private lateinit var cameraSource: CameraSource;
    private lateinit var barcodeDetector: BarcodeDetector;
    private lateinit var surfaceView: SurfaceView;

    private val surfaceHolderCallback = object: SurfaceHolder.Callback {
        override fun surfaceCreated(holder: SurfaceHolder?) {
            if (ActivityCompat.checkSelfPermission(context!!, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                cameraSource.start(holder);
            }
        }

        override fun surfaceChanged(holder: SurfaceHolder?, format: Int, width: Int, height: Int) = Unit;

        override fun surfaceDestroyed(holder: SurfaceHolder?) = cameraSource.stop();
    }

    private val barcodeDetectorProcessor = object: Detector.Processor<Barcode> {
        override fun receiveDetections(detections: Detector.Detections<Barcode>?) {
                //TODO Proper handling
                val barcodes = detections?.detectedItems;
                barcodes?.forEach { _, barcode -> Log.d(Constants.loggerTag, barcode.displayValue); }
        }

        override fun release() = Unit;
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = inflater.inflate(R.layout.fragment_barcode_scan, container, false);
        surfaceView = binding.findViewById<SurfaceView>(R.id.barcodeCameraSurfaceView);
        requestPermissions(arrayOf(Manifest.permission.CAMERA), Constants.PermissionRequestId.camera) {
            setup();
        }
        return binding.rootView;
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        when (requestCode) {
            Constants.PermissionRequestId.camera ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    setup();
                } else {
                    parentFragmentManager.commit {
                        replace(R.id.nav_host, PermissionExplanationFragment::class.java, bundleOf(Constants.FragmentArgs.PermissionExplanation.permissionId to Manifest.permission.CAMERA))
                        setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                        addToBackStack(null);
                    }
                }
        }
    }

    private fun setup() {
        val barcodeTypes = Barcode.EAN_13 or Barcode.EAN_8 or Barcode.UPC_A or Barcode.UPC_E;
        barcodeDetector = BarcodeDetector.Builder(context).setBarcodeFormats(barcodeTypes).build();
        barcodeDetector.setProcessor(barcodeDetectorProcessor);
        cameraSource = CameraSource.Builder(context, barcodeDetector)
                .setFacing(CameraSource.CAMERA_FACING_BACK)
                .setAutoFocusEnabled(true)
                .build();
        surfaceView.holder.addCallback(surfaceHolderCallback);
    }
}
