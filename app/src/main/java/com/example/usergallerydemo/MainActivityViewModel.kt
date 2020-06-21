package com.example.usergallerydemo

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class MainActivityViewModel(
    application: Application
) : AndroidViewModel(application),
    CoroutineScope {

    private val job = Job()
    override val coroutineContext: CoroutineContext
        get() = job + Dispatchers.IO

    private val _images: MutableLiveData<List<Media>> = MutableLiveData()
    val images: LiveData<List<Media>>
        get() = _images

    /**
     * Getting All Images Path.
     *
     * Required Storage Permission
     *
     * @return ArrayList with images Path
     */
    private fun loadImagesFromSDCard(): ArrayList<Media> {
        val uri: Uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val imageList = ArrayList<Media>()
        val projection =
            arrayOf(
                MediaStore.MediaColumns._ID,
                MediaStore.MediaColumns.WIDTH,
                MediaStore.MediaColumns.HEIGHT
            )
        getApplication<Application>().contentResolver.query(
            uri,
            projection,
            null,
            null,
            null
        )
            .use {
                it ?: return@use
                while (it.moveToNext()) {
                    val id = it.getString(0)
                    imageList.add(
                        Media(
                            id,
                            ContentUris.withAppendedId(uri, id.toLong()),
                            it.getInt(1),
                            it.getInt(2)
                        )
                    )
                }
            }
        return imageList
    }

    fun getAllImages() {
        launch {
            _images.postValue(loadImagesFromSDCard())
        }
    }
}

