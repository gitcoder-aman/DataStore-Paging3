package com.tech.paging3

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.request.ImageRequest
import com.google.accompanist.coil.rememberCoilPainter
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.tech.datastore.ui.theme.DataStoreTheme
import com.tech.paging3.model.MovieModel
import com.tech.paging3.model.Result
import com.tech.paging3.viewmodel.MovieViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class Paging3Activity : ComponentActivity() {

    private val viewModel: MovieViewModel by viewModels()

    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DataStoreTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(title = { Text(text = "MoviePaging") })
                    },
                    content = {
                        Paging(viewModel)
                    }
                )
            }
        }
    }
}

@Composable
fun Paging(viewModel: MovieViewModel) {
    val pagingData = viewModel.pagingData.collectAsLazyPagingItems()

    val swipeRefreshState = rememberSwipeRefreshState(isRefreshing = false)

    SwipeRefresh(state = swipeRefreshState, onRefresh = {
        pagingData.refresh()
        swipeRefreshState.isRefreshing = true
    }) {
        LazyColumn {
            if (pagingData.loadState.refresh is LoadState.Loading) {
                Log.i("@@pagingLoadState", "1Paging: LoadState Refresh is Loading")
                item {
                    Box(modifier = Modifier.fillParentMaxSize()) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
            if (pagingData.loadState.refresh is LoadState.NotLoading) {
                Log.i("@@pagingLoadState", "2Paging: LoadState Refresh is Not Loading")
                swipeRefreshState.isRefreshing = false
                items(pagingData.itemCount) {
                    pagingData[it]?.let { movieItem ->
                        ListItem(movieItem = movieItem)
                    }
                }
            }
            if (pagingData.loadState.refresh is LoadState.Error) {
                item {
                Log.i("@@pagingLoadState", "3Paging: LoadState Refresh is Error")
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Error Occurred", modifier = Modifier.clickable {
                            pagingData.refresh()
                        }.align(Alignment.Center), style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
            if (pagingData.loadState.append is LoadState.Loading) {
                Log.i("@@pagingLoadState", "4Paging: LoadState Append is Loading")
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
            if (pagingData.loadState.append is LoadState.Error) {
                Log.i("@@pagingLoadState", "5Paging: LoadState Append is Error")
                item {
                    ErrorFooter {
                        pagingData.retry()
                    }
                }
            }
            if (pagingData.loadState.prepend is LoadState.Loading) {
                Log.i("@@pagingLoadState", "6Paging: LoadState Prepend is Loading")
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp)
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }
            if (pagingData.loadState.prepend is LoadState.Error) {
                Log.i("@@pagingLoadState", "7Paging: LoadState Prepend is Error")
                item {
                    ErrorHeader {
                        pagingData.retry()
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorHeader(retry: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Text(text = "Tap to Retry", modifier = Modifier
            .clickable {
                retry.invoke()
            }
            .align(Alignment.Center), style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun ErrorFooter(retry: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(imageVector = Icons.Default.Warning, contentDescription = null)
            Text(
                text = "Error Occurred",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(text = "Retry", modifier = Modifier
                    .clickable {
                        retry.invoke()
                    }
                    .align(Alignment.CenterEnd), style = MaterialTheme.typography.bodyMedium)
            }
        }
    }
}

@Composable
fun ListItem(movieItem: Result) {
    Column(
        modifier = Modifier.padding(
            8.dp
        )
    ) {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .height(380.dp),
            painter = rememberCoilPainter(
                request = ImageRequest.Builder(LocalContext.current).crossfade(true)
                    .data("https://image.tmdb.org/t/p/w500" + movieItem.poster_path).build()
            ), contentDescription = null
        )
        Text(
            text = "Title" + movieItem.title,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            text = "Overview" + movieItem.overview,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(8.dp)
        )
        Text(
            text = "Release Date" + movieItem.release_date,
            style = MaterialTheme.typography.bodyMedium,
            modifier = Modifier.padding(8.dp)
        )
    }
}