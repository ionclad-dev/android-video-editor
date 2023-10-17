package dev.iconclad.videoeditor.trimmer

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import dev.iconclad.videoeditor.R
import kotlin.math.ceil
import kotlin.math.min

interface VideoControllerListener {
    fun onChanged(play: Boolean)
}

class VideoController : View {
    private var playIcon: Drawable? = null
    private var pauseIcon: Drawable? = null
    private var isPlaying = false
    private var _isVisible = true
    private val handler = Handler(Looper.getMainLooper())
    private val visibilityDuration = 1000L
    private var _listener: VideoControllerListener? = null
    fun setListener(listener: VideoControllerListener) {
        _listener = listener

    }

    private var density = 1f
    private fun dp(value: Float): Int {
        return if (value == 0f) {
            0
        } else ceil((density * value)).toInt()
    }


    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )


    init {
        density = context.resources.displayMetrics.density
        playIcon = ContextCompat.getDrawable(context, R.drawable.ve_play)!!
        pauseIcon = ContextCompat.getDrawable(context, R.drawable.ve_pause)!!
        playAnim()
        setOnClickListener {
            togglePlayPause()
        }
    }


    private fun togglePlayPause() {
        isPlaying = !isPlaying
        _listener?.onChanged(isPlaying)
        playAnim()
    }

    fun play() {
        isPlaying = true
        _listener?.onChanged(true)
        playAnim()
    }

    fun pause() {
        isPlaying = false
        _listener?.onChanged(false)
        playAnim()
    }

    private fun playAnim() {
        _isVisible = true
        invalidate()
        handler.removeCallbacksAndMessages(null)
        handler.postDelayed({
            _isVisible = false
            invalidate()
        }, visibilityDuration)

    }


    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        // Arka planı temizle
        // canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR)
        if (_isVisible) {



            val iconX = (width / 2)
            val iconY = (height / 2)



            // Simgeyi çiz
            val icon: Drawable = if (isPlaying) pauseIcon!! else playIcon!!
            val drawableSize = dp(56f)
            val drawableLeft = iconX - drawableSize / 2
            val drawableTop = iconY - drawableSize / 2
            val drawableRight = iconX + drawableSize / 2
            val drawableBottom = iconY + drawableSize / 2
            icon.bounds = Rect(drawableLeft, drawableTop, drawableRight, drawableBottom)
            icon.draw(canvas)
        }
    }
}