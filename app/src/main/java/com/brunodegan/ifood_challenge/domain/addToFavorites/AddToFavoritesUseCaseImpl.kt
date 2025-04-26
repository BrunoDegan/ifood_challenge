package com.brunodegan.ifood_challenge.domain.addToFavorites

import com.brunodegan.ifood_challenge.base.network.base.Resource
import com.brunodegan.ifood_challenge.data.repositories.MoviesRepository
import com.brunodegan.ifood_challenge.ui.screen.favoriteMovies.model.FavoriteMoviesViewData
import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Factory

@Factory
class AddToFavoritesUseCaseImpl(private val repository: MoviesRepository) : AddToFavoritesUseCase {
    override suspend fun invoke(id: Int): Flow<Resource<FavoriteMoviesViewData>> =
        repository.addFavorite(id = id)
}