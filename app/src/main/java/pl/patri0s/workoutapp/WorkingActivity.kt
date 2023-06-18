package pl.patri0s.workoutapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.speech.tts.TextToSpeech
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import pl.patri0s.workoutapp.databinding.ActivityWorkingBinding
import pl.patri0s.workoutapp.databinding.DialogCustomBackConfirmationBinding
import pl.patri0s.workoutapp.databinding.DialogExerciseInfoBinding
import pl.patri0s.workoutapp.exercise.Exercise
import pl.patri0s.workoutapp.exercise.ExerciseStatusAdapter
import pl.patri0s.workoutapp.exercise.ExerciseList
import pl.patri0s.workoutapp.exerciseSection.SectionAdapter
import pl.patri0s.workoutapp.exerciseSection.SectionList
import java.util.*

class WorkingActivity : AppCompatActivity(), TextToSpeech.OnInitListener {
    private var binding: ActivityWorkingBinding? = null

    private var restTimer: CountDownTimer? = null
    private var restProgress = 0
    private var restTimerDuration: Long = 5 * 1000
    private var pauseOffsetRest: Long = 0

    private var exerciseTimer: CountDownTimer? = null
    private var exerciseProgress = 0
    private var exerciseTimerDuration: Long = 10 * 1000
    private var pauseOffsetExercise: Long = 0

    private var exerciseList: List<Exercise>? = null
    private var currentExercisePosition = -1

    private var tts: TextToSpeech? = null
    private var player: MediaPlayer? = null

    private var exerciseAdapter: ExerciseStatusAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWorkingBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        restTimerDuration = sharedPreferences.getString("restTime", "5")!!.toLong() * 1000

        setSupportActionBar(findViewById(R.id.toolbar))

        if (supportActionBar != null) {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        if (sharedPreferences.getBoolean("keepScreenOn", true)) {
            window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }

        val sectionAdapter = SectionAdapter(SectionList.sectionsList)

        exerciseList = when (sectionAdapter.getItemClickedId()) {
            0 -> ExerciseList.section1ExerciseList
            1 -> ExerciseList.section2ExerciseList
            2 -> ExerciseList.section3ExerciseList
            3 -> ExerciseList.section4ExerciseList
            4 -> ExerciseList.section5ExerciseList
            else -> ExerciseList.section1ExerciseList
        }

        tts = TextToSpeech(this, this, "com.google.android.tts")


        setupRestView()
        setupExerciseStatusRecyclerView()

        binding?.toolbar?.setNavigationOnClickListener {
            customDialogForBackButton()
        }

        binding?.pauseIcon?.setOnClickListener {
            pauseTimers()
        }

        binding?.resumeIcon?.setOnClickListener {
            resumeTimers()
        }

        binding?.exerciseInfoIcon?.setOnClickListener {
            customDialogExerciseInfo()
        }

        onBackPressedDispatcher.addCallback(this) {
            customDialogForBackButton()
        }
    }

    private fun pauseTimers() {
        if (binding?.restTimer?.visibility == View.VISIBLE) {
            restTimer!!.cancel()
        } else if (binding?.exerciseTimer?.visibility == View.VISIBLE) {
            exerciseTimer!!.cancel()
        }
        binding?.pauseIcon?.visibility = View.INVISIBLE
        binding?.resumeIcon?.visibility = View.VISIBLE
    }

    private fun resumeTimers() {
        if (binding?.restTimer?.visibility == View.VISIBLE) {
            setRestProgressBar(pauseOffsetRest)
        } else if (binding?.exerciseTimer?.visibility == View.VISIBLE) {
            setExerciseProgressBar(pauseOffsetExercise)
        }
        binding?.pauseIcon?.visibility = View.VISIBLE
        binding?.resumeIcon?.visibility = View.INVISIBLE
    }

