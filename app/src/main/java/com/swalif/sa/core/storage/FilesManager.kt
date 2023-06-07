package com.swalif.sa.core.storage

import android.content.Context
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.core.graphics.decodeBitmap
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import logcat.logcat
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import javax.inject.Inject

class FilesManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val contentResolver = context.contentResolver
    private val quality: Int = 80

    companion object{
        private const val ONE_MB :Long=  1024*1024
        private const val  FIFTY_MB :Long= ONE_MB * 50

    }
    @Inject lateinit var storage :FirebaseStorage
    private val formatImage: CompressFormat = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        CompressFormat.WEBP_LOSSY
    } else {
        CompressFormat.JPEG
    }


     fun ifFileAvailable(uri:String):Boolean{
        val file = File(context.filesDir,uri)
        return file.exists()
    }
    private suspend fun compressImage(uri: Uri):ByteArray{
        return withContext(Dispatchers.Default){
            val outputStream = ByteArrayOutputStream()
            val btm = ImageDecoder.createSource(contentResolver,uri).decodeBitmap{info,source->

            }
            btm.compress(formatImage,80,outputStream)
            outputStream.toByteArray()
        }
    }

     suspend fun saveToFireStorage(pathUidUser: String, mediaUri: Uri): String? {
        return try {
            val compressImage = compressImage(mediaUri)
            // TODO: check if image is uploaded if yes get current uri if not upload new
            val reference = storage.reference.child(pathUidUser)
                .child(mediaUri.lastPathSegment!!.plus(".jpeg")).putBytes(compressImage).await()
            reference.storage.downloadUrl.await().toString()
        } catch (f: Exception) {
            logcat("FirestoreChatMessageRepository") {
                "save file faield : ${f.message}"
            }
            null
        }

    }

    suspend fun saveImageLocally(uriReference:String, nameFile:String):Boolean{
        return try {
            withContext(Dispatchers.Default){
                val byteArray = storage.getReferenceFromUrl(uriReference).getBytes(FIFTY_MB).await()
                val bitmap = BitmapFactory.decodeByteArray(byteArray,0,byteArray.size)
                context.openFileOutput(nameFile,Context.MODE_PRIVATE).use {
                    if (!bitmap.compress(formatImage, quality, it)) {
                        throw IOException("Couldn't save bitmap")
                    }
                }
                true
            }
        }catch (e:Exception){
            logcat {
                e.message.toString()
            }
            false
        }
    }
    // bf9c06d3-10b9-47b3-89bd-fb318d186687.jpeg
    suspend fun saveImage(uri: Uri, nameFile: String): Boolean {
        return try {
            withContext(Dispatchers.Default) {
                val imageDecoder =
                    ImageDecoder.createSource(contentResolver, uri).decodeBitmap { info, source ->
                    }
                context.openFileOutput(nameFile, Context.MODE_PRIVATE)
                    .use {
                        if (!imageDecoder.compress(formatImage, quality, it)) {
                            throw IOException("Couldn't save bitmap")
                        }
                    }
                true
            }

        } catch (e: Exception) {
            logcat { e.message!! }
            false
        }

    }
}

data class MediaStorage(
    val nameFile: String,
    val contentUri: Uri,
//    val typeMedia:MediaType
)

enum class MediaType(ext: String) {
    IMAGE("png"), VIDEO("---"), AUDIO("---")
}