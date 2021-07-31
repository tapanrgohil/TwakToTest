package com.tapan.twaktotest.ui.custom

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.setPadding
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexWrap
import com.google.android.flexbox.FlexboxLayout
import com.tapan.twaktotest.R
import com.tapan.twaktotest.util.dp2Pixel

/**
 * TODO: document your custom view class.
 */
class HorizontalLabelValueView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FlexboxLayout(context, attrs, defStyleAttr) {

    var label: String? = ""
        set(value) {
            field = value
            findViewById<TextView>(R.id.tvLabel)?.text = value?.plus(" :") ?: "-"
        }
    var value: String? = ""
        set(value) {
            field = value
            findViewById<TextView>(R.id.tvValue)?.text = value ?: "-"
        }

    var labelColor: Int = ContextCompat.getColor(context, R.color.black)
        set(value) {
            field = value
            findViewById<TextView>(R.id.tvLabel)?.setTextColor(value)
        }

    var valueColor: Int = ContextCompat.getColor(context, R.color.black)
        set(value) {
            field = value
            findViewById<TextView>(R.id.tvValue)?.setTextColor(value)
        }

    init {
        flexDirection = FlexDirection.ROW
        flexWrap = FlexWrap.WRAP
        setPadding(context.dp2Pixel(4).toInt())
        View.inflate(context, R.layout.horizontal_lable_value_view, this)
        val typedArray: TypedArray? = getContext().theme.obtainStyledAttributes(
            attrs,
            R.styleable.LabelValueView,
            0, 0
        )
        label = typedArray?.getString(R.styleable.LabelValueView_label) ?: ""
        value = typedArray?.getString(R.styleable.LabelValueView_value) ?: ""
        findViewById<TextView>(R.id.tvLabel)?.text = label
        findViewById<TextView>(R.id.tvValue)?.text = value

        typedArray?.getColor(
            R.styleable.LabelValueView_labelColor, -1
        )?.let {
            if(it>-1)
            labelColor = it
        }
        typedArray?.getColor(
            R.styleable.LabelValueView_valueColor, -1
        )?.let {
            if(it>-1)
            valueColor = it
        }

        findViewById<TextView>(R.id.tvLabel)?.text = label.plus(" :")
        findViewById<TextView>(R.id.tvValue)?.text = value
    }
}