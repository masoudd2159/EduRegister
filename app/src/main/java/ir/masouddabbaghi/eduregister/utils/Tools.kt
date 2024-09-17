package ir.masouddabbaghi.eduregister.utils

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import carbon.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import ir.masouddabbaghi.eduregister.R
import java.text.SimpleDateFormat
import java.util.Locale

object Tools {
    fun Fragment.hideKeyboard() {
        view?.let { activity?.hideKeyboard(it) }
    }

    fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }

    fun displayImageWithGlide(
        context: Context,
        imageView: ImageView,
        imageSource: Any?,
        isImageDrawable: Boolean = false,
    ) {
        val glideRequest =
            when {
                imageSource is String && isImageDrawable -> {
                    val drawableResId = context.resources.getIdentifier(imageSource, "drawable", context.packageName)
                    if (drawableResId != 0) {
                        Glide.with(context).load(drawableResId)
                    } else {
                        Glide.with(context).load(R.drawable.no_image)
                    }
                }

                imageSource is String && !isImageDrawable -> {
                    if (imageSource.isNotEmpty()) {
                        Glide.with(context).load(imageSource)
                    } else {
                        Glide.with(context).load(R.drawable.no_image)
                    }
                }

                imageSource is Int -> Glide.with(context).load(imageSource)
                imageSource is Bitmap -> Glide.with(context).load(imageSource)
                else -> Glide.with(context).load(R.drawable.no_image)
            }

        val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()
        glideRequest
            .fallback(
                R.drawable.no_image,
            ).transition(DrawableTransitionOptions.withCrossFade(factory))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(imageView)
    }

    fun formatBirthdayDate(serverDate: String): String {
        val serverDateFormat = SimpleDateFormat("M/d/yyyy hh:mm:ss a", Locale.US)
        val outputDateFormat = SimpleDateFormat("yyyy/MM/dd", Locale.US)
        val date = serverDateFormat.parse(serverDate)
        return outputDateFormat.format(date!!)
    }

    fun formatJalaliDate(serverDate: String): String {
        val dateParts = serverDate.split("/")
        val year = dateParts[0]
        val month = dateParts[1].padStart(2, '0')
        val day = dateParts[2].padStart(2, '0')
        return "$year-$month-$day"
    }
}
