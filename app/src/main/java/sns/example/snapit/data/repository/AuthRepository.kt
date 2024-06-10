package sns.example.snapit.data.repository

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import sns.example.snapit.data.remote.api.ApiResponse
import sns.example.snapit.data.remote.auth.AuthBody
import sns.example.snapit.data.remote.auth.AuthResponse
import sns.example.snapit.data.remote.auth.LoginBody
import sns.example.snapit.data.remote.auth.RegisterResponse
import sns.example.snapit.data.source.AuthDataSource
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AuthRepository @Inject constructor(private val authDataSource: AuthDataSource) {

    suspend fun registerUser(authBody: AuthBody): Flow<ApiResponse<RegisterResponse>> {
        return authDataSource.registerUser(authBody).flowOn(Dispatchers.IO)
    }

    suspend fun loginUser(loginBody: LoginBody): Flow<ApiResponse<AuthResponse>> {
        return authDataSource.loginUser(loginBody).flowOn(Dispatchers.IO)
    }
}