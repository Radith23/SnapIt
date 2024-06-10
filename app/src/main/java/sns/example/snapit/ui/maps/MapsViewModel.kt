package sns.example.snapit.ui.maps

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import sns.example.snapit.data.remote.api.ApiResponse
import sns.example.snapit.data.remote.story.GetStoryResponse
import sns.example.snapit.data.repository.StoryRepository
import javax.inject.Inject

@ExperimentalPagingApi
@HiltViewModel
class MapsViewModel @Inject constructor(private val storyRepository: StoryRepository) :
    ViewModel() {

    fun getStoriesWithLocation(
        token: String,
        location: Int
    ): LiveData<ApiResponse<GetStoryResponse>> {
        val result = MutableLiveData<ApiResponse<GetStoryResponse>>()
        viewModelScope.launch {
            storyRepository.getStoriesWithLocation(token, location).collect {
                result.postValue(it)
            }
        }
        return result
    }
}