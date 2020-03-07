package com.slack.exercise.dataprovider

import android.content.Context
import android.content.res.Resources
import com.slack.exercise.OpenClass
import com.slack.exercise.R
import timber.log.Timber
import java.io.InputStream

/**
 * This class is responsible for providing data from raw folders
 */
@OpenClass
class RawDataProvider(private val context: Context) {

    fun readFromBlacklist(): String? {
        return try {
            val res: Resources = context.resources
            val inputStream: InputStream = res.openRawResource(R.raw.blacklist)
            val bytes = ByteArray(inputStream.available())
            inputStream.read(bytes)
            String(bytes)
        } catch (e: Exception) {
            Timber.e(e)
            null
        }
    }
}