package com.github.atyranovets.homemedicineinventorysystem.lib

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat

fun Activity.requestPermissions(requiredPermissions: Array<String>, permissionRequestCode: Int, whenHavePermission: () -> Unit) {
    //If any of required permissions is not granted
    if(requiredPermissions.any{ ActivityCompat.checkSelfPermission(this, it) != PackageManager.PERMISSION_GRANTED }) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(requiredPermissions, permissionRequestCode);
        }
    }
    else  {
        whenHavePermission();
    }
}
