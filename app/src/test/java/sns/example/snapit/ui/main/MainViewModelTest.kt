package sns.example.snapit.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.ExperimentalPagingApi
import androidx.recyclerview.widget.ListUpdateCallback
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import sns.example.snapit.data.remote.api.ApiResponse
import sns.example.snapit.data.remote.story.AddStoryResponse
import sns.example.snapit.data.repository.StoryRepository
import sns.example.snapit.ui.DataDummy
import sns.example.snapit.ui.DataDummy.dataFileUploadResponseDummy
import sns.example.snapit.ui.MainDispatcherRule
import sns.example.snapit.ui.getOrAwaitValue
import sns.example.snapit.ui.maps.MapsViewModel

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mainViewModel: MainViewModel
    private lateinit var mapsViewModel: MapsViewModel

    @Mock
    private lateinit var storyRepository: StoryRepository

    private var dataDummyToken = "testing_token"
    private val dummyFile = DataDummy.multipartFileDummy()
    private val dummyDescription = DataDummy.dataRequestBodyDummy()

    @Before
    fun setUp() {
        storyRepository = mock(storyRepository::class.java)
        mapsViewModel = MapsViewModel(mock())
    }

    @get:Rule
    var mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `Upload new story successfully`() = runTest {
        val dummyUploadResponse = dataFileUploadResponseDummy()

        val dataUploadResponse = MutableLiveData<ApiResponse<AddStoryResponse>>()
        dataUploadResponse.value = ApiResponse.Success(dummyUploadResponse)

        Mockito.`when`(
            mainViewModel.addNewStory(
                dataDummyToken,
                dummyFile,
                dummyDescription,
                null,
                null
            )
        ).thenReturn(dataUploadResponse)

        val actualUploadResponse =
            mainViewModel.addNewStory(dataDummyToken, dummyFile, dummyDescription, null, null)
                .getOrAwaitValue()
        Mockito.verify(mainViewModel)
            .addNewStory(dataDummyToken, dummyFile, dummyDescription, null, null)

        advanceUntilIdle()

        Assert.assertNotNull(actualUploadResponse)
        Assert.assertTrue(actualUploadResponse is ApiResponse.Success)
    }

    @Test
    fun `Upload new story failed`() = runTest {
        val dataUploadResponse = MutableLiveData<ApiResponse<AddStoryResponse>>()
        dataUploadResponse.value = ApiResponse.Error("add story failed")

        Mockito.`when`(
            mainViewModel.addNewStory(
                dataDummyToken,
                dummyFile,
                dummyDescription,
                null,
                null
            )
        ).thenReturn(dataUploadResponse)

        val actualUploadResponse =
            mainViewModel.addNewStory(dataDummyToken, dummyFile, dummyDescription, null, null)
                .getOrAwaitValue()
        Mockito.verify(mainViewModel)
            .addNewStory(dataDummyToken, dummyFile, dummyDescription, null, null)

        advanceUntilIdle()

        Assert.assertNotNull(actualUploadResponse)
        Assert.assertTrue(actualUploadResponse is ApiResponse.Error)
    }

    private val listUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}