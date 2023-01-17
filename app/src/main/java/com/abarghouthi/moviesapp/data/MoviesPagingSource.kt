package com.abarghouthi.moviesapp.data

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.abarghouthi.moviesapp.data.models.Movie
import com.abarghouthi.moviesapp.data.models.toMovie

class MoviesPagingSource(private val moviesService: MoviesService) : PagingSource<Int, Movie>() {
    override fun getRefreshKey(state: PagingState<Int, Movie>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Movie> {
        return try {
            val page = params.key ?: 1
            val movies: List<Movie> = moviesService.getMovies(page).results.map { it.toMovie() }
            LoadResult.Page(
                data = movies,
                prevKey = if (page == 1) null else page.minus(1),
                nextKey = if (movies.isEmpty()) null else page.plus(1),
            )
        } catch (e: Exception) {
            Log.e("TAG", "load movies error response ${e.message}")
            LoadResult.Error(e)
        }
    }
}