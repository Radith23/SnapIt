package sns.example.snapit.ui.profile

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.paging.ExperimentalPagingApi
import sns.example.snapit.R
import sns.example.snapit.databinding.ActivityProfileBinding
import sns.example.snapit.ui.landing.LandingPageActivity
import sns.example.snapit.utils.SessionManager

@ExperimentalPagingApi
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var pref: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = SessionManager(this)

        initUI()
        initAction()
    }

    private fun initUI() {
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        title = getString(R.string.title_profile)

        binding.tvUserName.text = pref.getUserName
        binding.tvUserEmail.text = pref.getEmail
    }

    private fun initAction() {
        binding.btnLogout.setOnClickListener {
            openLogoutDial()
        }
    }

    private fun openLogoutDial() {
        val alertDialog = AlertDialog.Builder(this)
        alertDialog.setTitle(getString(R.string.message_logout_confirm))
            ?.setPositiveButton(getString(R.string.logout)) { _, _ ->
                pref.clearPreferences()
                val intent = Intent(this, LandingPageActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
                finish()
            }
            ?.setNegativeButton(getString(R.string.action_cancel), null)
        val alert = alertDialog.create()
        alert.show()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, ProfileActivity::class.java)
            context.startActivity(intent)
        }
    }
}