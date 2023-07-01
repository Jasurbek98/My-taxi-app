package uz.jsoft.mytaxiapp.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.appcompat.widget.AppCompatEditText
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import uz.jsoft.mytaxiapp.utils.`typealias`.SingleBlock
import java.util.*



fun ViewGroup.inflate(@LayoutRes resId: Int): View =
    LayoutInflater.from(context).inflate(resId, this, false)


fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

fun Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

@SuppressLint("ServiceCast")
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}


fun View.visible(animate: Boolean = true) {
    if (animate) {
        animate().alpha(1f).setDuration(300).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
                visibility = View.VISIBLE
            }
        })
    } else {
        visibility = View.VISIBLE
    }
}


/** Set the View visibility to INVISIBLE and eventually animate view alpha till 0% */
fun View.invisible(animate: Boolean = true) {
    hide(View.INVISIBLE, animate)
}

/** Set the View visibility to GONE and eventually animate view alpha till 0% */
fun View.gone(animate: Boolean = true) {
    hide(View.GONE, animate)
}



/** Convenient method that chooses between View.visible() or View.invisible() methods */
fun View.visibleOrInvisible(show: Boolean, animate: Boolean = true) {
    if (show) visible(animate) else invisible(animate)
}

/** Convenient method that chooses between View.visible() or View.gone() methods */
fun View.visibleOrGone(show: Boolean, animate: Boolean = true) {
    if (show) visible(animate) else gone(animate)
}

private fun View.hide(hidingStrategy: Int, animate: Boolean = true) {
    if (animate) {
        animate().alpha(0f).setDuration(300).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                visibility = hidingStrategy
            }
        })
    } else {
        visibility = hidingStrategy
    }
}



fun View.gone() {
    this.visibility = View.GONE
}




fun Fragment.showToast(string: String) {
    Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
}

fun Fragment.showToastWithRes(@StringRes string: Int) {
    Toast.makeText(requireContext(), string, Toast.LENGTH_SHORT).show()
}

fun Activity.changeStatusBarColor(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.statusBarColor = color
    }
}

fun Activity.changeNavigationBarColor(color: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
        window.navigationBarColor = color
    }
}

fun Int.toDarkenColor(): Int {
    val hsv = FloatArray(3)
    Color.colorToHSV(this, hsv)
    hsv[2] *= 0.8f
    return Color.HSVToColor(hsv)
}

fun Spinner.onItemSelected(action: SingleBlock<Int>) {
    onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(
            parent: AdapterView<*>?,
            view: View?,
            position: Int,
            id: Long
        ) {
            action(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {
        }
    }
}







