package com.segunfrancis.expensetracker

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import kotlinx.serialization.Serializable
import java.io.ByteArrayOutputStream

sealed class ExpenseTrackerRoute {

    @Serializable
    data class AddExpenseRoute(val id: Long? = null) : ExpenseTrackerRoute()

    @Serializable
    data class ViewExpenseRoute(val id: Long) : ExpenseTrackerRoute()

    @Serializable
    data object ExpensesRoute : ExpenseTrackerRoute()
}

fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
    return stream.toByteArray()
}

fun byteArrayToBitmap(byteArray: ByteArray): Bitmap {
    return BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
}

fun uriToBitmap(context: Context, uri: Uri): Bitmap? {
    return try {
        context.contentResolver.openInputStream(uri).use {
            BitmapFactory.decodeStream(it)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun getByteArraySizeInKB(byteArray: ByteArray): Double {
    val sizeInBytes = byteArray.size
    return sizeInBytes / 1024.0
}
