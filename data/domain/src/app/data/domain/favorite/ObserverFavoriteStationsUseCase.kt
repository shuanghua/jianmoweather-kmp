package app.data.domain.favorite

import app.data.model.Province
import app.data.repo.ProvinceCityRepository
import dev.shuanghua.weather.shared.ObservableUseCase
import kotlinx.coroutines.flow.Flow


class ObserverFavoriteStationsUseCase(
	private val provinceRepository: ProvinceCityRepository
) : ObservableUseCase<Unit, List<Province>>() {

	override fun createObservable(params: Unit): Flow<List<Province>> {
		return provinceRepository.observerProvinces()
	}
}