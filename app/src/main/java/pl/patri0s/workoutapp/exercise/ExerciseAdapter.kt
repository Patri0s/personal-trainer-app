package pl.patri0s.workoutapp.exercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.patri0s.workoutapp.R
import pl.patri0s.workoutapp.databinding.ItemExerciseOnListBinding

class ExerciseAdapter(private val exercisesList: List<Exercise>) :
    RecyclerView.Adapter<ExerciseAdapter.MainViewHolder>() {
    inner class MainViewHolder(private val itemBinding: ItemExerciseOnListBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun bindItem(exercise: Exercise) {
            itemBinding.exerciseName.text = exercise.exerciseName
            if (exercise.exerciseTimeInSecond >= 60 && exercise.exerciseTimeInSecond % 60 < 10) {
                itemBinding.exerciseTime.text = itemBinding.root.context.getString(
                    R.string.exercise_section_time_1,
                    exercise.exerciseTimeInSecond % 60
                )
            } else {
                itemBinding.exerciseTime.text = itemBinding.root.context.getString(
                    R.string.exercise_section_time,
                    exercise.exerciseTimeInSecond % 60
                )
            }
            itemBinding.exerciseGif.setImageResource(exercise.exerciseGif)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            ItemExerciseOnListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return exercisesList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val exercise = exercisesList[position]
        holder.bindItem(exercise)
    }
}