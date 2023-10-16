package dev.iconclad.videoeditor.util.view

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Color
import android.util.AttributeSet
import android.view.Gravity
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import dev.iconclad.videoeditor.R


class TabItemView : LinearLayout {
    private lateinit var tabImage: ImageView
    private lateinit var tabText: TextView


    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        orientation = VERTICAL
        gravity = Gravity.CENTER

        tabImage = ImageView(context)
        val imageParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        tabImage.layoutParams = imageParams
        addView(tabImage)

        tabText = TextView(context)
        val textParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        tabText.layoutParams = textParams
        addView(tabText)

        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.TabItemView)
            val tabIconResId = a.getResourceId(R.styleable.TabItemView_tabIcon, 0)
            val tabTextValue = a.getString(R.styleable.TabItemView_tabText)
            val color = a.getColor(dev.iconclad.videotimelineview.R.styleable.VideoTimelineView_color, Color.BLACK)
            a.recycle()

            if (tabIconResId != 0) {
                tabImage.setImageResource(tabIconResId)
            }
            tabText.text = tabTextValue
            tabText.setTextColor(color)

        }
    }

    fun setTabImage(bitmap: Bitmap?) {
        tabImage.setImageBitmap(bitmap)
    }

    fun setTabText(text: String?) {
        tabText.text = text
    }
}
