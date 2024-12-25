package app.data.database.dao

import androidx.room.*
import app.data.database.entity.DistrictEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DistrictDao {
    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDistricts(districts: List<DistrictEntity>)

    @Transaction
    @Query("SELECT * FROM district")
    fun observerDistricts(): Flow<List<DistrictEntity>>
}