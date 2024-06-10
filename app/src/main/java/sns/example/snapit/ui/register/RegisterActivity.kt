package sns.example.snapit.ui.register

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sns.example.snapit.R
import sns.example.snapit.data.remote.api.ApiResponse
import sns.example.snapit.data.remote.auth.AuthBody
import sns.example.snapit.databinding.ActivityRegisterBinding
import sns.example.snapit.ui.login.LoginActivity
import sns.example.snapit.utils.ConstVal
import sns.example.snapit.utils.ext.isEmailValid
import sns.example.snapit.utils.ext.showOKDialog
import sns.example.snapit.utils.ext.showToast

@ExperimentalPagingApi
@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private val registerViewModel: RegisterViewModel by viewModels()

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initAction()
    }

    private fun initAction() {
        binding.btnRegister.setOnClickListener {
            val userName = binding.editName.text.toString()
            val userEmail = binding.editEmail.text.toString()
            val userPassword = binding.editPassword.text.toString()

            Handler(Looper.getMainLooper()).postDelayed({
                when {
                    userName.isBlank() -> binding.editName.error =
                        getString(R.string.error_empty_name)

                    userEmail.isBlank() -> binding.editEmail.error =
                        getString(R.string.error_empty_email)

                    !userEmail.isEmailValid() -> binding.editEmail.error =
                        getString(R.string.error_invalid_email)

                    userPassword.isBlank() -> binding.editPassword.error =
                        getString(R.string.error_empty_password)

                    else -> {
                        val request = AuthBody(
                            userName, userEmail, userPassword
                        )
                        registerUser(request)
                    }
                }
            }, ConstVal.ACTION_DELAYED_TIME)
        }
        binding.tvToLogin.setOnClickListener {
            LoginActivity.start(this)
        }
    }

    private fun registerUser(newUser: AuthBody) {
        lifecycleScope.launch {
            registerViewModel.userRegister(newUser).collect { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        showLoading(true)
                    }

                    is ApiResponse.Success -> {
                        try {
                            showLoading(false)
                        } finally {
                            LoginActivity.start(this@RegisterActivity)
                            finish()
                            showToast(getString(R.string.register_success))
                        }
                    }

                    is ApiResponse.Error -> {
                        showLoading(false)
                        showOKDialog(getString(R.string.title_dialog_error), response.errorMessage)
                    }

                    else -> {
                        showToast(getString(R.string.message_unknown_state))
                    }
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.apply {
            progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            editName.isClickable = !isLoading
            editEmail.isEnabled = !isLoading
            editPassword.isClickable = !isLoading
            editPassword.isEnabled = !isLoading
            btnRegister.isClickable = !isLoading
            editName.isEnabled = !isLoading
            editEmail.isClickable = !isLoading
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, RegisterActivity::class.java)
            context.startActivity(intent)
        }
    }
}