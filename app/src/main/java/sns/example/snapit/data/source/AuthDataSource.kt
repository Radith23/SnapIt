package sns.example.snapit.data.source

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import sns.example.snapit.data.remote.api.ApiResponse
import sns.example.snapit.data.remote.auth.AuthBody
import sns.example.snapit.data.remote.auth.AuthResponse
import sns.example.snapit.data.remote.auth.AuthService
import sns.example.snapit.data.remote.auth.LoginBody
import sns.example.snapit.data.remote.auth.RegisterResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthDataSource @Inject constructor(private val authService: AuthService) {

    suspend fun registerUser(authBody: AuthBody): Flow<ApiResponse<RegisterResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = authService.registerUser(authBody)
                if (!response.error) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
            }
        }
    }

    suspend fun loginUser(loginBody: LoginBody): Flow<ApiResponse<AuthResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = authService.loginUser(loginBody)
                if (!response.error) {
                    emit(ApiResponse.Success(response))
                } else {
                    emit(ApiResponse.Error(response.message))
                }
            } catch (ex: Exception) {
                emit(ApiResponse.Error(ex.message.toString()))
            }
        }
    }
}