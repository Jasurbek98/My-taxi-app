package uz.jsoft.mytaxiapp.data.local

import android.content.Context
import android.content.SharedPreferences
import uz.jsoft.mytaxiapp.utils.helpers.BooleanPreference
import uz.jsoft.mytaxiapp.utils.helpers.DoublePreference
import uz.jsoft.mytaxiapp.utils.helpers.IntPreference
import uz.jsoft.mytaxiapp.utils.helpers.StringPreference

/**
 * Created by Jasurbek Kurganbayev 13/03/2022
 */
class LocalStorage private constructor(context: Context) {

    companion object {

        @Volatile
        lateinit var instance: LocalStorage
            private set

        fun init(context: Context) {
            synchronized(this) {
                instance = LocalStorage(context)
            }
        }
    }

    val pref: SharedPreferences =
        context.getSharedPreferences("LocalStorage", Context.MODE_PRIVATE)

    var currentLat by DoublePreference(pref, 0.0)
    var currentLong by DoublePreference(pref, 0.0)
    var searchText by StringPreference(pref, "")

    var isPermissioned by BooleanPreference(pref, false)

    var searchItemPosition by IntPreference(pref, -1)

    var isOnCurrentLocation by BooleanPreference(pref, true)

}