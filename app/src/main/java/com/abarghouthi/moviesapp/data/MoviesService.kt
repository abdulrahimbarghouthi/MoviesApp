package com.abarghouthi.moviesapp.data

import android.content.Context
import androidx.compose.ui.text.intl.Locale
import com.abarghouthi.moviesapp.BuildConfig
import com.abarghouthi.moviesapp.data.models.MovieDetailsDto
import com.abarghouthi.moviesapp.data.models.MovieDto
import com.abarghouthi.moviesapp.data.models.MoviesResponse
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MoviesService {

    @GET("discover/movie")
    suspend fun getMovies(
        @Query("page") page: Int,
        @Query("api_key") moviesApiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = Locale.current.language,
    ): MoviesResponse

    @GET("movie/{movieId}")
    suspend fun getMovieDetails(
        @Path("movieId") movieId: Int,
        @Query("api_key") moviesApiKey: String = BuildConfig.TMDB_API_KEY,
        @Query("language") language: String = Locale.current.language
    ): MovieDetailsDto

    companion object {
        private const val BASE_URL = "https://api.themoviedb.org/3/"
        private const val BASE_POSTER_PATH = "https://image.tmdb.org/t/p/w500"
        private const val BASE_BACKDROP_PATH = "https://image.tmdb.org/t/p/w780"


        fun create(applicationContext: Context): MoviesService {
            val clientBuilder = OkHttpClient.Builder()
                .cache(Cache(applicationContext.cacheDir,10 * 1024 * 1024)) // 5MB
            if (BuildConfig.DEBUG) {
                val logger = HttpLoggingInterceptor().apply {
                    level = HttpLoggingInterceptor.Level.BODY
                }
                clientBuilder.addInterceptor(logger)
            }
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(clientBuilder.build())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(MoviesService::class.java)
        }

        fun getPosterPath(posterPath: String): String {
            return BASE_POSTER_PATH + posterPath
        }

        fun getBackdropPath(backdropPath: String): String {
            return BASE_BACKDROP_PATH + backdropPath
        }

    }
}
