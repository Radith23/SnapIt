package sns.example.snapit.ui.register

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import sns.example.snapit.data.remote.api.ApiResponse
import sns.example.snapit.data.remote.auth.AuthBody
import sns.example.snapit.data.remote.auth.RegisterResponse
import sns.example.snapit.data.repository.AuthRepository
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {

    suspend fun userRegister(authBody: AuthBody): Flow<ApiResponse<RegisterResponse>> {
        return authRepository.registerUser(authBody)
    }
}