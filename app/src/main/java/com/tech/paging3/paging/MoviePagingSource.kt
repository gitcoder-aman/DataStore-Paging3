package com.tech.paging3.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.tech.paging3.constants.Constant.API_KEY
import com.tech.paging3.model.Result
import com.tech.paging3.network.ApiService
import kotlinx.coroutines.delay
import retrofit2.HttpException
import java.io.IOException

class MoviePagingSource(
    private val apiService : ApiService
)  : PagingSource<Int,Result>(){
    override fun getRefreshKey(state: PagingState<Int, Result>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1) ?:
            state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Result> {
        val page = params.key?:1
        return try {
            delay(3000)
            val remoteData = apiService.getTrendingMovie(API_KEY,page)

            Log.i("@@moviePaging", "load: ${remoteData.results.size}")
            val nextKey = if(remoteData.results.size < params.loadSize){
                null
            }else{
                page+1
            }
            LoadResult.Page(
                remoteData.results,
                prevKey = if(page == 1 ) null else page - 1,
                nextKey = nextKey
            )
        }catch (e : IOException){
            LoadResult.Error(e)
        }catch (e : HttpException){
            LoadResult.Error(e)
        }
    }
}