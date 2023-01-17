package com.abarghouthi.moviesapp.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.abarghouthi.moviesapp.data.MoviesService

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieCard(
    modifier: Modifier = Modifier,
    title: String,
    posterPath: String,
    year: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier.semantics { contentDescription = "movie item" },
        onClick = onClick
    ) {
        Column {
            AsyncImage(
                model = MoviesService.getPosterPath(posterPath),
                contentDescription = "movie poster image",
                modifier = Modifier.height(264.dp),
                contentScale = ContentScale.Crop
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxSize().padding(horizontal = 10.dp, vertical = 10.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium, overflow = TextOverflow.Ellipsis, softWrap = false)
                Spacer(modifier = Modifier.height(10.dp))
                Text(text = year, style = MaterialTheme.typography.bodyLarge)
            }
        }
    }
}