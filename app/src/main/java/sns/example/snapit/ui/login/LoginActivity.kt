package sns.example.snapit.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import sns.example.snapit.R
import sns.example.snapit.data.remote.api.ApiResponse
import sns.example.snapit.data.remote.auth.LoginBody
import sns.example.snapit.databinding.ActivityLoginBinding
import sns.example.snapit.ui.main.MainActivity
import sns.example.snapit.ui.register.RegisterActivity
import sns.example.snapit.utils.ConstVal.KEY_EMAIL
import sns.example.snapit.utils.ConstVal.KEY_IS_LOGIN
import sns.example.snapit.utils.ConstVal.KEY_TOKEN
import sns.example.snapit.utils.ConstVal.KEY_USER_ID
import sns.example.snapit.utils.ConstVal.KEY_USER_NAME
import sns.example.snapit.utils.SessionManager
import sns.example.snapit.utils.ext.gone
import sns.example.snapit.utils.ext.show
import sns.example.snapit.utils.ext.showOKDialog
import sns.example.snapit.utils.ext.showToast

@ExperimentalPagingApi
@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivityLoginBinding
    private lateinit var pref: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = SessionManager(this)

        initAction()
    }

    private fun loginUser(loginBody: LoginBody, email: String) {
        lifecycleScope.launch {
            loginViewModel.userLogin(loginBody).collect() { response ->
                when (response) {
                    is ApiResponse.Loading -> {
                        showLoading(true)
                    }

                    is ApiResponse.Success -> {
                        try {
                            showLoading(false)
                            val userData = response.data.loginResult
                            pref.apply {
                                setStringPreference(KEY_USER_ID, userData.userId)
                                setStringPreference(KEY_TOKEN, userData.token)
                                setStringPreference(KEY_USER_NAME, userData.name)
                                setStringPreference(KEY_EMAIL, email)
                                setBooleanPreference(KEY_IS_LOGIN, true)
                            }
                        } finally {
                            val intent = Intent(this@LoginActivity, MainActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                            startActivity(intent)
                            showToast(getString(R.string.login_success))
                            finish()
                        }
                    }

                    is ApiResponse.Error -> {
                        showLoading(false)
                        showOKDialog(
                            getString(R.string.title_dialog_error),
                            getString(R.string.message_incorrect_auth)
                        )
                    }

                    else -> {
                        showToast(getString(R.string.message_unknown_state))
                    }
                }
            }
        }
    }

    private fun initAction() {
        binding.btnLogin.setOnClickListener {
            val userEmail = binding.editEmail.text.toString()
            val userPassword = binding.editPassword.text.toString()

            when {
                userEmail.isBlank() -> {
                    binding.editEmail.requestFocus()
                    binding.editEmail.error = getString(R.string.error_empty_email)
                }

                userPassword.isBlank() -> {
                    binding.editPassword.requestFocus()
                    binding.editPassword.error = getString(R.string.error_empty_password)
                }

                else -> {
                    val request = LoginBody(
                        userEmail, userPassword
                    )

                    loginUser(request, userEmail)
                }
            }
        }
        binding.tvToRegister.setOnClickListener {
            RegisterActivity.start(this)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) binding.progressBar.show() else binding.progressBar.gone()
        binding.apply {
            editEmail.isClickable = !isLoading
            editEmail.isEnabled = !isLoading
            editPassword.isClickable = !isLoading
            editPassword.isEnabled = !isLoading
            btnLogin.isClickable = !isLoading
        }
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, LoginActivity::class.java)
            context.startActivity(intent)
        }
    }
}