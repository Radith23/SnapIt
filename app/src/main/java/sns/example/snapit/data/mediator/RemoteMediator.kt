package sns.example.snapit.data.mediator

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import sns.example.snapit.data.local.database.StoryDatabase
import sns.example.snapit.data.local.entity.RemoteKeys
import sns.example.snapit.data.local.entity.StoryEntity
import sns.example.snapit.data.mapper.storyToStoryEntity
import sns.example.snapit.data.mapper.storyToWidgetContent
import sns.example.snapit.data.remote.story.StoryService
import sns.example.snapit.utils.ConstVal.INITIAL_PAGE_INDEX

@OptIn(ExperimentalPagingApi::class)
class RemoteMediator(
    private val database: StoryDatabase,
    private val service: StoryService,
    private val token: String
) : RemoteMediator<Int, StoryEntity>() {

    override suspend fun initialize(): InitializeAction {
        return InitializeAction.LAUNCH_INITIAL_REFRESH
    }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, StoryEntity>
    ): MediatorResult {

        val page = when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getRemoteKeyClosestToCurrentPosition(state)
                remoteKeys?.nextKey?.minus(1) ?: INITIAL_PAGE_INDEX
            }

            LoadType.PREPEND -> {
                val remoteKeys = getRemoteKeyForFirstItem(state)
                remoteKeys?.prevKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItem(state)
                remoteKeys?.nextKey ?: return MediatorResult.Success(
                    endOfPaginationReached = remoteKeys != null
                )
            }
        }

        try {
            database.getWidgetContentDao().deleteAllWidgets()

            val responseData = service.getStories(token, page, state.config.pageSize)
            val storyList = responseData.listStory.map {
                storyToWidgetContent(it)
            }
            database.getWidgetContentDao().insertNewWidgets(storyList)

            val endOfPagination = responseData.listStory.isEmpty()

            database.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    database.getRemoteKeysDao().deleteRemoteKeys()
                    database.getStoryDao().deleteAllStories()
                }
                val prevKey = if (page == 1) null else page - 1
                val nextKey = if (endOfPagination) null else page + 1
                val keys = responseData.listStory.map {
                    RemoteKeys(id = it.id, prevKey = prevKey, nextKey = nextKey)
                }

                database.getRemoteKeysDao().insertAll(keys)

                responseData.listStory.forEach {
                    val storyEntity = storyToStoryEntity(it)

                    database.getStoryDao().insertStories(storyEntity)
                }
            }
            return MediatorResult.Success(endOfPagination)
        } catch (ex: Exception) {
            return MediatorResult.Error(ex)
        }
    }

    private suspend fun getRemoteKeyForLastItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.lastOrNull { it.data.isNotEmpty() }?.data?.lastOrNull()?.let { data ->
            database.getRemoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyForFirstItem(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.pages.firstOrNull { it.data.isNotEmpty() }?.data?.firstOrNull()?.let { data ->
            database.getRemoteKeysDao().getRemoteKeysId(data.id)
        }
    }

    private suspend fun getRemoteKeyClosestToCurrentPosition(state: PagingState<Int, StoryEntity>): RemoteKeys? {
        return state.anchorPosition?.let { position ->
            state.closestItemToPosition(position)?.id?.let { id ->
                database.getRemoteKeysDao().getRemoteKeysId(id)
            }
        }
    }
}