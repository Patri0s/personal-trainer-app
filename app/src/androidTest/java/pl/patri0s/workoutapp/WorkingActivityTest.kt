package pl.patri0s.workoutapp

import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.ext.junit.runners.AndroidJUnit4
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import pl.patri0s.workoutapp.activities.WorkingActivity


@RunWith(AndroidJUnit4::class)
class WorkingActivityTest : TestCase() {
    @Before
    fun setup() {
        launchActivity<WorkingActivity>()
    }

    @Test
    fun should_pause_timer_when_pause_icon_clicked() {
        // when
        Espresso.onView(ViewMatchers.withId(R.id.pauseIcon)).perform(ViewActions.click())

        //then
        Espresso.onView(ViewMatchers.withId(R.id.resumeIcon))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }

    @Test
    fun should_resume_timer_when_resume_icon_clicked() {
        // when
        Espresso.onView(ViewMatchers.withId(R.id.pauseIcon)).perform(ViewActions.click())
        Espresso.onView(ViewMatchers.withId(R.id.resumeIcon)).perform(ViewActions.click())

        //then
        Espresso.onView(ViewMatchers.withId(R.id.pauseIcon))
            .check(ViewAssertions.matches(ViewMatchers.withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)))
    }
}