package uz.jsoft.mytaxiapp.domain.geoposition

import retrofit2.HttpException
import uz.jsoft.mytaxiapp.data.local_models.dashboard.DataWrapper
import uz.jsoft.mytaxiapp.data.model.Point
import uz.jsoft.mytaxiapp.data.remote.apis.PositionApi
import uz.jsoft.mytaxiapp.utils.GEOPOSITION_ACCES_KEY
import javax.inject.Inject

/**
 * Created by Jasurbek Kurganbayev 21/03/2022
 */
class GeoPositionRepositoryImpl @Inject constructor(
    private val positionApi: PositionApi,
) : GeoPositionRepository {


    override suspend fun getGeoPositionInfo(latLng: Point) = try {
        val response = positionApi.getGeoCodeInfo(GEOPOSITION_ACCES_KEY, latLng)
        if (response.isSuccessful) {
            DataWrapper.Success(response.body()?.data!![0])
        } else {
            DataWrapper.Error(HttpException(response))
        }
    } catch (e: Exception) {
        DataWrapper.Error(e)
    }
}