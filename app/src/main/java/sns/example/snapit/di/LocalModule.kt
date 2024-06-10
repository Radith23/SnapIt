package sns.example.snapit.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import sns.example.snapit.data.local.dao.StoryDao
import sns.example.snapit.data.local.database.StoryDatabase
import sns.example.snapit.utils.ConstVal.DB_NAME
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext context: Context): StoryDatabase {
        return Room.databaseBuilder(context, StoryDatabase::class.java, DB_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideStoryDao(database: StoryDatabase): StoryDao = database.getStoryDao()

//    @Provides
//    fun provideRemoteKeyDao(database: StoryDatabase): RemoteKeysDao = database.getRemoteKeysDao()
}