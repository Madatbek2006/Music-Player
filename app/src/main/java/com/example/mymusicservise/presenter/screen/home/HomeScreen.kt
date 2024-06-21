package com.example.mymusicservise.presenter.screen.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Observable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.misicplayer.presenter.adapter.HomeAdapter
import com.example.misicplayer.presenter.adapter.MediaAdapter
import com.example.mymusicservise.R
import com.example.mymusicservise.data.model.MusicData
import com.example.mymusicservise.data.model.ServiceEnum
import com.example.mymusicservise.databinding.ScreenHomeBinding
import com.example.mymusicservise.presenter.service.MyService
import com.example.mymusicservise.utils.MyMusicModel
import com.example.uzummarketclient.utils.getMusicsCursor
import com.example.uzummarketclient.utils.isHavePermission
import com.example.uzummarketclient.utils.myLog
import com.example.uzummarketclient.utils.requestPermission
import com.example.uzummarketclient.utils.setStatusBar
import com.example.uzummarketclient.utils.startMyService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeScreen:Fragment(R.layout.screen_home) {

    private var _binding:ScreenHomeBinding?=null
    private val binding get() = _binding!!
    private  val  adapter:MediaAdapter by lazy{MediaAdapter()}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding= ScreenHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initAdapter()
        "salom".myLog()
        requireActivity().setStatusBar(binding.spase)
        if (Build.VERSION.SDK_INT<=32){
            if (!requireActivity().isHavePermission(Manifest.permission.READ_EXTERNAL_STORAGE)){
                requireContext().requestPermission(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            }else{
                onStart()
            }
        }else{
            if (!requireActivity().isHavePermission(Manifest.permission.READ_MEDIA_AUDIO)){
                requireContext().requestPermission(arrayOf(Manifest.permission.READ_MEDIA_AUDIO))
            }else{
                onStart()
            }
        }
        adapter.submitList(MyMusicModel.allList)
    }


    private fun initAdapter(){

        binding.recyclerView.adapter=adapter
        binding.recyclerView.layoutManager= LinearLayoutManager(requireContext())
        adapter.onClickItem={
            "onClick".myLog()
            MyMusicModel.pos1=it
            requireActivity().startMyService(ServiceEnum.PLAY)
            MyMusicModel.currentTime=0
            MyMusicModel.currentTimeLiveData.value=0
            MyMusicModel.isFavorite=false
        }
        MyMusicModel.playMusicLiveData.observe(requireActivity()){
            adapter.id=MyMusicModel.id
        }
        MyMusicModel.playMusicLiveData.observe(requireActivity(),observer)

    }

    private val observer= Observer<MusicData> {
        if (_binding!=null){
            adapter.notifyDataSetChanged()
        }
    }



    override fun onStart() {
        super.onStart()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding=null
    }
}