package sns.example.snapit.ui.login

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import sns.example.snapit.data.remote.api.ApiResponse
import sns.example.snapit.data.remote.auth.AuthResponse
import sns.example.snapit.data.remote.auth.LoginBody
import sns.example.snapit.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val authRepository: AuthRepository) : ViewModel() {

    suspend fun userLogin(loginBody: LoginBody): Flow<ApiResponse<AuthResponse>> {
        return authRepository.loginUser(loginBody)
    }
}