package com.abarghouthi.moviesapp.di

import android.content.Context
import com.abarghouthi.moviesapp.data.MoviesRepository
import com.abarghouthi.moviesapp.data.MoviesService
import com.abarghouthi.moviesapp.data.RemoteMoviesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class AppModule {

    @Singleton
    @Provides
    fun provideMoviesService(@ApplicationContext applicationContext: Context): MoviesService {
        return MoviesService.create(applicationContext)
    }

    @Singleton
    @Provides
    fun provideMoviesRepository(moviesService: MoviesService): MoviesRepository {
        return RemoteMoviesRepository(moviesService)
    }

}