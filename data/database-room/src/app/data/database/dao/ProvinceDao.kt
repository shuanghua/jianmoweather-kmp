package app.data.database.dao

import androidx.room.*
import app.data.database.entity.ProvinceEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProvinceDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProvince(provinces: List<ProvinceEntity>)

    @Transaction
    @Query("SELECT * FROM province")
    fun observerProvinces(): Flow<List<ProvinceEntity>>
}