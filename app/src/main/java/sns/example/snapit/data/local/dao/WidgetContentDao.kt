package sns.example.snapit.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import sns.example.snapit.data.local.entity.WidgetContent

@Dao
interface WidgetContentDao {

    @Query("SELECT * FROM tbl_widget_content")
    fun getAllWidgetContent(): List<WidgetContent>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertNewWidgets(storyList: List<WidgetContent>)

    @Query("DELETE FROM tbl_widget_content")
    suspend fun deleteAllWidgets()
}