package com.example.mymusicservise.utils

import android.database.Cursor
import androidx.lifecycle.MutableLiveData
import com.example.mymusicservise.data.model.MusicData

object MyMusicModel {
    var isFavorite= false
    var favoriteList:ArrayList<MusicData>?=null
    var allList:ArrayList<MusicData>?=null
    var cursor:Cursor?=null
    var pos1:Int=-1
    var pos2:Int=-1
    var id=-1
    var currentTime:Long=0L
    var fullTime:Long=0L
    val currentTimeLiveData = MutableLiveData<Long>()
    val playMusicLiveData = MutableLiveData<MusicData>()
    val isPlayingLiveData = MutableLiveData<Boolean>()
    val newMusic = MutableLiveData<Unit>()

    val BG_COLOR=-16754680
}