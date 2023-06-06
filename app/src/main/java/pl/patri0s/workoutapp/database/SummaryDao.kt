package pl.patri0s.workoutapp.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SummaryDao {
    @Insert
    suspend fun insert(summaryEntity: SummaryEntity)

    @Insert
    suspend fun insert(bmiEntity: BmiEntity)

    @Delete
    suspend fun delete(allData: List<SummaryEntity>)

    @Delete
    suspend fun deleteBmi(allData: List<BmiEntity>)

    @Query("SELECT * FROM `summary-table`")
    fun fetchAllDates(): Flow<List<SummaryEntity>>

    @Query("SELECT * FROM `bmi-table`")
    fun fetchBmiData(): Flow<List<BmiEntity>>
}