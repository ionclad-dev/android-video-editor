package dev.iconclad.videoeditorexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import dev.iconclad.videoeditor.camera.CameraActivity
import dev.iconclad.videoeditor.editor.EditorActivity
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

            //  EditorActivity.start(this,"/storage/emulated/0/Android/data/dev.iconclad.videoeditorexample/files/Movies/VIDEO_20231017_180006.mp4")
        }
    }
}