package dev.iconclad.videoeditor.editor

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dev.iconclad.videoeditor.R
import dev.iconclad.videoeditor.trimmer.VideoController
import dev.iconclad.videoeditor.trimmer.VideoControllerListener
import dev.iconclad.videoeditor.util.ffmpeg.efectList
import dev.iconclad.videoeditor.util.view.TabItemView


class EditorActivity : AppCompatActivity() {
    private lateinit var _videoController: VideoController
    private lateinit var _playerView: PlayerView
    private lateinit var _tabBar: LinearLayout
    private lateinit var _player: ExoPlayer
    private lateinit var _mediaItem: MediaItem
    private lateinit var videoPath: String
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.statusBarColor = Color.TRANSPARENT

        setContentView(R.layout.ve_activity_editor)
        videoPath = intent.getStringExtra("videoPath")!!
        init()
        playerInit()
        filterAdapterInit(videoPath)

        val bottomSheetLayout = findViewById<ConstraintLayout>(R.id.bottom_sheet)
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetLayout);
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN;
    }

    private fun init() {
        _videoController = findViewById(R.id.videoController)
        _playerView = findViewById(R.id.exoplayerView)
        _tabBar = findViewById(R.id.tabBar)

        findViewById<TabItemView>(R.id.textTab).setOnClickListener {
            textAction()
        }

        findViewById<TabItemView>(R.id.voiceTab).setOnClickListener {
            voiceAction()
        }

        findViewById<TabItemView>(R.id.timeTab).setOnClickListener {
            timeAction()
        }

        findViewById<TabItemView>(R.id.filterTab).setOnClickListener {
            filterAction()
        }
    }

    private fun playerInit() {
        _playerView.useController = false
        _player = ExoPlayer.Builder(this).build()
        _playerView.player = _player
        _mediaItem = MediaItem.fromUri(videoPath)
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

    private fun textAction() {

    }

    private fun voiceAction() {

    }

    private fun timeAction() {

    }

    private fun filterAction() {
        if (bottomSheetBehavior.state != BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

        } else {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);

        }
    }

    private fun filterAdapterInit(videoPath: String) {
        val adapter = FilterAdapter(videoPath)
        val recycleView = findViewById<RecyclerView>(R.id.filterRecycleView)
        recycleView.adapter = adapter
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycleView.layoutManager = layoutManager
        adapter.setData(efectList)
    }

    companion object {
        fun start(context: Context, videoPath: String) {
            val intent = Intent(context, EditorActivity::class.java)
            intent.putExtra("videoPath", videoPath)
            context.startActivity(intent)
        }
    }

}