package sns.example.snapit.data.local.dao

import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sns.example.snapit.data.local.entity.StoryEntity

@Dao
interface StoryDao {

    @Query("SELECT * FROM tbl_story")
    fun getAllStories(): PagingSource<Int, StoryEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStories(vararg : StoryEntity)

    @Query("DELETE FROM tbl_story")
    fun deleteAllStories()
}