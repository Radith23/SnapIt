package sns.example.snapit.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import sns.example.snapit.data.local.dao.RemoteKeysDao
import sns.example.snapit.data.local.dao.StoryDao
import sns.example.snapit.data.local.dao.WidgetContentDao
import sns.example.snapit.data.local.entity.RemoteKeys
import sns.example.snapit.data.local.entity.StoryEntity
import sns.example.snapit.data.local.entity.WidgetContent

@Database(
    entities = [StoryEntity::class, RemoteKeys::class, WidgetContent::class],
    version = 2,
    exportSchema = false
)
abstract class StoryDatabase : RoomDatabase() {
    abstract fun getStoryDao(): StoryDao
    abstract fun getRemoteKeysDao(): RemoteKeysDao
    abstract fun getWidgetContentDao(): WidgetContentDao
}