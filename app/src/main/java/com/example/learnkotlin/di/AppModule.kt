package com.example.learnkotlin.di

import android.content.Context
import com.example.learnkotlin.BuildConfig
import com.example.learnkotlin.data.remote.api.ApiInterface
import com.example.learnkotlin.data.repository.AppsRepositoryImpl
import com.example.learnkotlin.domain.repository.AppsRepository
import com.example.learnkotlin.util.AUTH.AUTH_HEADER
import com.example.learnkotlin.util.ResponseHandler
import com.example.learnkotlin.util.SessionManager
import com.example.learnkotlin.util.URL.BASE_URL
import com.example.learnkotlin.util.at
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideOKHttpClient(sessionManager: SessionManager): OkHttpClient {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .connectTimeout(2, TimeUnit.MINUTES)
            .writeTimeout(2, TimeUnit.MINUTES)
            .readTimeout(2, TimeUnit.MINUTES)
            .addInterceptor { chain ->
                val request = chain.request()
                    .newBuilder()
                    .addHeader("x-localization", "id")
                    .addHeader("Content-Type","application/json")
                    .addHeader("Cache-Control", "no-cache")
                    .addHeader("Cache-Control", "no-store")
                    .addHeader("Accept", "Accept: application/json")
                    .addHeader(AUTH_HEADER, at(sessionManager.token ?: ""))
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()

    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): ApiInterface =
        retrofit.create(ApiInterface::class.java)

    @Provides
    @Singleton
    fun provideRepository(apiInterface: ApiInterface, responseHandler: ResponseHandler): AppsRepository {
        return AppsRepositoryImpl(apiInterface, responseHandler)
    }

    @Provides
    @Singleton
    fun provideSessionManager(@ApplicationContext context: Context) = SessionManager(context)
}