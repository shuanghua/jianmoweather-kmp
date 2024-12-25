package app.data.repo

import AppDispatcher
import app.data.database.LocalDataSource
import app.data.model.City
import app.data.model.Province
import app.data.model.ProvinceCity
import app.data.network.NetworkDataSource
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


interface ProvinceCityRepository {
    suspend fun updateAllProvinceWithCity()
    fun observerProvinces(): Flow<List<Province>>
    fun observerCities(provinceName: String): Flow<List<City>>
    suspend fun saveFavoriteCity(city: City)
}

class ProvinceCityRepositoryImpl(
    private val database: LocalDataSource,
    private val network: NetworkDataSource,
    private val dispatchers: AppDispatcher,
) : ProvinceCityRepository {

    override suspend fun updateAllProvinceWithCity() = withContext(dispatchers.io) {
        val provinceCity: ProvinceCity = network.getProvincesWithCities()// 网络请求
//		val networkModel: ProvinceCityModel = network.getProvinceCityListFromAssets()// 使用本地 json 文件

        val job = coroutineScope { // 失败取消全部
            launch { database.insertProvinces(provinceCity.provinces) }
            launch { database.insertCities(provinceCity.cities) }
        }
        job.join()
    }

    override fun observerProvinces(): Flow<List<Province>> {
        val provincesFlow = database.observerProvinces()
        return provincesFlow
            .onStart {
                if (provincesFlow.first().isEmpty()) updateAllProvinceWithCity()
            }
            .flowOn(dispatchers.io)
    }

    override fun observerCities(provinceName: String): Flow<List<City>> {
        return database.observerCities(provinceName)
    }

    override suspend fun saveFavoriteCity(city: City) {
        database.insertCityToFavorite(city)
    }
}

