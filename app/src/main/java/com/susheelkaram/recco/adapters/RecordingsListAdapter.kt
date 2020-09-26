package com.susheelkaram.recco.adapters

import android.content.ContentUris
import android.content.Intent
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.susheelkaram.recco.databinding.ItemRecordingBinding
import com.susheelkaram.recco.recording.model.Recording
import com.susheelkaram.recco.util.Utils
import kotlinx.android.synthetic.main.item_recording.view.*
import org.koin.android.java.KoinAndroidApplication
import org.koin.core.KoinApplication
import java.util.*
import kotlin.time.ExperimentalTime

/**
 * Created by Susheel Kumar Karam
 * Website - SusheelKaram.com
 */

class RecordingsListAdapter(private var onItemClick : (recording: Recording) -> Unit) : RecyclerView.Adapter<RecordingsListAdapter.RecordingItemVH>() {
    private var recordingsList = listOf<Recording>()

    inner class RecordingItemVH(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordingItemVH {
        var recordingItem = ItemRecordingBinding.inflate(LayoutInflater.from(parent.context))
        return RecordingItemVH(recordingItem.root)
    }

    @ExperimentalTime
    override fun onBindViewHolder(holder: RecordingItemVH, position: Int) {
        var recording = recordingsList[position]
        holder.itemView.txt_FileName.text = recording.name
        holder.itemView.txtDate.text = Utils.getFormattedDate(recording.dateAdded)
        holder.itemView.txtDurationSize.text = "${Utils.getFormattedDuration(recording.duration)} (${Utils.getFormattedSize(recording.size)})"

        holder.itemView.setOnClickListener({
            it -> onItemClick(recording)
        })
    }

    override fun getItemCount(): Int {
        return recordingsList.size
    }

    fun setData(recordings: List<Recording>) {
        recordingsList = recordings
        notifyDataSetChanged()
        // TODO: Use DiffUtil for data updates
    }

}