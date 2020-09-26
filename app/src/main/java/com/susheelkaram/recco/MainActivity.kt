package com.susheelkaram.recco

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.susheelkaram.recco.recording.PermissionManager
import com.susheelkaram.recco.util.toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(!PermissionManager.checkPermissions(application)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(PermissionManager.requiredPermissions.toTypedArray(), PermissionManager.PERMISSION_REQUEST_CODE)
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PermissionManager.PERMISSION_REQUEST_CODE) {
            var ungrantedPermissions = ""
            grantResults.withIndex().forEach() { result ->
                if (result.value != PackageManager.PERMISSION_GRANTED) {
                    ungrantedPermissions += ", " + permissions.get(result.index)
                }
            }
            if(ungrantedPermissions.length > 0) {
                toast("$ungrantedPermissions permissions are not granted")
            }
            else {
                toast("All permissions are granted.")
            }
        }
    }
}