package uz.jsoft.mytaxiapp.domain.search

import androidx.paging.PagingData
import kotlinx.coroutines.CoroutineScope
import uz.jsoft.mytaxiapp.data.local_models.dashboard.DataWrapper
import uz.jsoft.mytaxiapp.data.model.Point
import uz.jsoft.mytaxiapp.data.remote.response.GeoResponse
import uz.jsoft.mytaxiapp.data.remote.response.SearchResponse

/**
 * Created by Jasurbek Kurganbayev 27/03/2022
 */
interface SearchRepository {


    suspend fun getSearchAddressInfo(
        limit: Int,
        lat: Double,
        lng: Double,
        query: String
    ): DataWrapper<List<SearchResponse.SearchAddressItem>>

//    fun getSearchAddressInfo(scope: CoroutineScope): DataWrapper<PagingData<SearchResponse.SearchAddressItem>>


    suspend fun getSubSearchAddressInfo(
        limit: Int,
        lat: Double,
        lng: Double,
        street_poi_id: Int
    ): DataWrapper<List<SearchResponse.SearchAddressItem>>


}