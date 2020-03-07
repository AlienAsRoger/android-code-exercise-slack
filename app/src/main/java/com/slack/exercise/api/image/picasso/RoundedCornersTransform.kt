package com.slack.exercise.api.image.picasso

import android.graphics.*
import com.squareup.picasso.Transformation

/**
 * Transformation class for [PicassoImageLoader] to support rounded corners
 */
class RoundedCornersTransform(private val cornerRadius: Float) : Transformation {

    override fun transform(source: Bitmap): Bitmap {
        val copy = Bitmap.createBitmap(source)
        if (copy != source) {
            source.recycle()
        }
        val bitmap = Bitmap.createBitmap(source.width, source.height, source.config)
        val canvas = Canvas(bitmap)
        val paint = Paint()
        val shader = BitmapShader(copy, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)

        paint.shader = shader
        paint.isAntiAlias = true
        val radius = cornerRadius
        canvas.drawRoundRect(RectF(0f, 0f, source.width.toFloat(), source.height.toFloat()), radius, radius, paint)
        copy.recycle()
        return bitmap
    }

    override fun key(): String {
        return "rounded_corners"
    }
}
