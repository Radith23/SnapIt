package sns.example.snapit.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "tbl_story")
data class StoryEntity(
    @PrimaryKey
    val id: String,

    val name: String,

    val description: String,

    @ColumnInfo(name = "created_at")
    val createdAt: String,

    @ColumnInfo(name = "photo_url")
    val photoUrl: String,

    val lon: Double?,

    val lat: Double?
): Parcelable