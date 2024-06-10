package sns.example.snapit.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tbl_widget_content")
data class WidgetContent(
    @ColumnInfo(name = "id")
    @PrimaryKey(autoGenerate = false)
    val id: String,
    @ColumnInfo(name = "photoUrl")
    val photoUrl: String
)