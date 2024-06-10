package sns.example.snapit.data.source

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MultipartBody
import okhttp3.RequestBody
import sns.example.snapit.data.local.database.StoryDatabase
import sns.example.snapit.data.local.entity.StoryEntity
import sns.example.snapit.data.mediator.RemoteMediator
import sns.example.snapit.data.remote.api.ApiResponse
import sns.example.snapit.data.remote.story.AddStoryResponse
import sns.example.snapit.data.remote.story.GetStoryResponse
import sns.example.snapit.data.remote.story.StoryService
import sns.example.snapit.utils.ConstVal.DEFAULT_PAGE_SIZE
import javax.inject.Inject
import javax.inject.Singleton

@ExperimentalPagingApi
@Singleton
class StoryDataSource @Inject constructor(
    private val storyDatabase: StoryDatabase,
    private val storyService: StoryService
) {

    fun getAllStories(token: String): Flow<PagingData<StoryEntity>> {
        return Pager(
            config = PagingConfig(
                pageSize = DEFAULT_PAGE_SIZE
            ),
            remoteMediator = RemoteMediator(storyDatabase, storyService, token),
            pagingSourceFactory = { storyDatabase.getStoryDao().getAllStories() }
        ).flow
    }

    suspend fun getStoriesWithLocation(
        token: String,
        location: Int = 1
    ): Flow<ApiResponse<GetStoryResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = storyService.getStoriesWithLocation(token, location)
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

    suspend fun addNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody? = null,
        lon: RequestBody? = null
    ): Flow<ApiResponse<AddStoryResponse>> {
        return flow {
            try {
                emit(ApiResponse.Loading)
                val response = storyService.addNewStories(token, file, description, lat, lon)
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