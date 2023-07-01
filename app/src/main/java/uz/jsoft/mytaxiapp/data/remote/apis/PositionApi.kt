package uz.jsoft.mytaxiapp.data.remote.apis

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uz.jsoft.mytaxiapp.data.model.Point
import uz.jsoft.mytaxiapp.data.remote.response.GeoResponse

/**
 * Created by Jasurbek Kurganbayev 20/03/2022
 */
interface PositionApi {


    @GET("reverse")
    suspend fun getGeoCodeInfo(
        @Query("access_key") access_key: String,
        @Query("query") query: Point,
    ): Response<GeoResponse.Result>

}