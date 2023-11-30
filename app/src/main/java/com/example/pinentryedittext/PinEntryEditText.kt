package com.example.pinentryedittext

import android.content.Context
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import androidx.annotation.AttrRes
import androidx.annotation.DrawableRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat

class PinEntryEditText : AppCompatEditText {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        attrs?.let {
            initView(it)
        }
    }

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        attrs?.let {
            initView(it)
        }
    }

    companion object {
        private const val XML_NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android"
    }

    private var space: Float = 24f // pixel, 每格的間距
    private var numChars: Int = 4 // 字數
    private var editTextBackgroundId: Int = -1 // 每格的背景


    private var clickListener: OnClickListener? = null


    private fun initView(attrs: AttributeSet) {
        // 取得最多字數
        numChars = attrs.getAttributeIntValue(XML_NAMESPACE_ANDROID, "maxLength", 4)
        val typeArray = context.obtainStyledAttributes(attrs, R.styleable.PinEntryEditText)
        // 取得指定的背景
        editTextBackgroundId =
            typeArray.getResourceId(R.styleable.PinEntryEditText_editTextBackground, -1)
        // 取得指定間距
        space = typeArray.getDimension(R.styleable.PinEntryEditText_editTextSpace, 0f)
        typeArray.recycle()

        // 阻擋複製貼上的行為
        super.setCustomSelectionActionModeCallback(object : ActionMode.Callback {
            override fun onActionItemClicked(p0: ActionMode?, p1: MenuItem?): Boolean {
                return false
            }

            override fun onCreateActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return false
            }

            override fun onPrepareActionMode(p0: ActionMode?, p1: Menu?): Boolean {
                return false
            }

            override fun onDestroyActionMode(p0: ActionMode?) {
            }

        })

        super.setOnClickListener { view ->
            // 讓cursor永遠在最後
            setSelection(text?.length ?: 0)
            clickListener?.onClick(view)
        }

    }

    override fun setOnClickListener(l: OnClickListener?) {
        clickListener = l
    }

    // 阻擋複製貼上的行為
    override fun setCustomSelectionActionModeCallback(actionModeCallback: ActionMode.Callback?) {
        throw RuntimeException("setCustomSelectionActionModeCallback() is not supported")
    }

    override fun onDraw(canvas: Canvas?) {
        // 計算可用寬度
        val availableWidth = width - paddingRight - paddingLeft
        // 計算每格寬度
        val charSize = if (space < 0) {
            availableWidth / ((numChars.toFloat() * 2) - 1)
        } else {
            (availableWidth - (space * (numChars - 1))) / numChars
        }
        // 起始點
        var startX = paddingLeft.toFloat()
        // 可使用的最底位置
        val bottom = (height - paddingBottom).toFloat()

        canvas?.let {
            // 依序繪製每格樣式與文字
            for (i in 0 until numChars) {
                if (editTextBackgroundId == -1) { // 若沒提供background則繪製底線
                    canvas.drawLine(startX, bottom, startX + charSize, bottom, paint)
                } else {  // 若沒提供background則用drawable方式繪製
                    val drawable = ContextCompat.getDrawable(context, editTextBackgroundId)
                    drawable?.setBounds(
                        startX.toInt(),
                        paddingTop,
                        (startX + charSize).toInt(),
                        bottom.toInt()
                    )
                    drawable?.draw(canvas)
                }

                // 若有文字再執行
                text?.let {editable ->
                    if (editable.length > i) {
                        // 設定畫筆
                        val textLength: Int = editable.length
                        val textWidths: FloatArray = createTextWidths(textLength)
                        paint.getTextWidths(editable, 0, textLength, textWidths)
                        paint.color = currentTextColor

                        // 計算水平中間位置
                        val middleHorizontal: Float = startX + charSize / 2 - textWidths[0] / 2
                        // 計算垂直中間位置
                        val middleVertical = height.toFloat() / 2 - ((paint.descent() + paint.ascent()) / 2)
                        canvas.drawText(
                            editable,
                            i,
                            i + 1,
                            middleHorizontal,
                            middleVertical,
                            paint
                        )
                    }
                }

                // 計算下一格起始位置
                startX += if (space < 0) {
                    charSize * 2
                } else {
                    charSize + space
                }
            }
        }
    }

    fun clearText() {
        setText("")
    }

    fun setEditBlockBackground(@DrawableRes resourcesId: Int) {
        editTextBackgroundId = resourcesId
        invalidate()
    }

    private fun createTextWidths(textLength: Int): FloatArray {
        return FloatArray(textLength)
    }
}