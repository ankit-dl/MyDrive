package com.rsoft.mydrive.di

import android.content.Context
import android.content.SharedPreferences
import com.rsoft.mydrive.common.Const
import com.rsoft.mydrive.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    private const val BASE_URL = "https://www.googleapis.com/"




    @Provides
    @Singleton
    fun provideRetroFit(client: OkHttpClient) = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()


    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit) = retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun providePrefreence(@ApplicationContext context: Context) =

        context.getSharedPreferences(Const.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE)


}