package com.slack.exercise.api.image

import android.widget.ImageView

/**
 * Generic interface for image loading mechanism
 */
interface ImageLoader {

    fun loadImage(imageView: ImageView, imageUrl: String?)

    fun loadRoundedImage(imageView: ImageView, imageUrl: String?, cornerRadius: Float)
}
