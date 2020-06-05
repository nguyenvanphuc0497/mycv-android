package vanphuc0497.job.mycv.ui.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
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
        private const val DEFAULT_WIDTH_STROKE = 1F
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

        var textDraw = text ?: ""
//        text?.forEach { _ ->
//            textDraw += mMarkPassword
//        }
        paint.getTextWidths(textDraw, 0, textDraw.length, mTextWidths)

        //Set position cursor
        mRectCursor.let {
            it.top = height - bottom.toFloat()
            it.left = startX.toFloat() + mCharSize / 2
            it.right = it.left + mWidthCursor
            it.bottom = height.toFloat() - paddingBottom
        }

        for (i in 0..mNumChars.toInt()) {
            drawBackground(canvas, startX.toFloat())

            // Draw text
            if ((text?.length ?: 0) > i) {
                val middle = startX + mCharSize / 2
                paint.run {
                    color = currentTextColor
                    strokeWidth = 0F
                    canvas.drawText(
                        textDraw, i, i + 1, middle - mTextWidths[0] / 2,
                        bottom.toFloat() - mOffsetBottomText,
                        this
                    )
                }
                //Reset position cursor
                mRectCursor.let {
                    it.left = startX + mCharSize + mSpacePin + (mCharSize / 2)
                    it.right = it.left + mWidthCursor
                }
            }

            // Calculator startX of next text
            startX += if (mSpacePin < 0) {
                (mCharSize * 2).toInt()
            } else {
                (mCharSize + mSpacePin).toInt()
            }
        }

        if (isShowCursor) {
            if (isCursorBlink) {
                paint.color = Color.TRANSPARENT
            } else {
                paint.color = mColorCursor
            }
            isCursorBlink = !isCursorBlink
            canvas.drawRect(mRectCursor, paint)
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

    private fun drawBackground(canvas: Canvas, startX: Float) {
        val rectBackground = RectF(startX, 0F, startX + mCharSize, height.toFloat())
        if (isErrorPassword) {
            // Draw Background error
            paint.run {
                color = mColorBackGroundPinError
                style = Paint.Style.FILL_AND_STROKE
                canvas.drawRoundRect(rectBackground, mRadiusBackGround, mRadiusBackGround, this)
            }
        } else {
            // Draw stroke
            paint.run {
                color = mColorStroke
                strokeWidth = mWidthStroke
                style = Paint.Style.STROKE
                canvas.drawRoundRect(rectBackground, mRadiusBackGround, mRadiusBackGround, this)
            }
            // Draw Background
            val rectFill = RectF(
                startX + mWidthStroke,
                0F + mWidthStroke,
                startX + mCharSize - mWidthStroke,
                height - mWidthStroke
            )
            paint.run {
                color = mColorBackGroundPin
                style = Paint.Style.FILL
                canvas.drawRoundRect(rectFill, mRadiusBackGround, mRadiusBackGround, this)
            }
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
}
