package uz.jsoft.mytaxiapp.di.modules

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import uz.jsoft.mytaxiapp.domain.geoposition.GeoPositionRepository
import uz.jsoft.mytaxiapp.domain.geoposition.GeoPositionRepositoryImpl
import uz.jsoft.mytaxiapp.domain.search.SearchRepository
import uz.jsoft.mytaxiapp.domain.search.SearchRepositoryImpl

/**
 * Created by Jasurbek Kurganbayev 21/03/2022
 */

@Module
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun getGeoInfoRepository(geoPositionRepositoryImpl: GeoPositionRepositoryImpl): GeoPositionRepository

    @Binds
    fun getSearchRepository(searchRepositoryImpl: SearchRepositoryImpl): SearchRepository
}