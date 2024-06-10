package sns.example.snapit.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadState
import androidx.paging.PagingData
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import sns.example.snapit.R
import sns.example.snapit.data.local.entity.StoryEntity
import sns.example.snapit.databinding.ActivityMainBinding
import sns.example.snapit.ui.loading.LoadingStateAdapter
import sns.example.snapit.ui.maps.MapsActivity
import sns.example.snapit.ui.profile.ProfileActivity
import sns.example.snapit.ui.story.add.AddActivity
import sns.example.snapit.utils.SessionManager
import sns.example.snapit.utils.ext.gone
import sns.example.snapit.utils.ext.hide
import sns.example.snapit.utils.ext.isTrue
import sns.example.snapit.utils.ext.show
import sns.example.snapit.utils.ext.toBearerToken
import timber.log.Timber

@ExperimentalPagingApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val mainViewModel: MainViewModel by viewModels()

    private lateinit var binding: ActivityMainBinding

    private lateinit var pref: SessionManager
    private var token: String? = null

    private lateinit var adapter: MainAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        pref = SessionManager(this)
        token = pref.getToken

        initAction()
        initUI()
        initAdapter()

        getAllStories(token!!.toBearerToken())
    }

    private fun initUI() {
        binding.rvStories.layoutManager = LinearLayoutManager(this)
        binding.rvStories.layoutManager?.onRestoreInstanceState(binding.rvStories.layoutManager?.onSaveInstanceState())
        binding.tvGreetingName.text = getString(R.string.label_greeting_user, pref.getUserName)
    }

    private fun initAction() {
        binding.fabAddStory.setOnClickListener {
            AddActivity.start(this)
        }
        binding.btnAccount.setOnClickListener {
            ProfileActivity.start(this)
        }
        binding.btnSetting.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
        binding.btnMaps.setOnClickListener {
            MapsActivity.start(this)
        }
    }

    private fun getAllStories(token: String) {
        mainViewModel.getAllStories(token).observe(this) { stories ->
            initRecyclerViewUpdate(stories)
        }
    }

    private fun initRecyclerViewUpdate(storiesData: PagingData<StoryEntity>) {
        val recyclerViewState = binding.rvStories.layoutManager?.onSaveInstanceState()

        adapter.submitData(lifecycle, storiesData)

        binding.rvStories.layoutManager?.onRestoreInstanceState(recyclerViewState)
    }

    private fun initAdapter() {
        adapter = MainAdapter()
        binding.rvStories.layoutManager?.onRestoreInstanceState(binding.rvStories.layoutManager?.onSaveInstanceState())
        binding.rvStories.adapter = adapter
        lifecycleScope.launch {
            adapter.loadStateFlow.distinctUntilChanged { old, new ->
                old.mediator?.prepend?.endOfPaginationReached.isTrue() == new.mediator?.prepend?.endOfPaginationReached.isTrue()
            }
                .filter { it.refresh is LoadState.NotLoading && it.prepend.endOfPaginationReached && !it.append.endOfPaginationReached }
                .collect {
                    binding.rvStories.scrollToPosition(0)
                }
        }
        adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )
        adapter.addLoadStateListener { loadState ->
            when (loadState.refresh) {
                is LoadState.Loading -> {
                    isError(false)
                    isLoading(true)
                }

                is LoadState.NotLoading -> {
                    isError(false)
                    isLoading(false)
                }

                is LoadState.Error -> isError(true)
                else -> {
                    Timber.e(getString(R.string.message_unknown_state))
                }
            }
        }
    }

    private fun isLoading(loading: Boolean) {
        if (loading) {
            binding.apply {
                rvStories.hide()
                shimmerLoading.show()
                shimmerLoading.startShimmer()
            }
        } else {
            binding.apply {
                rvStories.show()
                shimmerLoading.stopShimmer()
                shimmerLoading.gone()
            }
        }
    }

    private fun isError(error: Boolean) {
        if (error) {
            binding.tvStoriesError.show()
            binding.rvStories.hide()
        } else {
            binding.tvStoriesError.hide()
            binding.rvStories.show()
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.refresh()
    }

    @Deprecated("Deprecated in Java")
    @Suppress("DEPRECATION")
    override fun onBackPressed() {
        finishAffinity()
        super.onBackPressed()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, MainActivity::class.java)
            context.startActivity(intent)
        }
    }
}