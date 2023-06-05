package pl.patri0s.workoutapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import pl.patri0s.workoutapp.databinding.ActivitySectionExerciseBinding
import pl.patri0s.workoutapp.databinding.FragmentHomeBinding
import pl.patri0s.workoutapp.exercise.ExerciseAdapter
import pl.patri0s.workoutapp.exercise.ExerciseList
import pl.patri0s.workoutapp.exerciseSection.SectionAdapter
import pl.patri0s.workoutapp.exerciseSection.SectionList

class SectionExerciseActivity : AppCompatActivity() {

    private var binding: ActivitySectionExerciseBinding? = null
    private var sectionBinding: FragmentHomeBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var exerciseAdapter: ExerciseAdapter? = null
        val sectionAdapter = SectionAdapter(SectionList.sectionsList)

        when (sectionAdapter.getItemClickedId()) {
            0 -> {
                exerciseAdapter = ExerciseAdapter(ExerciseList.section1ExerciseList)
                title =
                    getText(SectionList.sectionsList[sectionAdapter.getItemClickedId()!!].sectionTitle)
            }

            1 -> {
                exerciseAdapter = ExerciseAdapter(ExerciseList.section2ExerciseList)
                title =
                    getText(SectionList.sectionsList[sectionAdapter.getItemClickedId()!!].sectionTitle)
            }

            2 -> {
                exerciseAdapter = ExerciseAdapter(ExerciseList.section3ExerciseList)
                title =
                    getText(SectionList.sectionsList[sectionAdapter.getItemClickedId()!!].sectionTitle)
            }

            3 -> {
                exerciseAdapter = ExerciseAdapter(ExerciseList.section4ExerciseList)
                title =
                    getText(SectionList.sectionsList[sectionAdapter.getItemClickedId()!!].sectionTitle)
            }

            4 -> {
                exerciseAdapter = ExerciseAdapter(ExerciseList.section5ExerciseList)
                title =
                    getText(SectionList.sectionsList[sectionAdapter.getItemClickedId()!!].sectionTitle)
            }
        }

        binding = ActivitySectionExerciseBinding.inflate(layoutInflater)
        sectionBinding = FragmentHomeBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        setSupportActionBar(findViewById(R.id.toolbar))
        binding?.toolbarLayout?.title = title

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        binding?.toolbar?.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        binding?.sectionExercisesRv?.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding?.sectionExercisesRv?.adapter = exerciseAdapter
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
        sectionBinding = null
    }
}