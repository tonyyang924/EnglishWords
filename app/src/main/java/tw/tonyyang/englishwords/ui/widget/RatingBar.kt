package tw.tonyyang.englishwords.ui.widget

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import tw.tonyyang.englishwords.R
import java.math.BigDecimal
import kotlin.math.roundToInt

class RatingBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    interface OnRatingChangeListener {
        fun onRatingChange(RatingCount: Float)
    }

    var mClickable: Boolean
    var halfStart: Boolean
    var starCount: Int
    var onRatingChangeListener: OnRatingChangeListener? = null
    var starImageWidth: Float
    var starImageHeight: Float
    var starImagePadding: Float
    var starEmptyDrawable: Drawable?
    var starFillDrawable: Drawable?
    var starHalfDrawable: Drawable?
    var y = 1

    private fun getStarImageView(context: Context, isEmpty: Boolean): ImageView {
        val imageView = ImageView(context)
        val para = ViewGroup.LayoutParams(
            starImageWidth.roundToInt(),
            starImageHeight.roundToInt()
        )
        imageView.layoutParams = para
        imageView.setPadding(0, 0, starImagePadding.roundToInt(), 0)
        if (isEmpty) {
            imageView.setImageDrawable(starEmptyDrawable)
        } else {
            imageView.setImageDrawable(starFillDrawable)
        }
        return imageView
    }

    fun setStar(starCount: Float) {
        var newStarCount = starCount
        val fint = newStarCount.toInt()
        val b1 = BigDecimal(newStarCount.toString())
        val b2 = BigDecimal(fint.toString())
        val fPoint = b1.subtract(b2).toFloat()
        newStarCount = if (fint > this.starCount) this.starCount.toFloat() else fint.toFloat()
        newStarCount = if (newStarCount < 0) 0.toFloat() else newStarCount

        var i = 0
        while (i < newStarCount) {
            (getChildAt(i) as ImageView).setImageDrawable(starFillDrawable)
            ++i
        }
        if (fPoint > 0) {
            (getChildAt(fint) as ImageView).setImageDrawable(starHalfDrawable)
            var i = this.starCount - 1
            while (i >= newStarCount + 1) {
                (getChildAt(i) as ImageView).setImageDrawable(starEmptyDrawable)
                --i
            }
        } else {
            var i = this.starCount - 1
            while (i >= newStarCount) {
                (getChildAt(i) as ImageView).setImageDrawable(starEmptyDrawable)
                --i
            }
        }
    }

    init {
        orientation = HORIZONTAL
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar)
        starHalfDrawable = typedArray.getDrawable(R.styleable.RatingBar_starHalf)
        starEmptyDrawable = typedArray.getDrawable(R.styleable.RatingBar_starEmpty)
        starFillDrawable = typedArray.getDrawable(R.styleable.RatingBar_starFill)
        starImageWidth = typedArray.getDimension(R.styleable.RatingBar_starImageWidth, 60f)
        starImageHeight = typedArray.getDimension(R.styleable.RatingBar_starImageHeight, 120f)
        starImagePadding = typedArray.getDimension(R.styleable.RatingBar_starImagePadding, 15f)
        starCount = typedArray.getInteger(R.styleable.RatingBar_starCount, 5)
        val starNum = typedArray.getInteger(R.styleable.RatingBar_starNum, 0)
        mClickable = typedArray.getBoolean(R.styleable.RatingBar_clickable, true)
        halfStart = typedArray.getBoolean(R.styleable.RatingBar_halfstart, false)
        for (i in 0 until starNum) {
            val imageView = getStarImageView(context, false)
            addView(imageView)
        }
        for (i in 0 until starCount) {
            val isEmpty = true
            val imageView = getStarImageView(context, isEmpty)
            imageView.setOnClickListener { v: View? ->
                if (mClickable) {
                    if (halfStart) {
                        //TODO:This is not the best way to solve half a star,
                        //TODO:but That's what I can do,Please let me know if you have a better solution
                        if (y % 2 == 0) {
                            setStar(indexOfChild(v) + 1f)
                        } else {
                            setStar(indexOfChild(v) + 0.5f)
                        }
                        if (y % 2 == 0) {
                            onRatingChangeListener?.onRatingChange(indexOfChild(v) + 1f)
                            y++
                        } else {
                            onRatingChangeListener?.onRatingChange(indexOfChild(v) + 0.5f)
                            y++
                        }
                    } else {
                        setStar(indexOfChild(v) + 1f)
                        onRatingChangeListener?.onRatingChange(indexOfChild(v) + 1f)
                    }
                }
            }
            addView(imageView)
        }
        typedArray.recycle()
    }
}