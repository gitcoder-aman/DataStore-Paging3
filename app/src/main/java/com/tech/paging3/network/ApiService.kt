package com.tech.paging3.network

import com.tech.paging3.model.MovieModel
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

//    https://api.themoviedb.org/3/trending/movie/day?api_key=ac28a3498de90c46b11f31bda02b8b97&page=1

    @GET("trending/movie/day")
    suspend fun getTrendingMovie(
        @Query("api_key") api_key : String,
        @Query("page") page : Int
    ) : MovieModel

}