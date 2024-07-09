package com.my.schedule.ui.api.internal.data.model

import androidx.annotation.Keep
import com.my.schedule.ui.api.model.Post as ExternalPost


@Keep
internal data class Post internal constructor(
    val userId: Int,
    val id: Int,
    val title: String,
    val body: String
)

internal fun Post.toExternalModel(): ExternalPost {
    return ExternalPost(
        userId = this.userId,
        id = this.id,
        title = this.title,
        body = this.body
    )
}
