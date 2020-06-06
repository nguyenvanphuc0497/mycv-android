package vanphuc0497.job.mycv.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.*
import androidx.appcompat.widget.AppCompatEditText
import io.reactivex.Observable
import vanphuc0497.job.mycv.R
import vanphuc0497.job.mycv.extension.observeOnUiThread
import java.util.concurrent.TimeUnit

/**
 * Create by Nguyen Van Phuc on 6/5/20
 */
class CustomPinEntryEditText : AppCompatEditText {
    companion object {
        private const val XML_NAMESPACE_ANDROID = "http://schemas.android.com/apk/res/android"
        private const val DELAY = 700L // 0.7 second
        private const val OFFSET_BOTTOM_TEXT = 2F
        private const val DEFAULT_WIDTH_CURSOR = 2F
        private const val DEFAULT_WIDTH_STROKE = 2F
        private const val DEFAULT_SPACE_PIN = 24F // space between the lines
        private const val DEFAULT_LENGTH = 4
        private const val DEFAULT_MARK_PASSWORD = "*"
    }

    private lateinit var mTextWidths: FloatArray
    private var mCharSize = 0f
    private var mNumChars = 4f
    private var mMaxLength = DEFAULT_LENGTH
    private var mColorStroke = 0
    private var mColorBackGroundPin = 0
    private var mColorBackGroundPinError = 0
    private var mColorCursor = 0
    private var mOffsetBottomText = OFFSET_BOTTOM_TEXT
    private var mMarkPassword = DEFAULT_MARK_PASSWORD
    private var mWidthCursor = DEFAULT_WIDTH_CURSOR
    private var mWidthStroke = DEFAULT_WIDTH_STROKE
    private var mSpacePin = DEFAULT_SPACE_PIN
    private var mRadiusBackGround = 0f
    private var isShowCursor = false
    private var isCursorBlink = false
    private var isErrorPassword = false

    private var mRectCursor = RectF()
    private var mClickListener: View.OnClickListener? = null

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
        val multi = context.resources.displayMetrics.density
        setBackgroundResource(0)
        //convert to pixels for our density
        mSpacePin *= multi
        mWidthStroke *= multi
        mOffsetBottomText *= multi
        mWidthCursor *= multi

        mMaxLength = attrs.getAttributeIntValue(XML_NAMESPACE_ANDROID, "maxLength", DEFAULT_LENGTH)
        mNumChars = mMaxLength.toFloat()
        mTextWidths = FloatArray(mMaxLength)
        getAttrs(attrs)

        mLineStroke *= multi;
        mLinesPaint.strokeWidth = mLineStroke;

