package pl.patri0s.workoutapp.ui.summary

import android.app.Dialog
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import pl.patri0s.workoutapp.R
import pl.patri0s.workoutapp.databinding.DialogBmiBinding

class SummaryFragmentTest {
    private var binding: DialogBmiBinding? = null

    @Before
    fun setup() {
        val dialog = SummaryFragment().context?.let { Dialog(it) }
        dialog?.setContentView(R.layout.dialog_bmi)
        binding = dialog?.let { DialogBmiBinding.bind(it.findViewById(R.layout.dialog_bmi)) }
    }

    @Test
    fun should_return_true_when_data_are_valid() {
        binding?.heightValue?.setText("180")
        binding?.weightValue?.setText("65")
        Assert.assertTrue(SummaryFragment().validateMetricUnits(binding))
    }
}