package dev.iconclad.videoeditor.editor

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import dev.iconclad.videoeditor.R
import dev.iconclad.videoeditor.trimmer.TrimmerActivity
import dev.iconclad.videoeditor.trimmer.VideoController
import dev.iconclad.videoeditor.trimmer.VideoControllerListener

class EditorActivity : AppCompatActivity() {
    private lateinit var _videoController: VideoController
    private lateinit var _playerView: PlayerView

    private lateinit var _player: ExoPlayer
    private lateinit var _mediaItem: MediaItem

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.statusBarColor = Color.TRANSPARENT

        setContentView(R.layout.ve_activity_editor)
        val videoPath = intent.getStringExtra("videoPath")

        _videoController = findViewById(R.id.videoController)
        _playerView = findViewById(R.id.exoplayerView)


        _playerView.useController = false
        _player = ExoPlayer.Builder(this).build()
        _playerView.player = _player
        _mediaItem = MediaItem.fromUri(videoPath!!)
        _player.setMediaItem(_mediaItem)
        _player.playWhenReady = true
        _player.prepare()

        _videoController.play()
        _videoController.setListener(object : VideoControllerListener {
            override fun onChanged(play: Boolean) {
                if (play) {
                    _player.play()
                } else {
                    _player.pause()
                }
            }
        })


    }

    companion object {
        fun start(context: Context, videoPath: String) {
            val intent = Intent(context, EditorActivity::class.java)
            intent.putExtra("videoPath", videoPath)
            context.startActivity(intent)
        }
    }

}