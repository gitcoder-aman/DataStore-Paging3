package com.tech.paging3.di

import com.tech.paging3.constants.Constant.BASE_URL
import com.tech.paging3.model.MovieModel
import com.tech.paging3.network.ApiService
import com.tech.paging3.repository.MovieRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    fun provideApiService() : ApiService{
        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }

    @Provides
    fun provideMovieRepository(apiService : ApiService) : MovieRepository{
        return MovieRepository(apiService)
    }
}