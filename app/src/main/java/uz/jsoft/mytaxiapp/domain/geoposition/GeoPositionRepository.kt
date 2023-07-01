package uz.jsoft.mytaxiapp.domain.geoposition

import uz.jsoft.mytaxiapp.data.local_models.dashboard.DataWrapper
import uz.jsoft.mytaxiapp.data.model.Point
import uz.jsoft.mytaxiapp.data.remote.response.GeoResponse

/**
 * Created by Jasurbek Kurganbayev 21/03/2022
 */
interface GeoPositionRepository {

//    fun geoGeoCodeInfo(latLng: Point): Flow<Result<GeoResponse>>

    suspend fun getGeoPositionInfo(latLng: Point): DataWrapper<GeoResponse.GeoInfo>
}