package uz.jsoft.mytaxiapp.utils.extensions

import android.content.IntentSender
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.*
import uz.jsoft.mytaxiapp.app.App
import uz.jsoft.mytaxiapp.utils.REQUEST_LOCATION_SETTING_PERMISSION

/**
 * Created by Jasurbek Kurganbayev 25/03/2022
 */

fun Fragment.LocationSetting() {

    val request = LocationRequest.create()
    request.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
    request.interval = 5000
    request.fastestInterval = 2000

    val builder = LocationSettingsRequest.Builder().addLocationRequest(request)
    builder.setAlwaysShow(true)

    val result = LocationServices.getSettingsClient(App.instance)
        .checkLocationSettings(builder.build())


    result.addOnCompleteListener { task ->
        try {
            var response = task.getResult(ApiException::class.java)

        } catch (e: ApiException) {

            when (e.statusCode) {
                LocationSettingsStatusCodes.RESOLUTION_REQUIRED -> {
                    try {
                        val resolvableApiException: ResolvableApiException =
                            e as ResolvableApiException
                        resolvableApiException.startResolutionForResult(
                            requireActivity(),
                            REQUEST_LOCATION_SETTING_PERMISSION
                        )

                    } catch (sendIntent: IntentSender.SendIntentException) {

                    }
                }
                LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE -> {
                    return@addOnCompleteListener;
                }
            }
        }

    }

}