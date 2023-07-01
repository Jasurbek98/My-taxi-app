package uz.jsoft.mytaxiapp.di.modules

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import uz.jsoft.mytaxiapp.data.remote.apis.PositionApi
import uz.jsoft.mytaxiapp.data.remote.apis.SearchApi
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Jasurbek Kurganbayev 14/03/2022
 */
@Module
@InstallIn(SingletonComponent::class)
class ApiModule {

    @Provides
    @Singleton
    fun getGepositionApi(
        @Named("geoposition_base_url")
        retrofit: Retrofit
    ): PositionApi = retrofit.create(PositionApi::class.java)

    @Provides
    @Singleton
    fun getSearchApi(
        retrofit: Retrofit
    ): SearchApi = retrofit.create(SearchApi::class.java)
}