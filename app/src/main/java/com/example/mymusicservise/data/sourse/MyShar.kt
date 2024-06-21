package com.example.mymusicservise.data.sourse

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.example.uzummarketclient.utils.getPosMusic

class MyShar private constructor(){

    companion object{
        private lateinit var sharedPreferences:SharedPreferences
        private val instance=MyShar()
        fun init(context: Context){
            sharedPreferences=context.getSharedPreferences("music",Context.MODE_PRIVATE)
        }

        fun getInstance():MyShar{
            return instance
        }
    }

    fun setOpenMusic(boolean: Boolean){
        sharedPreferences.edit().putBoolean("bool",boolean).apply()
    }


    fun isOpenMusicScreen():Boolean{
        return sharedPreferences.getBoolean("bool",false)
    }

    fun isShuffle():Boolean{
        return sharedPreferences.getBoolean("shuffle",false)
    }
    fun setShuffle(bool:Boolean){
        sharedPreferences.edit().putBoolean("shuffle",bool).apply()
    }
    fun setIsFavorite(data:List<Int>){
        val size=data.size
        for (i in 0 until size){
            sharedPreferences.edit().putInt("favoriteId$i",data[i]).apply()
        }
        sharedPreferences.edit().putInt("favoriteSize",size).apply()
    }
    fun getIsFavorite():List<Int>{
        val size= sharedPreferences.getInt("favoriteSize",0)
        val data=ArrayList<Int>()
        for (i in 0 until size){
            data.add( sharedPreferences.getInt("favoriteId$i",0))
        }

        return data
    }
}