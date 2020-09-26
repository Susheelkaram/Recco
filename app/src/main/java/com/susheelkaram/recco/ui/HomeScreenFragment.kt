package com.susheelkaram.recco.ui

import android.content.Context
import android.content.Intent
import android.graphics.drawable.Drawable
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.susheelkaram.recco.R
import com.susheelkaram.recco.recording.RecordingEventListener
import com.susheelkaram.recco.recording.ScreenRecorder
import com.susheelkaram.recco.recording.ScreenRecorderMode
import com.susheelkaram.recco.recording.ScreenRecorderMode.*
import kotlinx.android.synthetic.main.fragment_home_screen.*
import java.util.jar.Manifest

typealias onProjectionPermission = (resultCode: Int, data: Intent) -> Unit

class HomeScreenFragment : Fragment(), View.OnClickListener {

    lateinit var screenRecorder: ScreenRecorder

    val recordingEventListener = object : RecordingEventListener {
        override fun onRecordingModeChanged(mode: ScreenRecorderMode) {
            setFabState(mode)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var metrics = DisplayMetrics()
        requireActivity().windowManager.defaultDisplay.getMetrics(metrics)
        screenRecorder = ScreenRecorder(requireActivity().application).apply {
            width = metrics.widthPixels
            height = metrics.heightPixels
            dpi = metrics.densityDpi
        }
        fab_StartRecording.backgroundTintList = ContextCompat.getColorStateList(requireContext(), R.color.colorPrimary)
        fab_StartRecording.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab_StartRecording -> {
                if (screenRecorder.isPaused) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        screenRecorder.resume()
                    };
                } else if (screenRecorder.isRecording) {
                    screenRecorder.stop()
                } else {
                    screenRecorder.start(this)
                }
            }
        }
    }

    fun setFabState(mode: ScreenRecorderMode) {
        var color = R.color.colorPrimary
        var drawable =
            ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_videocam_24)
        when (mode) {
            RECORDING, PAUSED -> {
                color = android.R.color.holo_red_light
                drawable =
                    ContextCompat.getDrawable(requireContext(), R.drawable.ic_baseline_stop_24)
            }
            STOPPED -> {
            }
        }

        fab_StartRecording.setImageDrawable(drawable)
        fab_StartRecording.backgroundTintList =
            ContextCompat.getColorStateList(
                requireContext(),
                color
            )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        screenRecorder.onActivityResult(requestCode, resultCode, data)
    }

}