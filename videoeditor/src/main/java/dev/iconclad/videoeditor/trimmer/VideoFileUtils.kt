package dev.iconclad.videoeditor.trimmer

import android.content.Context
import android.os.Environment
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class VideoFileUtil(context: Context) {
    private val appContext: Context = context

    fun createOutputVideoPath(): String {
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val storageDir: File? = appContext.getExternalFilesDir(Environment.DIRECTORY_MOVIES)

        if (storageDir != null && storageDir.exists()) {
            val videoFileName = "VIDEO_$timeStamp.mp4"
            return File(storageDir, videoFileName).absolutePath
        } else {
            // Handle the case where external storage is not available
            return ""
        }
    }
}
