package uz.jsoft.mytaxiapp.domain.search

import android.util.Log
import retrofit2.HttpException
import uz.jsoft.mytaxiapp.data.local.LocalStorage
import uz.jsoft.mytaxiapp.data.local_models.dashboard.DataWrapper
import uz.jsoft.mytaxiapp.data.remote.apis.SearchApi
import uz.jsoft.mytaxiapp.data.remote.response.SearchResponse
import uz.jsoft.mytaxiapp.utils.API_KEY
import javax.inject.Inject

/**
 * Created by Jasurbek Kurganbayev 27/03/2022
 */
class SearchRepositoryImpl @Inject constructor(
    private val searchApi: SearchApi,
) : SearchRepository {


    override suspend fun getSearchAddressInfo(
        limit: Int,
        lat: Double,
        lng: Double,
        query: String
    ) = try {
        val response = searchApi.searchAddress(API_KEY, limit, lat, lng, query)
        if (response.isSuccessful) {
            DataWrapper.Success(response.body()?.data?.candidates ?: emptyList())
        } else {
            DataWrapper.Error(HttpException(response))
        }
    } catch (e: Exception) {
        DataWrapper.Error(e)
    }

    override suspend fun getSubSearchAddressInfo(
        limit: Int,
        lat: Double,
        lng: Double,
        street_poi_id: Int
    ) = try {
        val response = searchApi.searchSubAddress(API_KEY, limit, lat, lng, street_poi_id)
        if (response.isSuccessful) {
            DataWrapper.Success(response.body()?.data?.candidates?: emptyList())
        } else {
            DataWrapper.Error(HttpException(response))
        }
    } catch (e: Exception) {
        DataWrapper.Error(e)
    }


}