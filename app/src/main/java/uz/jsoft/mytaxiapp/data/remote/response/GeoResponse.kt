package uz.jsoft.mytaxiapp.data.remote.response

import com.google.gson.annotations.SerializedName
import java.io.Serializable


sealed class GeoResponse {

    data class Result(
        @field:SerializedName("data")
        val data: List<GeoInfo>
    ) : Serializable

    data class GeoInfo(

        @field:SerializedName("continent")
        val continent: String? = null,

        @field:SerializedName("country")
        val country: String? = null,

        @field:SerializedName("distance")
        val distance: Double? = null,

        @field:SerializedName("latitude")
        val latitude: Double? = null,

        @field:SerializedName("confidence")
        val confidence: Double? = null,

        @field:SerializedName("county")
        val county: String? = null,

        @field:SerializedName("locality")
        val locality: Any? = null,

        @field:SerializedName("administrative_area")
        val administrativeArea: Any? = null,

        @field:SerializedName("label")
        val label: String? = null,

        @field:SerializedName("type")
        val type: String? = null,

        @field:SerializedName("number")
        val number: Any? = null,

        @field:SerializedName("country_code")
        val countryCode: String? = null,

        @field:SerializedName("street")
        val street: Any? = null,

        @field:SerializedName("neighbourhood")
        val neighbourhood: Any? = null,

        @field:SerializedName("name")
        val name: String? = null,

        @field:SerializedName("postal_code")
        val postalCode: Any? = null,

        @field:SerializedName("region")
        val region: String? = null,

        @field:SerializedName("longitude")
        val longitude: Double? = null,

        @field:SerializedName("region_code")
        val regionCode: String? = null
    ) : Serializable

}

