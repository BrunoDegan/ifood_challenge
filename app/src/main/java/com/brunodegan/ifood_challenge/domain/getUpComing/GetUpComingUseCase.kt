package com.brunodegan.ifood_challenge.domain.getUpComing

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.datasources.local.entities.UpcomingMoviesEntity
import kotlinx.coroutines.flow.Flow

interface GetUpComingUseCase {
    suspend operator fun invoke(): Flow<Resource<List<UpcomingMoviesEntity>>>
}