package com.brunodegan.ifood_challenge.domain.getNowPlaying

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.NowPlayingMoviesEntity
import com.brunodegan.ifood_challenge.data.repositories.MoviesRepository
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class GetNowPlayingUseCaseImpl(
    private val repository: MoviesRepository,
) : GetNowPlayingUseCase {
    override suspend fun invoke(): Flow<Resource<List<NowPlayingMoviesEntity>>> =
        repository.getNowPlayingMovies()

}