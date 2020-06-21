package com.example.usergallerydemo

import android.net.Uri

data class Media(
    val id: String,
    val uri: Uri,
    val width: Int,
    val height: Int
)