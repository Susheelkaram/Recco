package com.susheelkaram.recco.recording

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.hardware.display.DisplayManager
import android.hardware.display.VirtualDisplay
import android.media.CamcorderProfile
import android.media.MediaRecorder
import android.media.projection.MediaProjection
import android.media.projection.MediaProjectionManager
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.susheelkaram.recco.util.Utils
import java.io.FileDescriptor
import java.util.Calendar

/**
 * Created by Susheel Kumar Karam
 * Website - SusheelKaram.com
 */
enum class ScreenRecorderMode {
    RECORDING,
    PAUSED,
    STOPPED
}

interface RecordingEventListener {
    fun onRecordingModeChanged(mode: ScreenRecorderMode)
}

class ScreenRecorder(private val app: Application) {
    var listener : RecordingEventListener? = null

    var mode: ScreenRecorderMode = ScreenRecorderMode.STOPPED

    val isRecording: Boolean
        get() = mode == ScreenRecorderMode.RECORDING
    val isPaused: Boolean
        get() = mode == ScreenRecorderMode.PAUSED

    private var mediaRecorder: MediaRecorder? = MediaRecorder()
    private var mediaProjectionManager: MediaProjectionManager =
        app.getSystemService(Context.MEDIA_PROJECTION_SERVICE) as MediaProjectionManager
    private var mediaProjection: MediaProjection? = null
    private var virtualDisplay: VirtualDisplay? = null
    private var storageManager = StorageManager(app)

    var width = 720;
    var height = 1280;
    var dpi = 100;
    var virtualDisplayName = "virtualRecordingDisplay"

    companion object {
        const val PROJECTION_REQUEST_CODE = 501
    }

    fun start(fragment: Fragment) {
        if (isRecording) {
            Log.d(this.javaClass.simpleName, "Recording is already active")
        }
        fragment.startActivityForResult(
            mediaProjectionManager.createScreenCaptureIntent(),
            PROJECTION_REQUEST_CODE
        )
    }

    fun start(activity: Activity) {
        if (isRecording) {
            Log.d(this.javaClass.simpleName, "Recording is already active")
        }

        if (mediaRecorder == null) {
            mediaRecorder = MediaRecorder()
        }

        activity.startActivityForResult(
            mediaProjectionManager.createScreenCaptureIntent(),
            PROJECTION_REQUEST_CODE
        )
    }

    fun stop() {
        mode = ScreenRecorderMode.STOPPED
        mediaRecorder?.stop()
        listener?.onRecordingModeChanged(mode)
        reset()
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun pause() {
        mediaRecorder?.pause()
        mode = ScreenRecorderMode.PAUSED
        listener?.onRecordingModeChanged(mode)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    fun resume() {
        if (isPaused) mediaRecorder?.resume()
        mode = ScreenRecorderMode.RECORDING
        listener?.onRecordingModeChanged(mode)
    }


    fun reset() {
        mediaProjection?.stop()
        virtualDisplay?.release()
        mediaRecorder?.release()
        mediaRecorder = null
        mediaProjection = null
        virtualDisplay = null
    }

    fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == PROJECTION_REQUEST_CODE) {
            data?.let {
                startActualRecording(resultCode, data)
            }
        }
    }

    private fun prepareMediaRecorder() {
        mediaRecorder?.apply {
            setAudioSource(MediaRecorder.AudioSource.DEFAULT)
            setVideoSource(MediaRecorder.VideoSource.SURFACE)
            setProfile(CamcorderProfile.get(CamcorderProfile.QUALITY_720P))
            setVideoSize(width, height)
            var fileDescriptor: FileDescriptor? =
                storageManager.createRecordingFile(Utils.buildFileName("Screen_recording_", ".mp4"))
            setOutputFile(fileDescriptor)
            prepare()
        }
    }

    private fun startActualRecording(resultCode: Int, intent: Intent) {
        mode = ScreenRecorderMode.RECORDING
        prepareMediaRecorder()
        mediaProjection = mediaProjectionManager.getMediaProjection(resultCode, intent)
        virtualDisplay = buildVirtualDisplay(mediaProjection as MediaProjection)

        mediaRecorder?.start()
        listener?.onRecordingModeChanged(mode)
    }

    private fun buildVirtualDisplay(mediaProjection: MediaProjection): VirtualDisplay {
        return mediaProjection.createVirtualDisplay(
            virtualDisplayName,
            width,
            height,
            dpi,
            DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
            mediaRecorder?.surface,
            null,
            null
        )
    }

}
