package dev.iconclad.videoeditorexample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dev.iconclad.videoeditor.gallery.VideoGalleryActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        VideoGalleryActivity.start(this)
    }
}