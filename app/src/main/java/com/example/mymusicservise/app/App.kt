package com.example.mymusicservise.app

import android.app.Application
import com.example.mymusicservise.data.sourse.MyShar

class App:Application() {

    override fun onCreate() {
        super.onCreate()
        MyShar.init(this)
    }
}