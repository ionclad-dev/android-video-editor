package dev.iconclad.videoeditor.gallery

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.provider.MediaStore
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.Coil
import coil.ImageLoader
import coil.decode.VideoFrameDecoder
import dev.iconclad.videoeditor.R
import dev.iconclad.videoeditor.trimmer.TrimmerActivity

class VideoGalleryActivity : AppCompatActivity() {
    private val _adapter = VideoGalleryGridAdapter()
    private val REQUEST_PERMISSION = 100
    private lateinit var _videoGridView: RecyclerView
    private var _videoList: MutableList<String> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )

        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.ve_activity_video_gallery)

        val imageLoader = ImageLoader.Builder(this)
            .components {
                add(VideoFrameDecoder.Factory())
            }
            .build()
        Coil.setImageLoader(imageLoader)

        findViewById<ImageView>(R.id.closeActionButton).setOnClickListener {
            finish()
        }
        _videoGridView = findViewById(R.id.videoGridView)


        if (checkPermission()) {
            loadVideoList()
        } else {
            requestPermission()
        }

        _adapter.setItemClickListener {
            TrimmerActivity.start(this, it)
        }
        _videoGridView.adapter = _adapter
    }

    private fun checkPermission(): Boolean {
        val result =
            ContextCompat.checkSelfPermission(this, Manifest.permission.READ_MEDIA_VIDEO)
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
            REQUEST_PERMISSION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
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
        val cursorItem = contentResolver.query(videoUri, null, null, null, null)

        if (cursorItem != null && cursorItem.moveToFirst()) {
            val columnIndex = cursorItem.getColumnIndex(MediaStore.Video.Media.DATA)
            do {
                val videoPath = cursorItem.getString(columnIndex)
                _videoList.add(videoPath)
            } while (cursorItem.moveToNext())

            cursorItem.close()
            _adapter.setData(_videoList)

        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, VideoGalleryActivity::class.java)
            context.startActivity(intent)
        }
    }

}