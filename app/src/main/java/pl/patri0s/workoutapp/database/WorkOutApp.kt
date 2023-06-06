package pl.patri0s.workoutapp.database

import android.app.Application

class WorkOutApp : Application() {
    val db by lazy {
        SummaryDatabase.getInstance(this)
    }
}