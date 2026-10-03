package com.brunodegan.androidplayground.domain.getUpComing

import com.brunodegan.androidplayground.base.network.base.Resource
import com.brunodegan.androidplayground.data.datasources.local.entities.UpcomingMoviesEntity
import kotlinx.collections.immutable.ImmutableList
import kotlinx.coroutines.flow.Flow

interface GetUpComingUseCase {
    suspend operator fun invoke(): Flow<Resource<ImmutableList<UpcomingMoviesEntity>>>
}
