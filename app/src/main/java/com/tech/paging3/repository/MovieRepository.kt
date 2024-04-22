package com.tech.paging3.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.tech.paging3.model.Result
import com.tech.paging3.network.ApiService
import com.tech.paging3.paging.MoviePagingSource
import kotlinx.coroutines.flow.Flow

class MovieRepository (
    private val apiService : ApiService
){
    fun getTrendingMovie() : Flow<PagingData<Result>>{
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                prefetchDistance = 5,
                enablePlaceholders = false,
                initialLoadSize = 10,
            ), pagingSourceFactory = {
                MoviePagingSource(apiService)
            }
        ).flow
    }
}