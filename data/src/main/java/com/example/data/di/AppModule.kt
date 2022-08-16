package com.example.data.di

import android.content.Context
import android.net.ConnectivityManager
import android.support.compat.BuildConfig
import com.example.data.api.GoRestApiService
import com.example.data.datastore.remote.GoRestRemoteDataStore
import com.example.data.datastore.remote.GoRestRemoteDataStoreImpl
import com.example.data.mapper.ResponseUsersToDataUsersMapper
import com.example.data.mapper.ResponseUsersToDataUsersMapperImpl
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.Reusable
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class ApiModule(
    private val baseUrlOverride: String? = null
) {
    //To allow dynamic testing to be made for the UI utilising mocks later
    @Provides
    @BaseUrl
    fun provideBaseUrl() = baseUrlOverride ?: "https://api.stackexchange.com/2.3/"

    @Singleton
    @Provides
    fun provideStackExchangeRemoteDataStore(
        apiService: GoRestApiService,
        responseStackExchangeToDataStackExchangeMapper: ResponseUsersToDataUsersMapper
    ): GoRestRemoteDataStore =
        GoRestRemoteDataStoreImpl(apiService, responseStackExchangeToDataStackExchangeMapper)

    @Provides
    @Singleton
    fun provideRetrofitBuilder(
        gsonConverterFactory: GsonConverterFactory,
        @BaseUrl baseUrl: String
    ): Retrofit.Builder = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(gsonConverterFactory)

    @Provides
    @Singleton
    fun provideHttpBuilder() =
        OkHttpClient.Builder().apply {
            //Logging interceptor to only apply to debug builds
            if (BuildConfig.DEBUG) {
                val httpLoggingInterceptor = HttpLoggingInterceptor()
                httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
                addInterceptor(httpLoggingInterceptor)
            }

            readTimeout(10L, TimeUnit.SECONDS)
            connectTimeout(10L, TimeUnit.SECONDS)
        }

    @Provides
    @Singleton
    fun provideGson(): Gson = GsonBuilder().create()

    @Provides
    @Singleton
    fun provideGsonConverterFactory(
        gson: Gson
    ): GsonConverterFactory = GsonConverterFactory.create(gson)

    @Provides
    @Singleton
    fun provideConnectivityManager(context: Context): ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager


    @Provides
    @Reusable
    fun provideResponseStackExchangeToDataStackExchangeMapper(): ResponseUsersToDataUsersMapper =
        ResponseUsersToDataUsersMapperImpl()

    @Module
    companion object {
        @Provides
        @JvmStatic
        @Singleton
        internal fun provideApi(retrofit: Retrofit): GoRestApiService =
            retrofit.create(GoRestApiService::class.java)

        @Provides
        @JvmStatic
        @Singleton
        internal fun provideRetrofit(
            httpBuilder: OkHttpClient.Builder,
            retrofitBuilder: Retrofit.Builder, gson: Gson
        ): Retrofit = retrofitBuilder
            .client(httpBuilder.build())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
}