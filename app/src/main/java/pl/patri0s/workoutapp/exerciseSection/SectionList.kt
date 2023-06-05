package pl.patri0s.workoutapp.exerciseSection

import pl.patri0s.workoutapp.R
import pl.patri0s.workoutapp.exercise.Exercise
import pl.patri0s.workoutapp.exercise.ExerciseList

object SectionList {
    val sectionsList = listOf(
        ExerciseSection(
            R.string.section_1_title,
            ExerciseList.section1ExerciseList.size,
            calculateSectionTime(ExerciseList.section1ExerciseList) * 9,
            calculateSectionTime(ExerciseList.section1ExerciseList),
            R.drawable.section_1_img
        ),
        ExerciseSection(
            R.string.section_2_title,
            ExerciseList.section2ExerciseList.size,
            calculateSectionTime(ExerciseList.section2ExerciseList) * 9,
            calculateSectionTime(ExerciseList.section2ExerciseList),
            R.drawable.section_2_img
        ),
        ExerciseSection(
            R.string.section_3_title,
            ExerciseList.section3ExerciseList.size,
            calculateSectionTime(ExerciseList.section3ExerciseList) * 8,
            calculateSectionTime(ExerciseList.section3ExerciseList),
            R.drawable.section_3_img
        ),
        ExerciseSection(
            R.string.section_4_title,
            ExerciseList.section4ExerciseList.size,
            calculateSectionTime(ExerciseList.section4ExerciseList) * 9,
            calculateSectionTime(ExerciseList.section4ExerciseList),
            R.drawable.section_4_img
        ),
        ExerciseSection(
            R.string.section_5_title,
            ExerciseList.section5ExerciseList.size,
            calculateSectionTime(ExerciseList.section5ExerciseList) * 9,
            calculateSectionTime(ExerciseList.section5ExerciseList),
            R.drawable.section_5_img
        ),

        )

    private fun calculateSectionTime(sectionList: List<Exercise>): Int {
        var sectionTime = 0
        sectionList.forEach {
            sectionTime += it.getTime()
        }
        return sectionTime / 60
    }
}