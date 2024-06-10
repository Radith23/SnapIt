package sns.example.snapit.data.remote.story

import com.google.gson.annotations.SerializedName
import sns.example.snapit.data.model.Story

data class GetStoryResponse(

    @SerializedName("listStory")
    val listStory: List<Story>,

    @SerializedName("error")
    val error: Boolean,

    @SerializedName("message")
    val message: String
)