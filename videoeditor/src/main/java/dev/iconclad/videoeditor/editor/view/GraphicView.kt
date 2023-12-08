package dev.iconclad.videoeditor.editor.view

import android.app.Activity
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GraphicView(context: Context, attrs: AttributeSet?) : FrameLayout(context, attrs) {
    private var selectedView: View? = null
    private var xDelta = 0
    private var yDelta = 0

    private var scaleGestureDetector: ScaleGestureDetector

    init {
        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.rawX.toInt()
        val y = event.rawY.toInt()

        scaleGestureDetector.onTouchEvent(event) // Yakınlaştırma/uzaklaştırma hareketlerini işle

        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                for (i in 0 until childCount) {
                    val view = getChildAt(i)
                    if (x >= view.left && x <= view.right &&
                        y >= view.top && y <= view.bottom
                    ) {
                        xDelta = x - view.left
                        yDelta = y - view.top
                        selectedView = view
                        bringViewToFront(selectedView)
                        longPressHandler.postDelayed(longPressRunnable, 1000)
                        break
                    }
                }
            }
            MotionEvent.ACTION_MOVE -> {
                selectedView?.let {
                    val layoutParams = it.layoutParams as FrameLayout.LayoutParams
                    layoutParams.leftMargin = x - xDelta
                    layoutParams.topMargin = y - yDelta
                    it.layoutParams = layoutParams
                }
            }
            MotionEvent.ACTION_UP -> {
                selectedView = null
                longPressHandler.removeCallbacks(longPressRunnable)
            }
        }
        invalidate()
        return true
    }
    private var longPressHandler: Handler = Handler(Looper.getMainLooper())
    private var isLongPressing = false

    private val longPressRunnable = Runnable {


               TextEditorDialogFragment.show(context as AppCompatActivity,"")


    }
    inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            selectedView?.let {
                if (it is ImageView) {
                    val scale = it.scaleX * detector.scaleFactor
                    it.scaleX = scale
                    it.scaleY = scale
                }
                if(it is TextView){
                    val currentSize = it.textSize
                    val newSize = currentSize * detector.scaleFactor
                    it.textSize = newSize

                }
            }
            return true
        }
    }

    private fun bringViewToFront(view: View?) {
        view?.let {
            val currentIndex = indexOfChild(it)
            if (currentIndex != childCount - 1) {
                removeViewAt(currentIndex)
                addView(it)
            }
        }
    }
}
