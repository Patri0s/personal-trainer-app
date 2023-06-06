package pl.patri0s.workoutapp

import android.icu.text.SimpleDateFormat
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import pl.patri0s.workoutapp.database.SummaryDao
import pl.patri0s.workoutapp.database.SummaryEntity
import pl.patri0s.workoutapp.database.WorkOutApp
import pl.patri0s.workoutapp.databinding.ActivityFinishBinding
import pl.patri0s.workoutapp.exercise.Exercise
import pl.patri0s.workoutapp.exercise.ExerciseList
import pl.patri0s.workoutapp.exerciseSection.SectionAdapter
import pl.patri0s.workoutapp.exerciseSection.SectionList
import java.util.*

class FinishActivity : AppCompatActivity() {
    private var binding: ActivityFinishBinding? = null
    private var player: MediaPlayer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFinishBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val soundURI =
            Uri.parse("android.resource://pl.patri0s.workoutapp/" + R.raw.finish)
        player = MediaPlayer.create(applicationContext, soundURI)
        player?.isLooping = false
        player?.start()

        binding?.btnFinish?.setOnClickListener {
            finish()
        }

        val summaryDao = (application as WorkOutApp).db.summaryDao()
        addDateToDatabase(summaryDao)
    }

    private fun addDateToDatabase(summaryDao: SummaryDao) {
        val c = Calendar.getInstance()
        val dateTime = c.time

//        val sdf = SimpleDateFormat("dd MMM yyyy HH:mm:ss", Locale.getDefault())
        val sdf = SimpleDateFormat("dd MM yyyy", Locale.getDefault())
        val date = sdf.format(dateTime)

        lifecycleScope.launch {
            val sectionAdapter = SectionAdapter(SectionList.sectionsList)
            lateinit var sectionClicked: List<Exercise>

            when (sectionAdapter.getItemClickedId()) {
                0 -> sectionClicked = ExerciseList.section1ExerciseList
                1 -> sectionClicked = ExerciseList.section2ExerciseList
                2 -> sectionClicked = ExerciseList.section3ExerciseList
                3 -> sectionClicked = ExerciseList.section4ExerciseList
                4 -> sectionClicked = ExerciseList.section5ExerciseList
            }


            var exercisesTimeInMinutes = 0
            sectionClicked.forEach {
                exercisesTimeInMinutes += it.exerciseTimeInSecond
            }
            summaryDao.insert(
                SummaryEntity(
                    date = date,
                    trainings = sectionClicked.size,
                    kcal = SectionList.sectionsList[sectionAdapter.getItemClickedId()!!].sectionKcal,
                    minutes = exercisesTimeInMinutes.toFloat() / 60
                )
            )
            Log.e("Date: ", "Added...")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}