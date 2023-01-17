package com.abarghouthi.moviesapp.ui.details

import android.app.Activity
import android.view.View
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.abarghouthi.moviesapp.R
import com.abarghouthi.moviesapp.data.MoviesService
import java.io.IOException

@Composable
fun DetailsScreen(modifier: Modifier = Modifier, id: Int, onBackClicked: () -> Unit) {
    val viewModel = hiltViewModel<DetailsViewModel>()
    val view = LocalView.current
    LaunchedEffect(Unit) {
        changeSystemUiVisibility(view, visible = false)
        viewModel.getMovieDetailsById(id)
    }
    DisposableEffect(Unit) {
        onDispose {
            changeSystemUiVisibility(view, visible = true)
        }
    }
    val state = viewModel.state
    Surface {
        Box {
            if (state.isLoading) {
                Box(modifier = Modifier.fillMaxSize()) {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }
            } else if (state.movieDetails != null) {
                Column(
                    Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState()),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(Modifier.fillMaxWidth()) {
                        AsyncImage(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp),
                            contentScale = ContentScale.Crop,
                            model = MoviesService.getBackdropPath(state.movieDetails.backdropPath ?: ""),
                            contentDescription = "movie backdrop image"
                        )
                        AsyncImage(
                            modifier = Modifier
                                .width(124.dp)
                                .height(187.dp)
                                .align(Alignment.BottomCenter)
                                .offset(y = 80.dp)
                                .border(
                                    width = 1.dp,
                                    color = MaterialTheme.colorScheme.background,
                                    shape = MaterialTheme.shapes.medium
                                )
                                .clip(
                                    MaterialTheme.shapes.medium
                                ),

                            model = MoviesService.getPosterPath(state.movieDetails.posterPath ?: ""),
                            contentDescription = "movie poster image",
                            contentScale = ContentScale.FillBounds
                        )

                    }
                    Spacer(modifier = Modifier.height(100.dp))
                    Text(
                        text = state.movieDetails.title,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = state.movieDetails.releaseDate ?: "",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = state.movieDetails.overview ?: "",
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 20.dp),
                        textAlign = TextAlign.Justify
                    )
                }
            } else {
                Box(Modifier.fillMaxSize()) {
                    val msg = when (state.error) {
                        is IOException -> stringResource(R.string.no_internet_connection)
                        else -> stringResource(R.string.something_went_wrong)
                    }
                    Text(
                        textAlign = TextAlign.Center,
                        text = msg,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(20.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
            IconButton(
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
                onClick = { onBackClicked() },
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(top = 10.dp, start = 10.dp)
            ) {
                Icon(
                    imageVector =
                    if(LocalLayoutDirection.current == LayoutDirection.Ltr) Icons.Default.ArrowBack
                    else Icons.Default.ArrowForward ,
                    contentDescription = "back"
                )
            }
        }

    }
}

fun changeSystemUiVisibility(view: View, visible: Boolean) {
    val window = (view.context as Activity).window
    val types = WindowInsetsCompat.Type.statusBars() or WindowInsetsCompat.Type.navigationBars()
    if (visible) {
        WindowCompat.getInsetsController(window, view).show(types)
    } else {
        WindowCompat.getInsetsController(window, view).hide(types)
    }
}