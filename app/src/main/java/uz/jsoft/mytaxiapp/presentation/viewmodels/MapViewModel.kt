package uz.jsoft.mytaxiapp.presentation.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import uz.jsoft.mytaxiapp.data.local_models.dashboard.DataWrapper
import uz.jsoft.mytaxiapp.data.remote.response.GeoResponse
import uz.jsoft.mytaxiapp.data.model.Point
import uz.jsoft.mytaxiapp.data.remote.response.SearchResponse
import uz.jsoft.mytaxiapp.domain.geoposition.GeoPositionRepository
import uz.jsoft.mytaxiapp.domain.search.SearchRepository
import uz.jsoft.mytaxiapp.utils.LIMIT_SIZE
import javax.inject.Inject

/**
 * Created by Jasurbek Kurganbayev 21/03/2022
 */

@HiltViewModel
class MapViewModel @Inject constructor(
    private val geoPositionRepository: GeoPositionRepository,
    private val searchRepository: SearchRepository
) : ViewModel() {

    private var searchPage = 20

    var currentDestination = GeoResponse.GeoInfo()

    var toDestination = SearchResponse.SearchAddressItem()


    private var _geoPositionResponse =
        MutableStateFlow<DataWrapper<GeoResponse.GeoInfo>>(DataWrapper.Empty())
    val geoResponse: StateFlow<DataWrapper<GeoResponse.GeoInfo>> =
        _geoPositionResponse.asStateFlow()


    private var _searchResponse =
        MutableStateFlow<DataWrapper<List<SearchResponse.SearchAddressItem>>>(DataWrapper.Empty())
    val searchResponse: StateFlow<DataWrapper<List<SearchResponse.SearchAddressItem>>> =
        _searchResponse.asStateFlow()

    private var _subSearchResponse =
        MutableStateFlow<DataWrapper<List<SearchResponse.SearchAddressItem>>>(DataWrapper.Empty())
    val subSearchResponse: StateFlow<DataWrapper<List<SearchResponse.SearchAddressItem>>> =
        _subSearchResponse.asStateFlow()

    fun getGeoPosition(point: Point) = viewModelScope.launch(Dispatchers.IO) {
        _geoPositionResponse.value = DataWrapper.Loading()
        _geoPositionResponse.value = geoPositionRepository.getGeoPositionInfo(point)
    }


    fun getSubSearchItems(point: Point, addressId: Int) = viewModelScope.launch(Dispatchers.IO) {
        _subSearchResponse.value = DataWrapper.Loading()
        _subSearchResponse.value =
            searchRepository.getSubSearchAddressInfo(
                LIMIT_SIZE,
                point.latitude,
                point.longitude,
                addressId
            )
    }


    fun getSearchItems(point: Point, query: String) = viewModelScope.launch(Dispatchers.IO) {
        _searchResponse.value = DataWrapper.Loading()
        _searchResponse.value =
            searchRepository.getSearchAddressInfo(
                searchPage,
                point.latitude,
                point.longitude,
                query
            )
    }


}