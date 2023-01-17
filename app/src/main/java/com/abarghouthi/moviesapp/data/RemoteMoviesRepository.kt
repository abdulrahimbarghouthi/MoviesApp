package com.abarghouthi.moviesapp.data

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.abarghouthi.moviesapp.data.models.Movie
import com.abarghouthi.moviesapp.data.models.MovieDetailsDto
import com.abarghouthi.moviesapp.data.models.MovieDto
import com.abarghouthi.moviesapp.data.models.MoviesResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import javax.inject.Inject


class RemoteMoviesRepository @Inject constructor(private val moviesService: MoviesService) : MoviesRepository {

    override fun getMoviesFeed(): Flow<PagingData<Movie>> {
       return Pager(
           config = PagingConfig(pageSize = 20, maxSize = 10 * 20),
           pagingSourceFactory = {
               MoviesPagingSource(moviesService)
           }
       ).flow
    }

    override suspend fun getMovieDetailsById(id: Int): Flow<Resource<MovieDetailsDto>> = flow {
        emit(Resource.Loading())
        try {
            val movieDetailsDto = moviesService.getMovieDetails(id)
            emit(Resource.Success(movieDetailsDto))
        } catch (exception: Exception) {
            emit(Resource.Error(exception))
        }
    }

}