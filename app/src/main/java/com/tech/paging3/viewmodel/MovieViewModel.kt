package com.tech.paging3.viewmodel

import androidx.lifecycle.ViewModel
import com.tech.paging3.repository.MovieRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(
    private val movieRepository: MovieRepository
) : ViewModel() {

    val pagingData = movieRepository.getTrendingMovie()
}