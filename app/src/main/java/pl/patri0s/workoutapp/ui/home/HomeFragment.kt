package pl.patri0s.workoutapp.ui.home

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import pl.patri0s.workoutapp.R
import pl.patri0s.workoutapp.databinding.FragmentHomeBinding
import pl.patri0s.workoutapp.exerciseSection.SectionAdapter
import pl.patri0s.workoutapp.exerciseSection.SectionList

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    private lateinit var activity: AppCompatActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as AppCompatActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        setFeelingButtons()

        val sectionAdapter = SectionAdapter(SectionList.sectionsList)
        binding.exerciseSectionRv.layoutManager =
            LinearLayoutManager(this.context, LinearLayoutManager.VERTICAL, false)
        binding.exerciseSectionRv.adapter = sectionAdapter

        activity.supportActionBar?.subtitle = getString(R.string.nav_home)

        return root
    }

    private fun setFeelingButtons() {
        binding.btnNormal.setOnClickListener {
            clearFeelingButtons()
            binding.btnReady.alpha = 0.5f
            binding.btnTired.alpha = 0.5f
            binding.characterReady.alpha = 0.5f
            binding.characterTired.alpha = 0.5f
        }
        binding.btnReady.setOnClickListener {
            clearFeelingButtons()
            binding.btnNormal.alpha = 0.5f
            binding.btnTired.alpha = 0.5f
            binding.characterNormal.alpha = 0.5f
            binding.characterTired.alpha = 0.5f
        }
        binding.btnTired.setOnClickListener {
            clearFeelingButtons()
            binding.btnNormal.alpha = 0.5f
            binding.btnReady.alpha = 0.5f
            binding.characterNormal.alpha = 0.5f
            binding.characterReady.alpha = 0.5f
        }
    }

    private fun clearFeelingButtons() {
        binding.btnNormal.alpha = 1f
        binding.btnTired.alpha = 1f
        binding.btnReady.alpha = 1f
        binding.characterNormal.alpha = 1f
        binding.characterTired.alpha = 1f
        binding.characterReady.alpha = 1f
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}