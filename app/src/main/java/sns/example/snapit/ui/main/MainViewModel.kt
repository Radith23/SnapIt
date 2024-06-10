package sns.example.snapit.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import sns.example.snapit.data.local.entity.StoryEntity
import sns.example.snapit.data.remote.api.ApiResponse
import sns.example.snapit.data.remote.story.AddStoryResponse
import sns.example.snapit.data.repository.StoryRepository
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class MainViewModel @Inject constructor(private val storyRepository: StoryRepository) :
    ViewModel() {

    fun getAllStories(token: String): LiveData<PagingData<StoryEntity>> =
        storyRepository.getAllStories(token).cachedIn(viewModelScope).asLiveData()

    fun addNewStory(
        token: String,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody?,
        lon: RequestBody?
    ): LiveData<ApiResponse<AddStoryResponse>> {
        val result = MutableLiveData<ApiResponse<AddStoryResponse>>()
        viewModelScope.launch {
            storyRepository.addNewStory(token, file, description, lat, lon).collect {
                result.postValue(it)
            }
        }
        return result
    }
}