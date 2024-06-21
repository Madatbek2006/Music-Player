package com.example.mymusicservise.data.model

import android.graphics.Bitmap

data class MusicData(
    val id : Int,
    val artist : String?,
    val title : String?,
    val data : String?,
    val duration : Long,
    val image:Bitmap?
)
