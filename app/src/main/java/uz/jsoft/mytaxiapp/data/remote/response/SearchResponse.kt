package uz.jsoft.mytaxiapp.data.remote.response

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

sealed class SearchResponse {

    data class Result(

        @field:SerializedName("data")
        val data: CandidatesData? = null,

        @field:SerializedName("status")
        val status: String? = null
    )


    data class CandidatesData(
        @field:SerializedName("candidates")
        val candidates: List<SearchAddressItem>? = null
    )

    data class Location(

        @field:SerializedName("lng")
        val lng: Double? = null,

        @field:SerializedName("lat")
        val lat: Double? = null
    )

    data class SearchAddressItem(

        @field:SerializedName("formatted_address")
        val formattedAddress: String? = null,

        @field:SerializedName("address")
        val address: String? = null,

        @field:SerializedName("distance")
        val distance: String? = null,

        @field:SerializedName("address_id")
        val addressId: Int? = null,

        @field:SerializedName("location")
        val location: Location? = null,

        @field:SerializedName("street_poi_id")
        val streetPoiId: Int? = null,

        @field:SerializedName("lang")
        val lang: String? = null,

        @Expose
        var subSearchList: List<SearchAddressItem>? = null
    )

}

