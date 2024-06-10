package sns.example.snapit.widget

import android.content.Context
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.room.Room
import sns.example.snapit.R
import sns.example.snapit.data.local.database.StoryDatabase
import sns.example.snapit.data.local.entity.WidgetContent
import sns.example.snapit.utils.ConstVal.DB_NAME
import sns.example.snapit.utils.urlToBitmap

internal class StackRemoteViewsFactory(private val context: Context) :
    RemoteViewsService.RemoteViewsFactory {

    private var content: MutableList<WidgetContent> = mutableListOf()

    override fun onCreate() {
    }

    override fun onDataSetChanged() {
        val database = Room.databaseBuilder(
            context.applicationContext, StoryDatabase::class.java,
            DB_NAME
        ).build()
        database.getWidgetContentDao().getAllWidgetContent().forEach {
            content.add(
                WidgetContent(
                    it.id,
                    it.photoUrl
                )
            )
        }
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = 5

    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(context.packageName, R.layout.item_widget)
        rv.setImageViewBitmap(R.id.imgStory, urlToBitmap(content[position].photoUrl))

        return rv
    }

    override fun getLoadingView(): RemoteViews? = null

    override fun getViewTypeCount(): Int = 1

    override fun getItemId(p0: Int): Long = 0

    override fun hasStableIds(): Boolean = false
}