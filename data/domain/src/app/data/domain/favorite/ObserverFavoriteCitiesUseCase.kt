package app.data.domain.favorite

import app.data.model.FavoriteCity
import app.data.repo.FavoritesRepository
import kotlinx.coroutines.flow.Flow


class ObserverFavoriteCitiesUseCase(
	private val favoriteRepository: FavoritesRepository,
) {
	suspend operator fun invoke(): Flow<List<FavoriteCity>> {
		return favoriteRepository.observerFavoriteCities()
	}
}