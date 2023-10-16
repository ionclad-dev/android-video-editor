package dev.iconclad.videoeditor.gallery

import android.annotation.SuppressLint
import android.media.MediaMetadataRetriever
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import dev.iconclad.videoeditor.R
import dev.iconclad.videoeditor.util.view.SquareImageView
import okhttp3.internal.toLongOrDefault
import java.io.File
import java.util.concurrent.TimeUnit


class VideoGalleryGridAdapter : RecyclerView.Adapter<VideoGalleryGridAdapter.MyViewHolder>() {
    private var _data: MutableList<String> = mutableListOf()
    private var _onItemClick: ((String) -> Unit)? = null
    fun setItemClickListener(onItemClick: ((String) -> Unit)?) {
        _onItemClick = onItemClick
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data: MutableList<String>) {
        _data = data
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_video_gallery, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = _data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return _data.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: String) {
            itemView.setOnClickListener {
                _onItemClick?.invoke(item)
            }
            val imageView = itemView.findViewById<SquareImageView>(R.id.videoThumbnailView)
            val durationText = itemView.findViewById<TextView>(R.id.videoDuration)



            imageView.load(File(item))
            val duration = getVideoDuration(item)
            val formattedDuration = formatDuration(duration)

            durationText.text = formattedDuration

        }
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
        val seconds =
            TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(minutes)
        return String.format("%02d:%02d", minutes, seconds)
    }
}