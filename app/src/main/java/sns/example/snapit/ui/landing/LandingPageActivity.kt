package sns.example.snapit.ui.landing

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.AndroidEntryPoint
import sns.example.snapit.databinding.ActivityLandingPageBinding
import sns.example.snapit.ui.login.LoginActivity
import sns.example.snapit.ui.register.RegisterActivity

@ExperimentalPagingApi
@AndroidEntryPoint
class LandingPageActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLandingPageBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLandingPageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAction()
    }

    private fun setupAction() {
        binding.btnLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        binding.btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LandingPageActivity::class.java)
            context.startActivity(intent)
        }
    }
}