package uz.jsoft.mytaxiapp.di.modules

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.chuckerteam.chucker.api.RetentionManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import uz.jsoft.mytaxiapp.app.App
import uz.jsoft.mytaxiapp.utils.BASE_URL
import uz.jsoft.mytaxiapp.utils.GEOPOSITION_BASE_URL
import uz.jsoft.mytaxiapp.utils.isConnected
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Jasurbek Kurganbayev 14/03/2022
 */


private const val MAX_STALE = 60 * 60 * 24 * 30 // 30day

private const val cacheSize: Long = 30 * 1024 * 1024 // 30MB
private val cache = Cache(App.instance.cacheDir, cacheSize)

@Module
@InstallIn(SingletonComponent::class)
class RetrofitModule {

    @Provides
    @Singleton
    fun getApi(): String = BASE_URL

    @Provides
    @Singleton
    @Named("geoposition_base_url")
    fun getGeopositionApi(): String = GEOPOSITION_BASE_URL

    @Provides
    @Singleton
    fun getLogging(): HttpLoggingInterceptor =
        HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BODY }


    @Provides
    @Singleton
    fun okHttpClient(
        logging: HttpLoggingInterceptor,
        @ApplicationContext context: Context,
    ): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .cache(cache)
        .connectionSpecs(listOf(ConnectionSpec.CLEARTEXT, ConnectionSpec.MODERN_TLS))
        .addInterceptor(provideOfflineCacheInterceptor())
        .addInterceptor(
            ChuckerInterceptor.Builder(context)
                .collector(getChuckerCollector(context))
                .maxContentLength(250_000L)
                .redactHeaders("Auth-Token", "Bearer")
                .alwaysReadResponseBody(true)
                .build()
        )
        .build()


    @Provides
    @Singleton
    fun getChuckerCollector(
        @ApplicationContext context: Context,
    ) = ChuckerCollector(
        context = context,
        // Toggles visibility of the notification
        showNotification = true,
        // Allows to customize the retention period of collected data
        retentionPeriod = RetentionManager.Period.ONE_HOUR
    )


    @Provides
    @Singleton
    fun getRetrofit(
        api: String,
        client: OkHttpClient
    ): Retrofit = Retrofit.Builder()
        .baseUrl(api)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    @Provides
    @Singleton
    @Named("geoposition_base_url")
    fun getGeopositionRetrofit(
        geoPositionApi: String,
        client: OkHttpClient
    ): Retrofit =
        Retrofit.Builder()
            .client(client)
            .baseUrl(GEOPOSITION_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    //retrieves max-stale cache if connection is off
    private fun provideOfflineCacheInterceptor() = Interceptor { chain ->
        var request = chain.request()
        if (!isConnected()) {
            val cacheControl = CacheControl.Builder()
                .maxStale(MAX_STALE, TimeUnit.SECONDS)
                .build()
            request = request.newBuilder()
                .removeHeader("Cache-Control")
                .cacheControl(cacheControl)
                .build()
        }
        chain.proceed(request)
    }
}