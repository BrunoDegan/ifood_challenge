package com.brunodegan.ifood_challenge.domain.getTopRated

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.TopRatedMoviesEntity
import kotlinx.coroutines.flow.Flow

interface GetTopRatedUseCase {
    suspend operator fun invoke(): Flow<Resource<List<TopRatedMoviesEntity>>>
}