package com.abarghouthi.moviesapp.ui.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale.Companion.current
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.abarghouthi.moviesapp.R
import com.abarghouthi.moviesapp.data.models.Movie
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.SwipeRefreshState
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.intellij.lang.annotations.JdkConstants.HorizontalAlignment
import java.io.IOException


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(modifier: Modifier = Modifier, onMovieSelected: (id: Int) -> Unit) {
    val viewModel = hiltViewModel<HomeViewModel>()
    val moviesLazyItems = viewModel.moviesPagingData.collectAsLazyPagingItems()
    var currentLanguage by rememberSaveable {
        mutableStateOf(current.language)
    }
    LaunchedEffect(currentLanguage) {
        if (current.language != currentLanguage) {
            launch(Dispatchers.Main) {
                moviesLazyItems.refresh()
                currentLanguage = current.language
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(id = R.string.app_name))
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
                )
            )
        }
    ) { scaffoldPaddingValues ->

        val swipeRefreshState = rememberSwipeRefreshState(
            isRefreshing = moviesLazyItems.loadState.refresh == LoadState.Loading &&
                    moviesLazyItems.itemCount > 0
        )
        SwipeRefresh(
            modifier = Modifier.padding(scaffoldPaddingValues),
            state = swipeRefreshState,
            onRefresh = {
                moviesLazyItems.refresh()
            }
        ) {
            Box(Modifier.fillMaxSize()) {
                LazyVerticalGrid(
                    contentPadding = PaddingValues(10.dp),
                    columns = GridCells.Adaptive(170.dp),
                    verticalArrangement = Arrangement.spacedBy(7.dp),
                    horizontalArrangement = Arrangement.spacedBy(7.dp)
                ) {
                    items(count = moviesLazyItems.itemCount) { index ->
                        if (moviesLazyItems[index] == null || moviesLazyItems[index]!!.posterPath == null) return@items
                        MovieCard(
                            title = moviesLazyItems[index]!!.title,
                            posterPath = moviesLazyItems[index]!!.posterPath!!,
                            year = if (
                                moviesLazyItems[index]!!.releaseDate != null
                                && moviesLazyItems[index]!!.releaseDate!!.isNotEmpty()
                            )
                                moviesLazyItems[index]!!.releaseDate!!.substring(0..3)
                            else "",
                            modifier = Modifier.fillMaxWidth(),
                            onClick = { onMovieSelected(moviesLazyItems[index]!!.id) }
                        )
                    }

                    when (moviesLazyItems.loadState.append) {
                        is LoadState.Error -> {
                            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 10.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center
                                ) {

                                    val msg =
                                        when ((moviesLazyItems.loadState.append as LoadState.Error).error) {
                                            is IOException -> stringResource(id = R.string.no_internet_connection)
                                            else -> stringResource(id = R.string.something_went_wrong)
                                        }

                                    Text(
                                        text = msg,
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    TextButton(onClick = { moviesLazyItems.retry() }) {
                                        Text(
                                            text = stringResource(R.string.retry),
                                            style = MaterialTheme.typography.titleMedium
                                        )
                                    }
                                }
                            }
                        }
                        is LoadState.Loading -> {
                            item(span = { GridItemSpan(maxCurrentLineSpan) }) {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(bottom = 20.dp),
                                ) {
                                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                                }
                            }
                        }
                        else -> {}
                    }
                }

                if (moviesLazyItems.loadState.refresh is LoadState.Error) {
                    val msg =
                        when ((moviesLazyItems.loadState.refresh as LoadState.Error).error) {
                            is IOException -> stringResource(id = R.string.no_internet_connection)
                            else -> stringResource(id = R.string.something_went_wrong)
                        }
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = msg,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.titleLarge,
                            modifier = Modifier.padding(20.dp),
                        )
                    }
                } else if(
                    moviesLazyItems.loadState.refresh is LoadState.Loading
                    && moviesLazyItems.itemCount == 0
                ) {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }

                when (moviesLazyItems.loadState.refresh) {
                    is LoadState.Error -> {

                    }
                    else -> {}
                }


            }

        }
    }
}