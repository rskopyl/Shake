package com.rskopyl.shake.util

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import androidx.compose.material3.LocalContentColor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.realm.kotlin.types.RealmInstant
import kotlinx.datetime.*
import org.mongodb.kbson.ObjectId
import java.io.File
import java.io.FileOutputStream

@Composable
fun ProvideContentColor(value: Color, content: @Composable () -> Unit) {
    CompositionLocalProvider(
        LocalContentColor provides value,
        content = content
    )
}

@Composable
inline operator fun <T : ViewModel> ViewModelProvider.Factory.Companion.invoke(
    crossinline factory: (Context) -> T
): ViewModelProvider.Factory {
    val context = LocalContext.current
    return object : ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>) =
            factory(context) as T
    }
}

fun String.toObjectIdOrNull(): ObjectId? {
    return try {
        ObjectId(this)
    } catch (e: IllegalArgumentException) {
        null
    }
}

fun RealmInstant.toLocalDateTime(
    timeZone: TimeZone = TimeZone.UTC
): LocalDateTime {
    return Instant
        .fromEpochSeconds(epochSeconds)
        .toLocalDateTime(timeZone)
}

fun LocalDateTime.toRealmInstant(
    timeZone: TimeZone = TimeZone.UTC
): RealmInstant {
    return RealmInstant.from(
        epochSeconds = toInstant(timeZone).epochSeconds,
        nanosecondAdjustment = nanosecond
    )
}

fun Context.resolveImage(uri: Uri): Bitmap {
    val stream = contentResolver.openInputStream(uri)
    val image = BitmapFactory.decodeStream(stream)
    Environment.getExternalStorageDirectory()
    return image.also { stream?.close() }
}

fun Context.storeImage(bitmap: Bitmap, folder: String): Uri? {
    val contentValues = ContentValues().apply {
        put(MediaStore.Images.Media.MIME_TYPE, "image/png")
    }
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        contentValues.run {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/$folder")
            put(MediaStore.Images.Media.IS_PENDING, true)
        }
        val url = contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues
        )
        if (url != null) {
            val stream = contentResolver.openOutputStream(url)
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
            stream?.close()
            contentResolver.update(
                url,
                contentValues.apply {
                    put(MediaStore.Images.Media.IS_PENDING, false)
                },
                null, null
            )
        }
        return url
    } else {
        val directory = File(
            "${Environment.getExternalStorageDirectory()}/$folder"
        ).apply {
            if (!exists()) mkdir()
        }
        val name = "${Clock.System.now().toEpochMilliseconds()}.png"
        val file = File(directory, name)
        val stream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        stream.close()
        return contentResolver.insert(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            contentValues.apply {
                put(MediaStore.Images.Media.DATA, file.absolutePath)
            }
        )
    }
}