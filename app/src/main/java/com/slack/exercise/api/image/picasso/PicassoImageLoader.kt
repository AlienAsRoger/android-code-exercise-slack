package com.slack.exercise.api.image.picasso

import android.widget.ImageView
import com.slack.exercise.R
import com.slack.exercise.api.image.ImageLoader
import com.squareup.picasso.Picasso
import javax.inject.Inject

/**
 * Implementation of [ImageLoader] via Picasso image loading library
 */
class PicassoImageLoader @Inject constructor() : ImageLoader {

    override fun loadImage(imageView: ImageView, imageUrl: String?) {
        Picasso.get().load(imageUrl)
            .placeholder(R.color.image_background)
            .error(R.color.image_background)
            .into(imageView)
    }

    override fun loadRoundedImage(imageView: ImageView, imageUrl: String?, cornerRadius: Float) {
        Picasso.get().load(imageUrl)
            .placeholder(R.color.image_background)
            .error(R.color.image_background)
            .transform(RoundedCornersTransform(cornerRadius))
            .into(imageView)
    }
}