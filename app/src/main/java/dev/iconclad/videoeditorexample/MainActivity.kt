package dev.iconclad.videoeditorexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import dev.iconclad.videoeditor.camera.CameraActivity
import dev.iconclad.videoeditor.gallery.VideoGalleryActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        findViewById<Button>(R.id.cameraAction).setOnClickListener {
            CameraActivity.start(this)
        }

        findViewById<Button>(R.id.videoGaleryAction).setOnClickListener {
            VideoGalleryActivity.start(this)
        }
    }
}