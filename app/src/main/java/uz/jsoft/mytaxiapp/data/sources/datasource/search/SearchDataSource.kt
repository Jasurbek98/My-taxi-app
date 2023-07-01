/*
package uz.jsoft.mytaxiapp.data.sources.datasource.search

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.mytaxi.utils.timberLog
import uz.jsoft.mytaxiapp.R
import uz.jsoft.mytaxiapp.app.App
import uz.jsoft.mytaxiapp.data.local.LocalStorage
import uz.jsoft.mytaxiapp.data.remote.apis.SearchApi
import uz.jsoft.mytaxiapp.data.remote.response.SearchResponse
import java.lang.Exception

*/
/**
 * Created by Jasurbek Kurganbayev 28/03/2022
 *//*

class SearchDataSource(
    private val searchApi: SearchApi,
    private val storage: LocalStorage
) : PagingSource<Int, SearchResponse.SearchAddressItem>() {

    private var emptyListener: ((Boolean) -> Unit)? = null

    override fun getRefreshKey(state: PagingState<Int, SearchResponse.SearchAddressItem>): Int? {
        timberLog("getRefreshKey = ${state.anchorPosition}")
        return state.anchorPosition

    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, SearchResponse.SearchAddressItem> {
        return try {
            val nextPageNumber = params.key ?: 1
            val response = searchApi.searchAddress(
                limit = nextPageNumber,
                lat = storage.currentLat,
                lng = storage.currentLong,
                query = storage.searchText
            )
            response.body()?.data?.let { emptyListener?.invoke(it.isEmpty()) }
            LoadResult.Page(
                data = response.body()?.data!!,
                prevKey = if (nextPageNumber > 1) nextPageNumber - 1 else null,
                nextKey = if (nextPageNumber < response.body()?.data?.size!!)
                    nextPageNumber + 1 else null

            )
        } catch (e: Exception) {
            LoadResult.Error(Exception(App.instance.resources.getString(R.string.connection_retry)))
        }
    }

    fun emptyFun(f: (Boolean) -> Unit) {
        emptyListener = f
    }
}*/
