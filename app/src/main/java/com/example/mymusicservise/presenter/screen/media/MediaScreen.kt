package com.example.mymusicservise.presenter.screen.media

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.misicplayer.presenter.adapter.HomeAdapter
import com.example.misicplayer.presenter.adapter.MediaAdapter
import com.example.mymusicservise.R
import com.example.mymusicservise.data.model.MusicData
import com.example.mymusicservise.data.model.ServiceEnum
import com.example.mymusicservise.data.sourse.MyShar
import com.example.mymusicservise.databinding.ScreenMediaBinding
import com.example.mymusicservise.utils.MyMusicModel
import com.example.uzummarketclient.utils.getMusicDataByPosition
import com.example.uzummarketclient.utils.getMusicsCursor
import com.example.uzummarketclient.utils.getPosMusic
import com.example.uzummarketclient.utils.myLog
import com.example.uzummarketclient.utils.setStatusBar
import com.example.uzummarketclient.utils.startMyService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.newCoroutineContext
import kotlinx.coroutines.withContext

class MediaScreen:Fragment(R.layout.screen_media) {
    private val binding by viewBinding(ScreenMediaBinding::bind)
    lateinit var adapter:MediaAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().setStatusBar(binding.spase)
        initAdapter()

    }
    private fun initAdapter(){
        adapter= MediaAdapter()
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_MEDIA_AUDIO), 123)
        }
        binding.recyclerView.adapter=adapter
        binding.recyclerView.layoutManager= LinearLayoutManager(requireContext())
        adapter.onClickItem={
            "onClick".myLog()
            MyMusicModel.pos2=it
            requireActivity().startMyService(ServiceEnum.PLAY)
            MyMusicModel.currentTime=0
            MyMusicModel.currentTimeLiveData.value=0
            MyMusicModel.isFavorite=true
        }
        MyMusicModel.playMusicLiveData.observe(viewLifecycleOwner){
            adapter.setPos(MyMusicModel.id)
        }
    }

    override fun onStart() {
        super.onStart()
        lifecycleScope.launch {
                val myShar=MyShar.getInstance()
                MyMusicModel.cursor?.let {
                    val data=ArrayList<MusicData>()
                    var favorites= myShar.getIsFavorite()
                    for (i in 0 until it.count){
                        val music=it.getMusicDataByPosition(i,requireContext())
                        if (favorites.contains(music.id)){
                            data.add(music)
                        }
                    }
                    isEmptyData(data.isEmpty())
                        adapter.submitList(data)
                    MyMusicModel.favoriteList=data
                }
        }
    }


    private fun isEmptyData(bool:Boolean)=binding.apply{
        if (bool){
            wave.visibility=View.VISIBLE
            txt.visibility=View.VISIBLE
        }else{
            wave.visibility=View.GONE
            txt.visibility=View.GONE
        }
    }
}
