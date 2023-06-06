package pl.patri0s.workoutapp.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bmi-table")
data class BmiEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val BMI: String,
    val weight: String,
    val height: String
)
