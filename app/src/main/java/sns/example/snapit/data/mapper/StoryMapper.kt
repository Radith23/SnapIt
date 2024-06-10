package sns.example.snapit.data.mapper

import sns.example.snapit.data.local.entity.StoryEntity
import sns.example.snapit.data.local.entity.WidgetContent
import sns.example.snapit.data.model.Story

fun storyToStoryEntity(story: Story): StoryEntity {
    return StoryEntity(
        id = story.id,
        name = story.name,
        description = story.description,
        createdAt = story.createdAt,
        photoUrl = story.photoUrl,
        lat = story.lat,
        lon = story.lon
    )
}

fun storyToWidgetContent(story: Story) : WidgetContent {
    return WidgetContent(
        id = story.id,
        photoUrl = story.photoUrl
    )
}