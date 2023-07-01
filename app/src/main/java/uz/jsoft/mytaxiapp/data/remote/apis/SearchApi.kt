package uz.jsoft.mytaxiapp.data.remote.apis

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query
import uz.jsoft.mytaxiapp.data.remote.response.SearchResponse

/**
 * Created by Jasurbek Kurganbayev 27/03/2022
 */
interface SearchApi {

    @GET("/v2/search")
    suspend fun searchAddress(
        @Header("api_key") api_key: String,
        @Query("limit") limit: Int,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("query") query: String
    ): Response<SearchResponse.Result>


    @GET("/v2/search")
    suspend fun searchSubAddress(
        @Header("api_key") api_key: String,
        @Query("limit") limit: Int,
        @Query("lat") lat: Double,
        @Query("lng") lng: Double,
        @Query("street_poi_id") street_poi_id: Int
    ): Response<SearchResponse.Result>


}