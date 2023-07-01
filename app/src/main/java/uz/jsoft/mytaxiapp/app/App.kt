package uz.jsoft.mytaxiapp.app

import android.app.Application
import android.app.PendingIntent
import com.intuit.sdp.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import uz.jsoft.mytaxiapp.data.local.LocalStorage


/**
 * Created by Jasurbek Kurganbayev 13/03/2022
 */

@HiltAndroidApp
class App : Application() {


    override fun onCreate() {
        super.onCreate()
        instance = this
        LocalStorage.init(this)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
     /*   val clickPendingIntent = PendingIntent.getActivity(
            this,
            0,
            clickIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )*/
    }

    companion object {

        lateinit var instance: App
    }
}