package com.susheelkaram.recco.recording

import android.Manifest
import android.app.Application
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat

/**
 * Created by Susheel Kumar Karam
 * Website - SusheelKaram.com
 */
class PermissionManager {

    companion object {
        val PERMISSION_REQUEST_CODE = 502

        val requiredPermissions = listOf<String>(
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECORD_AUDIO
        )

        fun checkPermissions(app: Application) : Boolean {
            for(permission in requiredPermissions) {
                if(!isPermissionGranted(permission, app)) {
                    return false
                }
            }
            return true
        }

        fun isPermissionGranted(permission: String, app: Application) : Boolean {
            val permissionStatus =  ContextCompat.checkSelfPermission(app, Manifest.permission.WRITE_EXTERNAL_STORAGE)
            return permissionStatus == PackageManager.PERMISSION_GRANTED
        }
    }

}