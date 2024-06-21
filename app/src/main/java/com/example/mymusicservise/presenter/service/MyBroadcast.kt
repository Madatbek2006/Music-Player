package com.example.mymusicservise.presenter.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.view.KeyEvent
import com.example.mymusicservise.data.model.ServiceEnum
import com.example.uzummarketclient.utils.myLog

class MyBroadcast:BroadcastReceiver() {
    var onClickButton:((ServiceEnum)->Unit)?=null
    override fun onReceive(context: Context?, intent: Intent?) {
        if ( Intent.ACTION_TIME_TICK==intent!!.action){
            "salomat".myLog()
        }
        if (Intent.ACTION_MEDIA_BUTTON == intent!!.action) {
            val keyEvent = intent.getParcelableExtra<KeyEvent>(Intent.EXTRA_KEY_EVENT)
            if (keyEvent != null && keyEvent.action == KeyEvent.ACTION_DOWN) {
                when (keyEvent.keyCode) {
                    KeyEvent.KEYCODE_MEDIA_PLAY -> {
                        "salom play".myLog()
                        // Play tugmasi bosilganda amalga oshiriladigan kod
                        onClickButton?.invoke(ServiceEnum.MERGE)
                    }
                    KeyEvent.KEYCODE_MEDIA_PAUSE -> {
                        "salom pause".myLog()
                        // Pause tugmasi bosilganda amalga oshiriladigan kod
                        onClickButton?.invoke(ServiceEnum.MERGE)
                    }
                    KeyEvent.KEYCODE_MEDIA_NEXT -> {
                        // Next tugmasi bosilganda amalga oshiriladigan kod
                        onClickButton?.invoke(ServiceEnum.NEXT)
                        "salom next".myLog()
                    }
                    KeyEvent.KEYCODE_MEDIA_PREVIOUS -> {
                        "salom prev".myLog()
                        // Previous tugmasi bosilganda amalga oshiriladigan kod
                        onClickButton?.invoke(ServiceEnum.PREV)
                    }
                }
            }
        }
    }
}