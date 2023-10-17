package dev.iconclad.videoeditor.trimmer


import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.arthenica.ffmpegkit.FFmpegKit
import com.arthenica.ffmpegkit.ReturnCode
import dev.iconclad.videoeditor.R
import dev.iconclad.videoeditor.editor.EditorActivity
import dev.iconclad.videoeditor.util.ffmpeg.FFmpegCommandBuilder
import dev.iconclad.videotimelineview.VideoTimelineView
import dev.iconclad.videotimelineview.VideoTimelineViewListener

class TrimmerActivity : AppCompatActivity(), Player.Listener, VideoTimelineViewListener {
    private lateinit var _videoController: VideoController
    private lateinit var _timelineView: VideoTimelineView

    private lateinit var _playerView: PlayerView

    private lateinit var _player: ExoPlayer
    private lateinit var _mediaItem: MediaItem

    private var _startPosition: Float = 0f
    private var _endPosition: Float = 1f
    private var _isDragging = false

    private var alertDialog: AlertDialog? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.statusBarColor = Color.TRANSPARENT
        setContentView(R.layout.ve_activity_trimmer)
        val videoPath = intent.getStringExtra("videoPath")
        findViewById<ImageView>(R.id.backActionButton).setOnClickListener {
            finish()
        }

        _videoController = findViewById(R.id.videoController)
        _timelineView = findViewById(R.id.timelineView2)
        _playerView = findViewById(R.id.exoplayerView)


        _playerView.useController = false
        _player = ExoPlayer.Builder(this).build()
        _playerView.player = _player
        _mediaItem = MediaItem.fromUri(videoPath!!)
        _player.setMediaItem(_mediaItem)
        _player.playWhenReady = true
        _player.prepare()
        _player.addListener(this)
        initializeTimeBar(videoPath)

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



        findViewById<TextView>(R.id.nextButton).setOnClickListener {
            _player.pause()
            showProcessingDialog()
            val outputVideoPath = VideoFileUtil(this).createOutputVideoPath()

           val builder= FFmpegCommandBuilder()
                .setInputPath(videoPath)
                .setStartTime((_player.duration * _startPosition).toLong())
                .setDuration((_player.duration * _endPosition).toLong() - (_player.duration * _startPosition).toLong())
                .setOutputPath(outputVideoPath)


            builder.execute {
                alertDialog?.dismiss()
                if (ReturnCode.isSuccess(it.returnCode)) {

                    EditorActivity.start(this,outputVideoPath)
                    // SUCCESS
                } else if (ReturnCode.isCancel(it.returnCode)) {
                    // CANCEL
                } else {
                    // FAILURE
                    Log.d(
                        "TAG 2",
                        java.lang.String.format(
                            "Command failed with state %s and rc %s.%s",
                            it.state,
                            it.returnCode,
                            it.failStackTrace
                        )
                    )
                }
            }
          /*  val cmd = arrayOf(
                "-i",
                videoPath,
                "-ss",
                TrimmerUtils.formatMilliseconds((_player.duration * _startPosition).toLong()),
                "-t",
                TrimmerUtils.formatMilliseconds((_player.duration * _endPosition).toLong() - (_player.duration * _startPosition).toLong()),
                "-c",
                "copy",
                outputVideoPath
            )

            Log.i("cmd", cmd.joinToString())

            FFmpegKit.executeWithArgumentsAsync(cmd) { session ->
                alertDialog?.dismiss()
                if (ReturnCode.isSuccess(session.returnCode)) {

                    EditorActivity.start(this,outputVideoPath)
                    // SUCCESS
                } else if (ReturnCode.isCancel(session.returnCode)) {
                    // CANCEL
                } else {
                    // FAILURE
                    Log.d(
                        "TAG 2",
                        java.lang.String.format(
                            "Command failed with state %s and rc %s.%s",
                            session.state,
                            session.returnCode,
                            session.failStackTrace
                        )
                    )
                }
            }
*/
        }
    }

    private fun initializeTimeBar(videoPath: String) {

        _timelineView.setVideoPath(videoPath)
        _timelineView.setListener(this)

        val handler = Handler(Looper.getMainLooper())

        val updateProgressAction: Runnable = object : Runnable {
            override fun run() {

                if (_player.currentPosition != 0.toLong()) {

                    if (_player.currentPosition > (_endPosition * _player.duration)) {

                        _player.seekTo((_player.duration * _startPosition).toLong())
                    } else if (_player.currentPosition < (_player.duration * _startPosition).toLong()) {

                        _player.seekTo((_player.duration * _startPosition).toLong())
                    } else {
                        val newProgress: Float =
                            (_player.currentPosition.toFloat() / _player.duration.toFloat())

                        _timelineView.setProgress(newProgress)
                    }

                }
                handler.postDelayed(this, 100)
            }
        }

        handler.post(updateProgressAction)
    }

    override fun onPause() {
        _player.pause()
        super.onPause()
    }

    override fun onDestroy() {
        _player.release()
        super.onDestroy()
    }

    companion object {
        fun start(context: Context, videoPath: String) {
            val intent = Intent(context, TrimmerActivity::class.java)
            intent.putExtra("videoPath", videoPath)
            context.startActivity(intent)
        }
    }


    override fun onLeftProgressChanged(progress: Float) {

        _startPosition = progress
    }

    override fun onRightProgressChanged(progress: Float) {

        _endPosition = progress
    }

    override fun onDurationChanged(duration: Long) {

    }

    override fun onPlayProgressChanged(progress: Float) {

        if (_isDragging) {

            val newPosition: Float = _player.duration.toFloat() * progress

            _player.seekTo(newPosition.toLong())
        }
    }

    override fun onDraggingStateChanged(isDragging: Boolean) {

        _isDragging = isDragging
    }


    private fun showProcessingDialog() {
        try {
            val alertDialogBuilder = AlertDialog.Builder(this)
            alertDialogBuilder.setCancelable(false)
            alertDialogBuilder.setTitle("Uyarı")
            alertDialogBuilder.setMessage("Bu bir temel uyarı iletişim kutusu örneğidir.")


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