package dev.iconclad.videoeditor.editor

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.MediaSource
import androidx.media3.ui.PlayerView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.Log
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dev.iconclad.videoeditor.R
import dev.iconclad.videoeditor.trimmer.VideoController
import dev.iconclad.videoeditor.trimmer.VideoControllerListener
import dev.iconclad.videoeditor.trimmer.VideoFileUtil
import dev.iconclad.videoeditor.util.ffmpeg.FFmpegCommandBuilder
import dev.iconclad.videoeditor.util.ffmpeg.efectList
import dev.iconclad.videoeditor.util.view.TabItemView


class EditorActivity : AppCompatActivity() {
    private lateinit var _videoController: VideoController
    private lateinit var _playerView: PlayerView
    private lateinit var _tabBar: LinearLayout
    private  var _player: ExoPlayer? = null
    private lateinit var _videoPath: String
    private lateinit var _filterVideoPath: String
    private lateinit var _bottomSheetBehavior: BottomSheetBehavior<ConstraintLayout>
    private var alertDialog: AlertDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.statusBarColor = Color.TRANSPARENT

        setContentView(R.layout.ve_activity_editor)
        _videoPath = intent.getStringExtra("videoPath")!!
        _filterVideoPath = VideoFileUtil(this).createOutputVideoPath()
        _filterVideoPath = _videoPath
        init()
        _player = ExoPlayer.Builder(this)
            .build()
        _playerView.player = _player
        playerInit()
        filterAdapterInit()


        _videoController.play()
        _videoController.setListener(object : VideoControllerListener {
            override fun onChanged(play: Boolean) {
                if (play) {
                    _player?.play()
                } else {
                    _player?.pause()
                }
            }
        })

        _bottomSheetBehavior = BottomSheetBehavior.from(findViewById(R.id.bottom_sheet));
        _bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
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
        findViewById<TextView>(R.id.saveFilterButton).setOnClickListener {
            _bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun playerInit() {
        runOnUiThread {
            _playerView.useController = false


            val mediaItem = MediaItem.fromUri(_filterVideoPath)

            _player?.setMediaItem(mediaItem)
            _player?.prepare()
            _player?.play()
        }

    }

    private fun textAction() {

    }

    private fun voiceAction() {

    }

    private fun timeAction() {

    }

    private fun filterAction() {
        _bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
    }

    private fun filterAdapterInit() {
        val adapter = FilterAdapter(_videoPath)
        val recycleView = findViewById<RecyclerView>(R.id.filterRecycleView)
        recycleView.adapter = adapter
        val layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycleView.layoutManager = layoutManager
        adapter.setData(efectList)
        adapter.setItemClickListener {

            if(it == null){
                _filterVideoPath = _videoPath
                playerInit()
            }else{
                showProcessingDialog()
                _filterVideoPath = VideoFileUtil(this).createOutputVideoPath()

                FFmpegCommandBuilder()
                    .setInputPath(_videoPath)
                    .setColorChannelMixer(it)
                    .setOutputPathCA(_filterVideoPath).execute {
                        alertDialog?.dismiss()
                        playerInit()
                    }
            }


        }
    }

    override fun onPause() {
        _player?.pause()
        super.onPause()
    }

    override fun onDestroy() {
        _player?.release()
        super.onDestroy()
    }

    companion object {
        fun start(context: Context, videoPath: String) {
            println(videoPath)
            val intent = Intent(context, EditorActivity::class.java)
            intent.putExtra("videoPath", videoPath)
            context.startActivity(intent)
        }
    }

    private fun showProcessingDialog() {
        try {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.setTitle("İşleniyor")
            alertDialogBuilder.setMessage("Video İşleniyor lütfen bekleyin.")


            alertDialogBuilder.setPositiveButton("Tamam") { dialog, which ->

                dialog.dismiss()
                FFmpegKit.cancel()
            }
            alertDialog = alertDialogBuilder.create()
            alertDialog!!.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}