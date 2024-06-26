package sns.example.snapit.data.model

import com.google.gson.annotations.SerializedName

data class User(

    @SerializedName("userId")
    val userId: String,

    @SerializedName("name")
    val name: String,

    @SerializedName("token")
    val token: String

)