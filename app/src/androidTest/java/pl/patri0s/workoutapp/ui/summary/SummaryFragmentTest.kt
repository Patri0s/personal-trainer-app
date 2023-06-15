package pl.patri0s.workoutapp.ui.summary

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.lifecycle.Lifecycle
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.typeText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pl.patri0s.workoutapp.R

@RunWith(AndroidJUnit4::class)
class SummaryFragmentTest : TestCase() {
    private lateinit var scenario: FragmentScenario<SummaryFragment>

    @Before
    fun setup() {
        scenario = launchFragmentInContainer(themeResId = R.style.Theme_WorkoutApp)
        scenario.moveToState(Lifecycle.State.STARTED)
    }

    @Test
    fun should_display_correct_BMI_when_data_are_valid() {
        // given
        val weight = "60"
        val height = "180"

        // when
        onView(withId(R.id.editTv)).perform(click())
        onView(withId(R.id.weightValue)).perform(typeText(weight))
        onView(withId(R.id.heightValue)).perform(typeText(height))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.saveBtn)).perform(click())

        // then
        onView(withId(R.id.descBMI)).check(matches(withText(R.string.bmi_normal)))
    }

    @Test
    fun should_display_incorrect_BMI_when_data_are_invalid() {
        // given
        val weight = "600"
        val height = "180"

        // when
        onView(withId(R.id.editTv)).perform(click())
        onView(withId(R.id.weightValue)).perform(typeText(weight))
        onView(withId(R.id.heightValue)).perform(typeText(height))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.saveBtn)).perform(click())

        // then
        onView(withId(R.id.descBMI)).check(matches(withText(R.string.bmi_invalid_value)))
    }

    @Test
    fun should_display_underweight_when_BMI_is_under_18_and_5_tenths() {
        // given
        val weight = "30"
        val height = "170"

        // when
        onView(withId(R.id.editTv)).perform(click())
        onView(withId(R.id.weightValue)).perform(typeText(weight))
        onView(withId(R.id.heightValue)).perform(typeText(height))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.saveBtn)).perform(click())

        // then
        onView(withId(R.id.descBMI)).check(matches(withText(R.string.bmi_underweight)))
    }

    @Test
    fun should_display_overweight_when_BMI_is_between_25_and_29_9() {
        // given
        val weight = "80"
        val height = "170"

        // when
        onView(withId(R.id.editTv)).perform(click())
        onView(withId(R.id.weightValue)).perform(typeText(weight))
        onView(withId(R.id.heightValue)).perform(typeText(height))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.saveBtn)).perform(click())

        // then
        onView(withId(R.id.descBMI)).check(matches(withText(R.string.bmi_overweight)))
    }

    @Test
    fun should_display_obese_when_BMI_is_between_30_and_34_9() {
        // given
        val weight = "100"
        val height = "170"

        // when
        onView(withId(R.id.editTv)).perform(click())
        onView(withId(R.id.weightValue)).perform(typeText(weight))
        onView(withId(R.id.heightValue)).perform(typeText(height))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.saveBtn)).perform(click())

        // then
        onView(withId(R.id.descBMI)).check(matches(withText(R.string.bmi_obese)))
    }

    @Test
    fun should_display_extreme_obese_when_BMI_is_between_35_and_100() {
        // given
        val weight = "120"
        val height = "170"

        // when
        onView(withId(R.id.editTv)).perform(click())
        onView(withId(R.id.weightValue)).perform(typeText(weight))
        onView(withId(R.id.heightValue)).perform(typeText(height))
        Espresso.closeSoftKeyboard()
        onView(withId(R.id.saveBtn)).perform(click())

        // then
        onView(withId(R.id.descBMI)).check(matches(withText(R.string.bmi_extreme_obese)))
    }
}