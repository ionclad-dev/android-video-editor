package dev.iconclad.videoeditor.gallery

import android.content.Context
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import coil.load
import dev.iconclad.videoeditor.R
import dev.iconclad.videoeditor.util.view.SquareImageView
import okhttp3.internal.toLongOrDefault
import java.io.File
import java.util.concurrent.TimeUnit

class VideoGalleryGridAdapter(private val context: Context, private val videoList: ArrayList<String>) : BaseAdapter() {

    override fun getCount(): Int {
        return videoList.size
    }

    override fun getItem(position: Int): Any {
        return videoList[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        var layout: View? = convertView
        if (layout == null) {
            layout = LayoutInflater.from(context)
                .inflate(R.layout.item_video_gallery, parent, false)
        }

        val imageView = layout!!.findViewById<SquareImageView>(R.id.videoThumbnailView)
        val durationText = layout.findViewById<TextView>(R.id.videoDuration)


        val videoPath = videoList[position]
        imageView.load(File(videoPath))


        val duration = getVideoDuration(videoPath)
        val formattedDuration = formatDuration(duration)

        durationText.text = formattedDuration

        return layout
    }

    private fun getVideoDuration(videoPath: String): Long {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(videoPath)
        val duration = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)
        retriever.release()
        return duration.orEmpty().toLongOrDefault(0)
    }
    private fun formatDuration(duration: Long): String {
        val minutes = TimeUnit.MILLISECONDS.toMinutes(duration)
        val seconds = TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%02d:%02d", minutes, seconds)
    }
}
