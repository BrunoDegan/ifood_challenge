package com.brunodegan.ifood_challenge.domain.getNowPlaying

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.NowPlayingMoviesEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface GetNowPlayingUseCase {
    suspend operator fun invoke(): Flow<Resource<ImmutableList<NowPlayingMoviesEntity>>>
}
