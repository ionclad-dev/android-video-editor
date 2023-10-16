package dev.iconclad.videoeditor.gallery

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.GridView
import android.widget.ImageView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import coil.Coil
import coil.ImageLoader
import coil.decode.VideoFrameDecoder
import dev.iconclad.videoeditor.R
import dev.iconclad.videoeditor.trimmer.TrimmerActivity

class VideoGalleryActivity : AppCompatActivity() {
    private val REQUEST_PERMISSION = 100
    private lateinit var videoGridView: GridView
    private lateinit var videoList: ArrayList<String>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.activity_video_gallery)

        val imageLoader = ImageLoader.Builder(this)
            .components {
                add(VideoFrameDecoder.Factory())
            }
            .build()
        Coil.setImageLoader(imageLoader)

findViewById<ImageView>(R.id.closeActionButton).setOnClickListener {
    finish()
}
        videoGridView = findViewById(R.id.videoGridView)
        videoList = ArrayList()

        if (checkPermission()) {
            loadVideoList()
        } else {
            requestPermission()
        }

        videoGridView.onItemClickListener =
            OnItemClickListener { _, _, position, _ ->
                TrimmerActivity.start(this,videoList[position])
            }
    }

    private fun checkPermission(): Boolean {
        val result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), REQUEST_PERMISSION)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadVideoList()
            } else {
                // İzin verilmedi, bu durumu kullanıcıya bildirebilirsiniz.
            }
        }
    }

    private fun loadVideoList() {
        val contentResolver = contentResolver
        val videoUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI
        val cursor = contentResolver.query(videoUri, null, null, null, null)

        if (cursor != null && cursor.moveToFirst()) {
            val columnIndex = cursor.getColumnIndex(MediaStore.Video.Media.DATA)
            do {
                val videoPath = cursor.getString(columnIndex)
                videoList.add(videoPath)
            } while (cursor.moveToNext())

            cursor.close()

            val adapter = VideoGalleryGridAdapter(this, videoList)
            videoGridView.adapter = adapter
        }
    }
    companion object {
        fun start(context: Context) {
            val intent = Intent(context, VideoGalleryActivity::class.java)
            context.startActivity(intent)
        }
    }

}