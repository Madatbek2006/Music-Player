package com.example.mymusicservise.presenter.screen.splash

import android.os.Bundle
import android.view.View
import android.window.SplashScreen
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.mymusicservise.R
import com.example.mymusicservise.data.model.MusicData
import com.example.mymusicservise.utils.MyMusicModel
import com.example.uzummarketclient.utils.getMusicDataByPosition
import com.example.uzummarketclient.utils.getMusicsCursor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SplashScreen:Fragment(R.layout.screen_splash) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

      requireActivity().getMusicsCursor().onEach {
          MyMusicModel.cursor=it
          val data=ArrayList<MusicData>()
          for (i in 0 until it.count){
              val music=it.getMusicDataByPosition(i,requireContext())
              data.add(music)
          }
          MyMusicModel.allList=data
          delay(500)
          findNavController().navigate(R.id.action_splashScreen_to_mainScreen)
      }.launchIn(lifecycleScope)
    }
}