        //Disable copy paste
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
        // When tapped, move cursor to end of text.
        super.setOnClickListener { v ->
            setSelection(text?.length ?: 0)
            if (mClickListener != null) {
                mClickListener?.onClick(v)
            }
        }
    }

    override fun setOnClickListener(l: View.OnClickListener?) {
        mClickListener = l
    }

    override fun setCustomSelectionActionModeCallback(actionModeCallback: ActionMode.Callback) {
        throw RuntimeException("setCustomSelectionActionModeCallback() not supported.")
    }

    override fun onDraw(canvas: Canvas) {
        // Calculator space between pins
        val availableWidth = width - paddingRight - paddingLeft
        mCharSize = if (mSpacePin < 0) {
            availableWidth / (mNumChars * 2 - 1)
        } else {
            (availableWidth - mSpacePin * (mNumChars - 1)) / mNumChars
        }

        var startX = paddingLeft
        val bottom = height - paddingBottom


        //Set position cursor
        mRectCursor.let {
            it.top = height - bottom.toFloat()
            it.left = startX.toFloat() + mCharSize / 2
            it.right = it.left + mWidthCursor
            it.bottom = height.toFloat() - paddingBottom
        }

        canvas.drawPinLine(startX)
        canvas.drawPinText(startX)



        if (isShowCursor) {
            if (isCursorBlink) {
                paint.color = Color.TRANSPARENT
            } else {
                paint.color = mColorCursor
            }
            isCursorBlink = !isCursorBlink
//            canvas.drawRect(mRectCursor, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.action == MotionEvent.ACTION_DOWN) {
            performClick()
            startBlinkCursor()
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }

    override fun onFocusChanged(focused: Boolean, direction: Int, previouslyFocusedRect: Rect?) {
        super.onFocusChanged(focused, direction, previouslyFocusedRect)
        if (!focused) {
            stopBlinkCursor()
        }
    }

    internal fun showError(isClearText: Boolean = true) {
        if (isClearText) {
            setText("")
        }
        if (!isErrorPassword) {
            isErrorPassword = true
            invalidate()
        }
    }

    internal fun hideError() {
        if (isErrorPassword) {
            isErrorPassword = false
            invalidate()
        }
    }

    internal fun isEnterFullPin() = mMaxLength == text?.length

    private fun getAttrs(attrs: AttributeSet) {
        context.obtainStyledAttributes(attrs, R.styleable.CustomPinEntryEditText).run {
            mColorStroke = getColor(R.styleable.CustomPinEntryEditText_colorStroke, 0)
            mColorBackGroundPin = getColor(R.styleable.CustomPinEntryEditText_colorBackgroundPin, 0)
            mColorBackGroundPinError =
                getColor(R.styleable.CustomPinEntryEditText_colorBackGroundPinError, 0)
            mColorCursor = getColor(R.styleable.CustomPinEntryEditText_colorCursor, 0)
            mMarkPassword = getString(R.styleable.CustomPinEntryEditText_markPassword)
                ?: DEFAULT_MARK_PASSWORD
            mRadiusBackGround =
                getDimension(R.styleable.CustomPinEntryEditText_radiusBackground, 0F)
            mSpacePin = getDimension(R.styleable.CustomPinEntryEditText_spacePin, 0F)
            recycle()
        }
    }

    @SuppressLint("CheckResult")
    private fun startBlinkCursor() {
        if (!isShowCursor) {
            isShowCursor = true
            Observable.interval(DELAY, TimeUnit.MILLISECONDS)
                .takeWhile { isShowCursor }
                .observeOnUiThread()
                .subscribe {
                    invalidate()
                }
        }
    }

    private fun stopBlinkCursor() {
        isShowCursor = false
    }

    private val mLinesPaint = Paint()
    private var mLineStroke = 3f //1dp by default

//    private val mColorStates = object : ColorStateList(
//        arrayOf(
//            intArrayOf(), intArrayOf(android.R.attr.state_selected),
//            intArrayOf(), intArrayOf(android.R.attr.state_focused),
//            intArrayOf(), intArrayOf(-android.R.attr.state_focused)
//        ), intArrayOf(
//            Color.GREEN,
//            Color.BLACK,
//            Color.GRAY
//        )
//    )
//
//    private fun getColorForState(vararg states: Int): Int {
//        return mColorStates.getColorForState(states, Color.GRAY)
//    }

    private fun Paint.updateColorForLines(next: Boolean) {
        color = when {
            isFocused && next -> Color.RED
            else -> currentTextColor
        }
    }

    private fun Canvas.drawPinLine(firstStartX: Int) {
        var startX = firstStartX.toFloat()
        for (i in 0..mMaxLength) {

            mLinesPaint.run {
                updateColorForLines(i == text?.length ?: 0)
                this@drawPinLine.drawLine(
                    startX.toFloat(),
                    bottom.toFloat(),
                    startX + mCharSize,
                    bottom.toFloat(),
                    this
                )
            }
            Log.e("drawLine", " s: $startX, stop: ${startX + mCharSize}")
            startX = getStartXOfNextText(startX)
        }
    }

    private fun Canvas.drawPinText(firstStartX: Int) {
        var startX = firstStartX.toFloat()
        val textDraw = text ?: ""
        val textWidths = FloatArray(mMaxLength)
        paint.getTextWidths(textDraw, 0, textDraw.length, textWidths)
        Log.e("xxx", " mTextWidths: ${textWidths.contentToString()}")
        for (i in 0..mNumChars.toInt()) {
            if ((text?.length ?: 0) > i) {
                val middle = startX + mCharSize / 2
                paint.run {
                    color = currentTextColor
                    strokeWidth = 0F
                    this@drawPinText.drawText(
                        textDraw, i, i + 1, middle - textWidths[i] / 2,
                        bottom.toFloat() - mOffsetBottomText,
                        this
                    )
                }
            }

            // Calculator startX of next text
            startX = getStartXOfNextText(startX)
        }
    }

    private fun getStartXOfNextText(currentStartX: Float): Float = if (mSpacePin < 0) {
        currentStartX + (mCharSize * 2).toInt()
    } else {
        currentStartX + (mCharSize + mSpacePin).toInt()
    }

}
