package sns.example.snapit.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.ExperimentalPagingApi
import sns.example.snapit.R
import sns.example.snapit.ui.landing.LandingPageActivity
import sns.example.snapit.ui.main.MainActivity
import sns.example.snapit.utils.ConstVal
import sns.example.snapit.utils.SessionManager

@ExperimentalPagingApi
@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var preference: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        preference = SessionManager(this)
        val isLogin = preference.isLogin
        Handler(Looper.getMainLooper()).postDelayed({
            when {
                isLogin -> {
                    MainActivity.start(this)
                    finish()
                }

                else -> {
                    LandingPageActivity.start(this)
                    finish()
                }
            }
        }, ConstVal.LOADING_TIME)
    }
}