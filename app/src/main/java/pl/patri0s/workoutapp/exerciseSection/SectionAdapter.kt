package pl.patri0s.workoutapp.exerciseSection

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import pl.patri0s.workoutapp.R
import pl.patri0s.workoutapp.activities.SectionExerciseActivity
import pl.patri0s.workoutapp.databinding.ItemExerciseSectionBinding

private var itemClickedId: Int? = null

class SectionAdapter(private val sectionsList: List<ExerciseSection>) :
    RecyclerView.Adapter<SectionAdapter.MainViewHolder>() {
    inner class MainViewHolder(private val itemBinding: ItemExerciseSectionBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(exerciseSection: ExerciseSection) {
            itemBinding.sectionTitle.text =
                itemBinding.root.context.getString(exerciseSection.sectionTitle)
            itemBinding.sectionTrainingsDesc.text = itemBinding.root.context.getString(
                R.string.section_desc_trainings,
                exerciseSection.sectionTrainings
            )
            itemBinding.sectionCaloriesDesc.text = exerciseSection.sectionKcal.toString()
            itemBinding.sectionTimeDesc.text = itemBinding.root.context.getString(
                R.string.section_time,
                exerciseSection.sectionTime
            )
            itemBinding.sectionImg.setImageResource(exerciseSection.sectionImg)
            itemBinding.sectionButton.setOnClickListener {
                itemClickedId = adapterPosition
                val intent = Intent(it.context, SectionExerciseActivity::class.java)
                it.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        return MainViewHolder(
            ItemExerciseSectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return sectionsList.size
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val exerciseSection = sectionsList[position]
        holder.bindItem(exerciseSection)
    }

    fun getItemClickedId(): Int? {
        return itemClickedId
    }

}