package uz.jsoft.mytaxiapp.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Handler
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.viewpager.widget.ViewPager
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File


fun Context.showToast(st: String) {
    Toast.makeText(this, st, Toast.LENGTH_SHORT).show()
}


fun ViewPager.pageChangeListener(f: (Int) -> Unit) =
    this.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
        override fun onPageScrollStateChanged(state: Int) {
        }

        override fun onPageScrolled(
            position: Int,
            positionOffset: Float,
            positionOffsetPixels: Int
        ) {
        }

        override fun onPageSelected(position: Int) {
            f.invoke(position)
        }
    })

const val REQUEST_APP_SETTINGS = 11001
fun Activity.goToSettings() {
    val intent =
        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:$packageName"))
    intent.addCategory(Intent.CATEGORY_DEFAULT)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    startActivityForResult(intent, REQUEST_APP_SETTINGS)
}

fun SearchView.onQueryListener(
    mHandler: Handler,
    time: Long,
    listener: (newText: String?) -> Unit
) =
    this.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            listener.invoke(query)
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            mHandler.removeCallbacksAndMessages(null)
            mHandler.postDelayed({
                listener.invoke(newText)
            }, time)
            return true
        }
    })

fun File.toRequestData(fieldName: String): MultipartBody.Part {
    val fileReqBody = this.asRequestBody("multipart/form-data".toMediaTypeOrNull())
    return MultipartBody.Part.createFormData(fieldName, this.name, fileReqBody)
}


