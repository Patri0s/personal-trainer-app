package pl.patri0s.workoutapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "summary-table")
data class SummaryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val date: String,
    val trainings: Int,
    val kcal: Int,
    val minutes: Float
)
