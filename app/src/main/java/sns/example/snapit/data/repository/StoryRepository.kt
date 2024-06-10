package sns.example.snapit.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import okhttp3.MultipartBody
import okhttp3.RequestBody
import sns.example.snapit.data.local.entity.StoryEntity
import sns.example.snapit.data.model.Story
import sns.example.snapit.data.remote.api.ApiResponse
import sns.example.snapit.data.remote.story.AddStoryResponse
import sns.example.snapit.data.remote.story.GetStoryResponse
import sns.example.snapit.data.source.StoryDataSource
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
class StoryRepository @Inject constructor(
    private val storyDataSource: StoryDataSource
) {

    fun getAllStories(token: String): Flow<PagingData<StoryEntity>> =
        storyDataSource.getAllStories(token).flowOn(Dispatchers.IO)

    suspend fun getStoriesWithLocation(
        token: String,
        location: Int
    ): Flow<ApiResponse<GetStoryResponse>> =
        storyDataSource.getStoriesWithLocation(token, location).flowOn(Dispatchers.IO)

    suspend fun addNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): Flow<ApiResponse<AddStoryResponse>> {
        return storyDataSource.addNewStory(token, file, description, lat, lon)
    }
}