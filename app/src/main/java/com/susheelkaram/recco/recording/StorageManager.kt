package com.susheelkaram.recco.recording

import android.app.Application
import android.content.ContentValues
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.MediaStore
import com.susheelkaram.recco.recording.model.Recording
import java.io.FileDescriptor

/**
 * Created by Susheel Kumar Karam
 * Website - SusheelKaram.com
 */
class StorageManager(private var app: Application) {

    private var resolver = app.contentResolver

    companion object {
        const val RECORDINGDS_FOLDER_NAME = "Recco Recordings"
    }

    fun createRecordingFile(fileName: String): FileDescriptor? {
        var uri = MediaStore.Video.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        var contentValues = ContentValues().apply {
            put(MediaStore.Video.Media.DISPLAY_NAME, fileName)
            put(MediaStore.Video.Media.RELATIVE_PATH, "DCIM/$RECORDINGDS_FOLDER_NAME")
        }
        var newFileUri = resolver.insert(uri, contentValues)
        newFileUri?.let{
            return resolver.openFileDescriptor(newFileUri, "rw")?.fileDescriptor
        }

        return null
    }

    fun getRecordings() : List<Recording> {
        var recordingsList = mutableListOf<Recording>()

        var uri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI

        var projection = arrayOf(
            MediaStore.Video.VideoColumns._ID,
            MediaStore.Video.VideoColumns.DISPLAY_NAME,
            MediaStore.Video.VideoColumns.DURATION,
            MediaStore.Video.VideoColumns.DATE_ADDED,
            MediaStore.Video.VideoColumns.SIZE
        )

        var selection = MediaStore.Video.VideoColumns.RELATIVE_PATH + " like ?"
        var selectionArgs = arrayOf("%$RECORDINGDS_FOLDER_NAME%");

        var recordingsCursor = resolver.query(
            uri,
            projection,
            selection,
            selectionArgs,
            MediaStore.Video.VideoColumns.DATE_ADDED + " DESC",
            null
        )


        recordingsCursor?.let {
            var idCol = recordingsCursor.getColumnIndex(MediaStore.Video.VideoColumns._ID)
            var nameCol = recordingsCursor.getColumnIndex(MediaStore.Video.VideoColumns.DISPLAY_NAME)
            var durationCol = recordingsCursor.getColumnIndex(MediaStore.Video.VideoColumns.DURATION)
            var dateAddedCol = recordingsCursor.getColumnIndex(MediaStore.Video.VideoColumns.DATE_ADDED)
            var sizeCol = recordingsCursor.getColumnIndex(MediaStore.Video.VideoColumns.SIZE)

            while (recordingsCursor.moveToNext()) {
                recordingsList.add(
                    Recording(
                        id = recordingsCursor.getLong(idCol),
                        name = recordingsCursor.getString(nameCol),
                        dateAdded = recordingsCursor.getInt(dateAddedCol),
                        duration = recordingsCursor.getInt(durationCol),
                        size = recordingsCursor.getInt(sizeCol)
                    )
                )
            }
        }

        return recordingsList;
    }
}