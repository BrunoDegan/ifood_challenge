package com.brunodegan.ifood_challenge.domain.getPopular

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.PopularMoviesEntity
import kotlinx.coroutines.flow.Flow

interface GetPopularUseCase {
    suspend operator fun invoke(): Flow<Resource<List<PopularMoviesEntity>>>
}