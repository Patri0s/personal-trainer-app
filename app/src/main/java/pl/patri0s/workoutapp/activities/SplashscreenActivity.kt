package pl.patri0s.workoutapp.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import pl.patri0s.workoutapp.R
import pl.patri0s.workoutapp.databinding.ActivitySplashscreenBinding

@SuppressLint("CustomSplashScreen")
class SplashscreenActivity : AppCompatActivity() {
    private var binding: ActivitySplashscreenBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashscreenBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        binding?.appIcon?.animation = AnimationUtils.loadAnimation(this, R.anim.up)
        binding?.appName?.animation = AnimationUtils.loadAnimation(this, R.anim.down)

        Handler(Looper.getMainLooper()).postDelayed({
            run {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }, 2000)
    }
}