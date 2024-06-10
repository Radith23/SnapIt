package sns.example.snapit.ui.story.detail

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import dagger.hilt.android.AndroidEntryPoint
import sns.example.snapit.R
import sns.example.snapit.data.local.entity.StoryEntity
import sns.example.snapit.databinding.ActivityDetailBinding
import sns.example.snapit.utils.ConstVal
import sns.example.snapit.utils.ext.setImageUrl

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var story: StoryEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initIntent()
        initUI()
    }

    private fun initUI() {
        binding.apply {
            imgStoryThumbnail.setImageUrl(story.photoUrl, true)
            tvStoryTitle.text = story.name
            tvStoryDesc.text = story.description
        }
        title = getString(R.string.title_detail_story)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    @Suppress("DEPRECATION")
    private fun initIntent() {
        story = intent.getParcelableExtra(ConstVal.BUNDLE_KEY_STORY)!!
    }

    @Suppress("DEPRECATION")
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onNavigateUp()
    }
}