package vanphuc0497.job.mycv.ui.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.ActionMode
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.res.ResourcesCompat
import vanphuc0497.job.mycv.R


/**
 * Create by Nguyen Van Phuc on 6/5/20
 */
class CustomPinEntryEditText : AppCompatEditText {
    companion object {
        private const val XML_NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android"
        private const val OFFSET_BOTTOM_TEXT = 13F
        private const val DEFAULT_WIDTH_CURSOR = 2F
        private const val DEFAULT_WIDTH_STROKE = 2F
        private const val DEFAULT_SPACE_PIN = 24F // space between the lines
        private const val DEFAULT_LENGTH = 4
    }

    private lateinit var mTextWidths: FloatArray
    private var mCharSize = 0f
    private var mMaxLength = DEFAULT_LENGTH
    private var mOffsetBottomText = OFFSET_BOTTOM_TEXT
    private var mWidthCursor = DEFAULT_WIDTH_CURSOR
    private var mWidthStroke = DEFAULT_WIDTH_STROKE
    private var mSpacePin = DEFAULT_SPACE_PIN
    private val mLinesPaint by lazy {
        Paint()
    }
    private var mLineStroke = 3f
    private var mClickListener: OnClickListener? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context, attrs,
        defStyleAttr
    ) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet) {
        setBackgroundResource(0) //Clear line default for EditText
        convertNumberToPixels(context)
        getAttrs(attrs)
        /**
         * Hard code follow spec of project, you can custom if need.
         */
        isCursorVisible = false
        mTextWidths = FloatArray(mMaxLength)
        mLinesPaint.strokeWidth = mLineStroke
        paint.color = currentTextColor
        maxLines = 1
        isSingleLine = true
        super.setTextIsSelectable(false)
        inputType = EditorInfo.TYPE_CLASS_NUMBER or EditorInfo.TYPE_NUMBER_VARIATION_PASSWORD

        /**
         * Disable copy paste
         */
        super.setCustomSelectionActionModeCallback(object : ActionMode.Callback {
            override fun onPrepareActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onDestroyActionMode(mode: ActionMode) {}

            override fun onCreateActionMode(mode: ActionMode, menu: Menu): Boolean {
                return false
            }

            override fun onActionItemClicked(mode: ActionMode, item: MenuItem): Boolean {
                return false
            }
        })
        /**
         * When tapped, move cursor to end of text.
         */
        super.setOnClickListener { v ->
            setSelection(text?.length ?: 0)
            if (mClickListener != null) {
                mClickListener?.onClick(v)
            }
        }
    }

    override fun setOnClickListener(l: OnClickListener?) {
        mClickListener = l
    }

    override fun setCustomSelectionActionModeCallback(actionModeCallback: ActionMode.Callback) {
        throw RuntimeException("setCustomSelectionActionModeCallback() not supported.")
    }

    override fun onDraw(canvas: Canvas) {
        val availableWidth = width * 1F - paddingRight - paddingLeft
        mCharSize = if (mSpacePin < 0) {
            availableWidth / (mMaxLength * 2 - 1)
        } else {
            (availableWidth - mSpacePin - mSpacePin * (mMaxLength - 1)) / mMaxLength
        }
        val startX = paddingLeft
        canvas.drawPinLine(startX)
        canvas.drawPinText(startX)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            performClick()
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    private fun getAttrs(attrs: AttributeSet) {
        mMaxLength =
            attrs.getAttributeIntValue(XML_NAMESPACE_ANDROID, "maxLength", DEFAULT_LENGTH)
        context.obtainStyledAttributes(attrs, R.styleable.CustomPinEntryEditText).run {
            mSpacePin = getDimension(R.styleable.CustomPinEntryEditText_spacePin, 0F)
            recycle()
        }
    }

    private fun convertNumberToPixels(context: Context) {
        val multi = context.resources.displayMetrics.density
        mSpacePin *= multi
        mWidthStroke *= multi
        mOffsetBottomText *= multi
        mWidthCursor *= multi
        mLineStroke *= multi
    }

    private fun Paint.updateColorForLines(isPinNext: Boolean) {
        color = when {
            isFocused && isPinNext -> ResourcesCompat.getColor(
                resources,
                R.color.colorLightBlue,
                null
            )
            else -> ResourcesCompat.getColor(resources, R.color.colorWhiteGray, null)
        }
    }

    private fun Canvas.drawPinLine(firstStartX: Int) {
        var startX = firstStartX.toFloat()
        val bottom = height - paddingBottom
        for (i in 0 until mMaxLength) {
            text?.length?.let {
                mLinesPaint.updateColorForLines(it == i || it == mMaxLength && i == mMaxLength - 1)
            }
            drawLine(
                startX,
                bottom.toFloat(),
                startX + mCharSize,
                bottom.toFloat(),
                mLinesPaint
            )
            startX = getStartXOfNextPin(i, startX)
        }
    }

    private fun Canvas.drawPinText(firstStartX: Int) {
        var startX = firstStartX.toFloat()
        val textWidths = FloatArray(mMaxLength)
        val bottom = height - paddingBottom
        text?.let { textDraw ->
            paint.getTextWidths(textDraw, 0, textDraw.length, textWidths)
            for (i in 0..mMaxLength) {
                if ((text?.length ?: 0) > i) {
                    val middle = startX + mCharSize / 2
                    drawText(
                        textDraw, i, i + 1, middle - textWidths[i] / 2,
                        bottom.toFloat() - mOffsetBottomText,
                        paint
                    )
                }
                startX = getStartXOfNextPin(i, startX)
            }
        }
    }

    private fun getStartXOfNextPin(indexPin: Int, currentStartX: Float): Float =
        if (mSpacePin < 0) {
            currentStartX + (mCharSize * 2).toInt()
        } else {
            if (indexPin == (mMaxLength / 2) - 1) {
                currentStartX + (mCharSize + mSpacePin * 2).toInt()
            } else {
                currentStartX + (mCharSize + mSpacePin).toInt()
            }
        }
}
