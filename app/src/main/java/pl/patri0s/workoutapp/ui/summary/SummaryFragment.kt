package pl.patri0s.workoutapp.ui.summary

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.coroutines.launch
import pl.patri0s.workoutapp.R
import pl.patri0s.workoutapp.database.BmiEntity
import pl.patri0s.workoutapp.database.SummaryDao
import pl.patri0s.workoutapp.database.WorkOutApp
import pl.patri0s.workoutapp.databinding.DialogBmiBinding
import pl.patri0s.workoutapp.databinding.FragmentSummaryBinding


class SummaryFragment : Fragment() {

    private var _binding: FragmentSummaryBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSummaryBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val summaryDao = (requireActivity().application as WorkOutApp).db.summaryDao()
        setAllSummaryData(summaryDao)
        binding.editTv.setOnClickListener {
            bmiDialog()
        }

        lifecycleScope.launch {
            summaryDao.fetchBmiData().collect { bmiList ->
                if (bmiList.isNotEmpty()) {
                    val bmi = bmiList[bmiList.lastIndex].BMI
                    val bmiLabel: Int
                    var bmiColor: Int = R.color.bmi_normal
                    when (bmi.replace(',', '.').toFloat()) {
                        in 0.0f..18.4f -> {
                            bmiLabel = R.string.bmi_underweight
                            bmiColor = R.color.bmi_underweight
                        }

                        in 18.5f..24.9f -> {
                            bmiLabel = R.string.bmi_normal
                            bmiColor = R.color.bmi_normal
                        }

                        in 25f..29.9f -> {
                            bmiLabel = R.string.bmi_overweight
                            bmiColor = R.color.bmi_overweight
                        }

                        in 30.0f..34.9f -> {
                            bmiLabel = R.string.bmi_obese
                            bmiColor = R.color.bmi_obese
                        }

                        in 35.0f..100.0f -> {
                            bmiLabel = R.string.bmi_extreme_obese
                            bmiColor = R.color.bmi_extreme_obese
                        }

                        else -> {
                            bmiLabel = R.string.bmi_invalid_value
                        }
                    }
                    binding.descBMI.text = context?.getText(bmiLabel)
                    binding.descBMI.setTextColor(
                        ContextCompat.getColor(
                            requireContext().applicationContext,
                            bmiColor
                        )
                    )
                    binding.resultBMI.setTextColor(
                        ContextCompat.getColor(
                            requireContext().applicationContext,
                            bmiColor
                        )
                    )
                    binding.resultBMI.text = bmi
                }
            }
        }

        setPieChart()

        return root
    }

    private fun setAllSummaryData(summaryDao: SummaryDao) {
        var kcalSum = 0
        var minutesSum = 0.0
        lifecycleScope.launch {
            summaryDao.fetchAllDates().collect { summaryList ->
                if (summaryList.isNotEmpty()) {
                    summaryList.forEach {
                        kcalSum += it.kcal
                        minutesSum += it.minutes
                    }
                    binding.exercisesDone.text = summaryList.size.toString()
                    binding.kcal.text = kcalSum.toString()
                    binding.minutes.text = String.format("%.2f", minutesSum)
                }
            }
        }
    }

    fun validateMetricUnits(dialogBinding: DialogBmiBinding?): Boolean {
        var isValid = true

        if (dialogBinding?.weightValue?.text.toString().isEmpty()) {
            isValid = false
        } else if (dialogBinding?.heightValue?.text.toString().isEmpty()) {
            isValid = false
        }

        return isValid
    }

    private fun calculateUnits(dialogBinding: DialogBmiBinding) {
        val summaryDao = (requireActivity().application as WorkOutApp).db.summaryDao()
        if (validateMetricUnits(dialogBinding)) {
            val weightValue: Float = dialogBinding.weightValue.text.toString().toFloat()

            val heightValue: Float =
                dialogBinding.heightValue.text.toString().toFloat() / 100

            val bmi = weightValue / (heightValue * heightValue)

            lifecycleScope.launch {
                summaryDao.insert(
                    BmiEntity(
                        BMI = String.format("%.2f", bmi),
                        weight = weightValue.toString(),
                        height = (heightValue * 100).toString()
                    )
                )
            }
        } else {
            Toast.makeText(this.context, getString(R.string.bmi_invalid_toast), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun bmiDialog() {
        val customDialog = this.context?.let { Dialog(it) }
        val dialogBinding = DialogBmiBinding.inflate(layoutInflater)
        customDialog?.setContentView(dialogBinding.root)
        customDialog?.setCanceledOnTouchOutside(false)
        customDialog?.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded)
        dialogBinding.saveBtn.setOnClickListener {
            calculateUnits(dialogBinding)
            customDialog?.dismiss()
        }
        dialogBinding.dismissBtn.setOnClickListener {
            customDialog?.dismiss()
        }

        customDialog?.show()
    }

    private fun setPieChart() {
        val pieChart: PieChart = binding.chartBMI

        val list: ArrayList<PieEntry> = ArrayList()
        list.add(PieEntry(18.5f, "< 18,5"))
        list.add(PieEntry(6.4f, "18,5 - 24,9"))
        list.add(PieEntry(4.9f, "25 - 29,9"))
        list.add(PieEntry(4.9f, "30 - 34,9"))
        list.add(PieEntry(18.5f, "35 <"))

        val pieDataSet = PieDataSet(list, "BMI SCALE")
        val colorsBMI = intArrayOf(
            Color.parseColor("#93b4d7"),
            Color.parseColor("#8fc69f"),
            Color.parseColor("#fad548"),
            Color.parseColor("#e7985f"),
            Color.parseColor("#d55c5b")
        )
        pieDataSet.setColors(colorsBMI, 245)
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.sliceSpace = 5f
        pieDataSet.setDrawValues(false)

        val pieData = PieData(pieDataSet)

        pieChart.data = pieData
        pieChart.description.isEnabled = false
        pieChart.legend.isEnabled = false
        pieChart.centerText = getString(R.string.bmi_scale)
        pieChart.rotationAngle = 210f
        pieChart.setCenterTextSize(20.0f)
        pieChart.setEntryLabelTextSize(14f)
        pieChart.animateY(1000)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}