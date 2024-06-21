package com.example.mymusicservise.presenter.activity

import android.Manifest
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.example.mymusicservise.R
import com.example.mymusicservise.data.sourse.MyShar
import com.example.mymusicservise.presenter.screen.main.MainScreen
import com.example.mymusicservise.presenter.service.MyService
import com.example.uzummarketclient.utils.isHavePermission
import com.example.uzummarketclient.utils.requestPermission
import com.example.uzummarketclient.utils.statusBarTRANSPARENT
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        statusBarTRANSPARENT()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (isHavePermission(Manifest.permission.READ_MEDIA_AUDIO)) {
                requestPermission(arrayOf(Manifest.permission.READ_MEDIA_AUDIO))
            }
        } else {
            if (isHavePermission(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                requestPermission(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            }
        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    123
                )
            }
        } else {

        }
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.FOREGROUND_SERVICE)
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.FOREGROUND_SERVICE),
                    123
                )
            }
        } else {

        }

        val myShar=MyShar.getInstance()
        // Проверяем, содержит ли intent специальные инструкции для открытия Fragment
        if (intent?.extras?.getBoolean("OPEN_FRAGMENT", false) == true) {
            // Запускаем ваш Fragment
            myShar.setOpenMusic(true)
        }else{
            myShar.setOpenMusic(false)
        }
    }

    private val REQ_CODE_SPEECH_INPUT = 100
    var shareData: ((String) -> Unit)? = null

    fun promptSpeechInput(lang: String) {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ru-Ru")//lang

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Please Speak")

        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT)
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(
                this, "Sorry! Your device doesn\\'t support speech input",
                Toast.LENGTH_SHORT
            ).show()
        }

    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQ_CODE_SPEECH_INPUT) {
            if (resultCode == RESULT_OK && data != null) {
                val message = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)

                shareData?.invoke(message.toString())
            }
        }
    }
}