package com.github.atyranovets.homemedicineinventorysystem.lib

import android.Manifest
import com.github.atyranovets.homemedicineinventorysystem.R

object PermissionExplanation {
    val permissionExplanations = mapOf(Manifest.permission.CAMERA to Camera, Manifest.permission.WRITE_EXTERNAL_STORAGE to Storage);

    object Camera: PermissionRequirementInfo {
        override val title = R.string.permission_camera_title;
        override val info = R.string.permission_camera_info;
        override val icon = R.drawable.ic_baseline_camera;
    }

    object Storage: PermissionRequirementInfo {
        override val title = R.string.permission_storage_title;
        override val info = R.string.permission_storage_info;
        override val icon = R.drawable.ic_baseline_folder_open;
    }
}

interface PermissionRequirementInfo {
    val title: Int;
    val icon: Int;
    val info: Int;
}
