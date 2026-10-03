package com.brunodegan.androidplayground.domain.getNowPlaying

import com.brunodegan.androidplayground.base.network.base.Resource
import com.brunodegan.androidplayground.data.datasources.local.entities.NowPlayingMoviesEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface GetNowPlayingUseCase {
    suspend operator fun invoke(): Flow<Resource<ImmutableList<NowPlayingMoviesEntity>>>
}
