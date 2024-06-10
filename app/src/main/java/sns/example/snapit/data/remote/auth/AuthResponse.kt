package sns.example.snapit.data.remote.auth

import com.google.gson.annotations.SerializedName
import sns.example.snapit.data.model.User

data class AuthResponse(

    @SerializedName("loginResult")
    val loginResult: User,

    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String
)