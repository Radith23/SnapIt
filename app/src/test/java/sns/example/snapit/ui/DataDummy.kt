package sns.example.snapit.ui

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import sns.example.snapit.data.local.entity.StoryEntity
import sns.example.snapit.data.model.Story
import sns.example.snapit.data.remote.story.AddStoryResponse
import sns.example.snapit.data.remote.story.GetStoryResponse

object DataDummy {

    fun listStoryDummy(): List<StoryEntity> {
        val items = arrayListOf<StoryEntity>()
        for (i in 0 until 10) {
            val story = StoryEntity(
                id = "story-FvU4u0Vp2S3PMsFg",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-08T06:34:18.598Z",
                name = "Testing user",
                description = "Lorem Ipsum",
                lon = -16.002,
                lat = -10.212
            )
            items.add(story)
        }
        return items
    }

    fun getStoriesResponseDummy(): GetStoryResponse {
        val error = false
        val message = "Stories fetched successfully"
        val listStory = mutableListOf<Story>()

        for (i in 0 until 10) {
            val story = Story(
                id = "story-FvU4u0Vp2S3PMsFg",
                photoUrl = "https://story-api.dicoding.dev/images/stories/photos-1641623658595_dummy-pic.png",
                createdAt = "2022-01-08T06:34:18.598Z",
                name = "Testing user",
                description = "Lorem Ipsum",
                lon = -16.002,
                lat = -10.212
            )
            listStory.add(story)
        }
        return GetStoryResponse(listStory, error, message)
    }

    fun multipartFileDummy(): MultipartBody.Part {
        val dummyText = "dummy"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun dataRequestBodyDummy(): RequestBody {
        val dummyText = "dummy"
        return dummyText.toRequestBody()
    }

    fun dataFileUploadResponseDummy(): AddStoryResponse {
        return AddStoryResponse(
            error = false,
            message = "success"
        )
    }
}