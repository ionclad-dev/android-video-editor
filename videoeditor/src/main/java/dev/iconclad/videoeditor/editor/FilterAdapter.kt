package dev.iconclad.videoeditor.editor

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.ColorMatrixColorFilter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import dev.iconclad.videoeditor.R
import dev.iconclad.videoeditor.util.ffmpeg.FilterModel
import dev.iconclad.videoeditor.util.view.SquareImageView
import java.io.File


class FilterAdapter(val filePath:String) : RecyclerView.Adapter<FilterAdapter.MyViewHolder>() {

    private var _data: List<FilterModel> = mutableListOf()
    private var _onItemClick: ((String) -> Unit)? = null
    fun setItemClickListener(onItemClick: ((String) -> Unit)?) {
        _onItemClick = onItemClick
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(data:  List<FilterModel>) {
        _data = data
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilterAdapter.MyViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.ve_item_filter, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: FilterAdapter.MyViewHolder, position: Int) {
        val item = _data[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return _data.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item:  FilterModel) {
            itemView.setOnClickListener {
                _onItemClick?.invoke(item.videoFilter)
            }
            val imageView = itemView.findViewById<SquareImageView>(R.id.imageView)
            imageView.colorFilter = ColorMatrixColorFilter(item.imageFilter)
            itemView.findViewById<TextView>(R.id.titleView).text = item.name
            imageView.load(File(filePath))

        }
    }
}