    private fun customDialogForBackButton() {
        pauseTimers()
        val customDialog = Dialog(this)
        val dialogBinding = DialogCustomBackConfirmationBinding.inflate(layoutInflater)
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(false)
        customDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded)
        dialogBinding.btnYes.setOnClickListener {
            this@WorkingActivity.finish()
            customDialog.dismiss()
        }
        dialogBinding.btnNo.setOnClickListener {
            customDialog.dismiss()
            resumeTimers()
        }

        customDialog.show()
    }

    @SuppressLint("SetTextI18n")
    private fun customDialogExerciseInfo() {
        pauseTimers()
        val customDialog = Dialog(this)
        customDialog.window?.setBackgroundDrawableResource(R.drawable.dialog_rounded)
        val dialogBinding = DialogExerciseInfoBinding.inflate(layoutInflater)
        if (binding?.restTimer?.visibility == View.VISIBLE) {
            dialogBinding.exerciseName.text = exerciseList!![currentExercisePosition + 1].getName()
            dialogBinding.exerciseGif.setImageResource(exerciseList!![currentExercisePosition + 1].getGif())
            dialogBinding.exerciseTime.text =
                exerciseList!![currentExercisePosition + 1].getTime().toString() + " s"
            dialogBinding.exerciseDesc.text =
                getText(exerciseList!![currentExercisePosition + 1].getDesc())
        } else if (binding?.exerciseTimer?.visibility == View.VISIBLE) {
            dialogBinding.exerciseName.text = exerciseList!![currentExercisePosition].getName()
            dialogBinding.exerciseGif.setImageResource(exerciseList!![currentExercisePosition].getGif())
            dialogBinding.exerciseTime.text =
                exerciseList!![currentExercisePosition].getTime().toString() + " s"
            dialogBinding.exerciseDesc.text =
                getText(exerciseList!![currentExercisePosition].getDesc())
        }
        customDialog.setContentView(dialogBinding.root)
        customDialog.setCanceledOnTouchOutside(true)
        customDialog.setOnCancelListener {
            resumeTimers()
        }
        customDialog.show()
    }

    private fun setupExerciseStatusRecyclerView() {
        binding?.rvExerciseStatus?.layoutManager =
            GridLayoutManager(this, 12)

        exerciseAdapter = ExerciseStatusAdapter(exerciseList!!)
        binding?.rvExerciseStatus?.adapter = exerciseAdapter
    }

    private fun setRestProgressBar(pauseOffsetRestL: Long) {
        binding?.restProgressBar?.progress = restProgress
        binding?.restProgressBar?.max = restTimerDuration.toInt() / 1000

        restTimer = object : CountDownTimer(restTimerDuration - pauseOffsetRestL, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                pauseOffsetRest = restTimerDuration - millisUntilFinished
                restProgress++
                binding?.restProgressBar?.progress = (millisUntilFinished / 1000).toInt()
                binding?.tvTimer?.text = (millisUntilFinished / 1000).toString()
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onFinish() {
                currentExercisePosition++

                exerciseList!![currentExercisePosition].setIsSelected(true)
                exerciseAdapter!!.notifyDataSetChanged()

                setupExerciseView()
            }

        }.start()
    }

    private fun setExerciseProgressBar(pauseOffsetExerciseL: Long) {
        exerciseTimerDuration = exerciseList!![currentExercisePosition].getTime().toLong() * 1000
        binding?.progressBarExercise?.progress = exerciseProgress
        binding?.progressBarExercise?.max = exerciseTimerDuration.toInt() / 1000

        exerciseTimer =
            object : CountDownTimer(exerciseTimerDuration - pauseOffsetExerciseL, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    when (millisUntilFinished / 1000) {
                        exerciseTimerDuration / 2000 -> speakOut(getString(R.string.working_half_time))
                        3L -> speakOut("3")
                        2L -> speakOut("2")
                        1L -> speakOut("1")
                    }
                    pauseOffsetExercise = exerciseTimerDuration - millisUntilFinished
                    exerciseProgress++
                    binding?.progressBarExercise?.progress = (millisUntilFinished / 1000).toInt()
                    binding?.tvTimerExercise?.text = (millisUntilFinished / 1000).toString()
                }

                @SuppressLint("NotifyDataSetChanged")
                override fun onFinish() {
                    if (currentExercisePosition < exerciseList?.size!! - 1) {
                        exerciseList!![currentExercisePosition].setIsSelected(false)
                        exerciseList!![currentExercisePosition].setIsCompleted(true)
                        exerciseAdapter!!.notifyDataSetChanged()
                        setupRestView()
                    } else {
                        finish()
                        val intent = Intent(this@WorkingActivity, FinishActivity::class.java)
                        startActivity(intent)
                    }
                }
            }.start()
    }

    private fun setupRestView() {
        try {
            val soundURI =
                Uri.parse("android.resource://pl.patri0s.workoutapp/" + R.raw.press_start)
            player = MediaPlayer.create(applicationContext, soundURI)
            player?.isLooping = false
            player?.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding?.restTimer?.visibility = View.VISIBLE
        binding?.readyText?.visibility = View.VISIBLE
        binding?.workText?.visibility = View.INVISIBLE
        binding?.exerciseNameWorkingView?.visibility = View.INVISIBLE
        binding?.exerciseTimer?.visibility = View.INVISIBLE
        binding?.upcomingGif?.visibility = View.VISIBLE
        binding?.exerciseGifWorkingView?.visibility = View.INVISIBLE
        binding?.upcomingExerciseName?.visibility = View.VISIBLE

        if (restTimer != null) {
            val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
            restTimer?.cancel()
            pauseOffsetRest = 0
            restProgress = 0
            restTimerDuration = sharedPreferences.getString("restTime", "5")!!.toLong() * 1000
        }

        binding?.upcomingExerciseName?.text =
            exerciseList!![currentExercisePosition + 1].getName()
        binding?.upcomingGif?.setImageResource(exerciseList!![currentExercisePosition + 1].getGif())

        setRestProgressBar(pauseOffsetRest)
    }

    private fun setupExerciseView() {
        binding?.restTimer?.visibility = View.INVISIBLE
        binding?.readyText?.visibility = View.INVISIBLE
        binding?.workText?.visibility = View.VISIBLE
        binding?.exerciseNameWorkingView?.visibility = View.VISIBLE
        binding?.exerciseTimer?.visibility = View.VISIBLE
        binding?.upcomingGif?.visibility = View.INVISIBLE
        binding?.exerciseGifWorkingView?.visibility = View.VISIBLE
        binding?.upcomingExerciseName?.visibility = View.INVISIBLE

        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
            pauseOffsetExercise = 0
        }

        speakOut(exerciseList!![currentExercisePosition].getName())

        binding?.exerciseGifWorkingView?.setImageResource(exerciseList!![currentExercisePosition].getGif())
        binding?.exerciseNameWorkingView?.text = exerciseList!![currentExercisePosition].getName()

        setExerciseProgressBar(pauseOffsetExercise)
    }

    override fun onDestroy() {
        super.onDestroy()

        if (restTimer != null) {
            restTimer?.cancel()
            restProgress = 0
        }

        if (exerciseTimer != null) {
            exerciseTimer?.cancel()
            exerciseProgress = 0
        }

        if (tts != null) {
            tts!!.stop()
            tts!!.shutdown()
        }

        if (player != null) {
            player!!.stop()
        }

        binding = null

        for (i in exerciseList!!) {
            i.setIsCompleted(false)
            i.setIsSelected(false)
        }
    }

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.getDefault())
            println(tts?.defaultEngine)

            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "The language specified is not supported!")
            }
        } else {
            Log.e("TTS", "Initialization failed!")
        }
    }

    private fun speakOut(text: String) {
        tts!!.speak(text, TextToSpeech.QUEUE_FLUSH, null, "")
    }
}