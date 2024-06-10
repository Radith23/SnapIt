package sns.example.snapit.ui.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import sns.example.snapit.data.local.entity.StoryEntity
import sns.example.snapit.databinding.ItemRowBinding
import sns.example.snapit.ui.story.detail.DetailActivity
import sns.example.snapit.utils.ConstVal
import sns.example.snapit.utils.ext.setImageUrl
import sns.example.snapit.utils.ext.timeStamptoString

class MainAdapter : PagingDataAdapter<StoryEntity, MainAdapter.MainViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainAdapter.MainViewHolder {
        val binding = ItemRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainAdapter.MainViewHolder, position: Int) {
        getItem(position)?.let { story ->
            holder.bind(holder.itemView.context, story)
        }
    }

    inner class MainViewHolder(private val binding: ItemRowBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(context: Context, story: StoryEntity) {
            binding.apply {
                tvStoryTitle.text = story.name
                tvStoryDesc.text = story.description
                tvStoryDate.text = story.createdAt.timeStamptoString()
                imgStoryThumbnail.setImageUrl(story.photoUrl, true)

                root.setOnClickListener {
                    val optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        root.context as Activity,
                        Pair(imgStoryThumbnail, "thumbnail"),
                        Pair(tvStoryTitle, "title"),
                        Pair(tvStoryDesc, "description"),
                    )
                    val intent = Intent(context, DetailActivity::class.java)
                    intent.putExtra(ConstVal.BUNDLE_KEY_STORY, story)
                    context.startActivity(intent, optionsCompat.toBundle())
                }
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryEntity>() {
            override fun areItemsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: StoryEntity, newItem: StoryEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}