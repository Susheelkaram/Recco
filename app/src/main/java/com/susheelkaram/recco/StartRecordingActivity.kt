package com.susheelkaram.recco

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.susheelkaram.recco.recording.RecordingEventListener
import com.susheelkaram.recco.recording.ScreenRecorder
import com.susheelkaram.recco.recording.ScreenRecorderMode
import com.susheelkaram.recco.util.toast
import kotlinx.android.synthetic.main.fragment_home_screen.*
import org.koin.android.ext.android.inject

class StartRecordingActivity : AppCompatActivity() {

    val screenRecorder: ScreenRecorder by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start_recording)

        var metrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(metrics)
        screenRecorder.apply {
            width = metrics.widthPixels
            height = metrics.heightPixels
            dpi = metrics.densityDpi
        }
        screenRecorder.start(this)
    }

//    override fun onClick(v: View?) {
//        when (v?.id) {
//            R.id.fab_StartRecording -> {
//                if (screenRecorder.isPaused) {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
//                        screenRecorder.resume()
//                    };
//                } else if (screenRecorder.isRecording) {
//                    screenRecorder.stop()
//                } else {
//                    screenRecorder.start(this)
//                }
//            }
//        }
//    }

//    fun setFabState(mode: ScreenRecorderMode) {
//        var color = R.color.colorPrimary
//        var drawable =
//            ContextCompat.getDrawable(this, R.drawable.ic_baseline_videocam_24)
//        when (mode) {
//            ScreenRecorderMode.RECORDING, ScreenRecorderMode.PAUSED -> {
//                color = android.R.color.holo_red_light
//                drawable =
//                    ContextCompat.getDrawable(this, R.drawable.ic_baseline_stop_24)
//            }
//            ScreenRecorderMode.STOPPED -> {
//            }
//        }
//
//        fab_StartRecording.setImageDrawable(drawable)
//        fab_StartRecording.backgroundTintList =
//            ContextCompat.getColorStateList(
//                this,
//                color
//            )
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode == ScreenRecorder.PROJECTION_REQUEST_CODE) {
            screenRecorder.onActivityResult(requestCode, resultCode, data)
            finish()
        }
    }
}