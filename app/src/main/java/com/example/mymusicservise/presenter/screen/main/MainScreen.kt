package com.example.mymusicservise.presenter.screen.main

import android.Manifest
import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.get
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mymusicservise.R
import com.example.mymusicservise.data.model.ServiceEnum
import com.example.mymusicservise.data.sourse.MyShar
import com.example.mymusicservise.databinding.ScreenMainBinding
import com.example.mymusicservise.databinding.ScreenMusicBinding
import com.example.mymusicservise.presenter.adapter.MainAdapter
import com.example.mymusicservise.presenter.screen.home.HomeScreen
import com.example.mymusicservise.presenter.screen.media.MediaScreen
import com.example.mymusicservise.presenter.screen.music.MusicScreen
import com.example.mymusicservise.presenter.service.MyService
import com.example.mymusicservise.utils.MyMusicModel
import com.example.uzummarketclient.utils.isHavePermission
import com.example.uzummarketclient.utils.myLog
import com.example.uzummarketclient.utils.requestPermission
import com.example.uzummarketclient.utils.textScrool
import com.google.android.material.bottomsheet.BottomSheetBehavior
import java.util.concurrent.Executors

class MainScreen:Fragment(R.layout.screen_main) {
    private val binding by viewBinding(ScreenMainBinding::bind)
    private lateinit var adapter:MainAdapter

    private val homeScreen=HomeScreen()
    private val mediaScreen= MediaScreen()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initMusic()
        isVisibleMusic(false)
        binding.musicName.textScrool()
        binding.executorName.textScrool()
        cameraPermission()
    }

    override fun onStart() {
        super.onStart()
        val myShar=MyShar.getInstance()

        if (myShar.isOpenMusicScreen()){
            findNavController().navigate(R.id.action_mainScreen_to_musicScreen)
            myShar.setOpenMusic(false)
        }
    }
    private fun initButton(){
        binding.apply {
            adapter=MainAdapter(this@MainScreen,homeScreen, mediaScreen)
            viewPager.adapter=adapter
            viewPager.isUserInputEnabled=false
            for (i in 0..1){
                bottomNavigationView.menu[i].setOnMenuItemClickListener {
                    viewPager.currentItem=i
                    binding.bottomNavigationView.menu[i].isChecked = true
                    return@setOnMenuItemClickListener true
                }
            }
            music.setOnClickListener {
                findNavController().navigate(R.id.action_mainScreen_to_musicScreen)

//                val bottomSheetBehavior = BottomSheetBehavior.from()
//                val bottomSheetBehavior = BottomSheetBehavior.from(a.root)
//                bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
            play.setOnClickListener {
                startMyService(ServiceEnum.MERGE)
            }
        }
    }
    private fun startMyService(play: ServiceEnum) {
        val intent = Intent(requireContext(), MyService::class.java)
        intent.putExtra("COMMAND", play)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            requireActivity().startForegroundService(intent)
            "startForegroundService".myLog()
        } else {
            "startService".myLog()
            requireActivity().startService(intent)
        }
    }



    private fun initMusic(){
        MyMusicModel.playMusicLiveData.observe(viewLifecycleOwner){ data->
            isVisibleMusic(true)
            binding.apply {
            musicName.text=data?.title
            executorName.text=data?.artist
            play.setBackgroundResource(R.drawable.pause)
            if (data?.image!=null){
                val palette: Palette = Palette.from(data.image).generate()
                val dominantColor = palette.getDominantColor(0x000000)
                musicImage.setImageBitmap(data.image)
                music.backgroundTintList= ColorStateList.valueOf(dominantColor)
            }else{
                music.backgroundTintList= ColorStateList.valueOf(MyMusicModel.BG_COLOR)
                musicImage.setImageResource(R.drawable.music_icon)
            }
        }
        }
        MyMusicModel.isPlayingLiveData.observe(viewLifecycleOwner){
            if (it)binding.play.setBackgroundResource(R.drawable.pause)
            else binding.play.setBackgroundResource(R.drawable.play)

        }

    }
    private fun isVisibleMusic(bool:Boolean){
        binding.apply {
            if (bool){
                music.isVisible=true
                musicName.isVisible=true
                executorName.isVisible=true
                play.isVisible=true
            }else{
                music.isVisible=false
                musicName.isVisible=false
                executorName.isVisible=false
                play.isVisible=false
            }
        }
    }
    private fun cameraPermission() {
        val requestPermissionLauncher = registerForActivityResult<String, Boolean>(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                initButton()
            } else {
                if (Build.VERSION.SDK_INT>32){
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_AUDIO)) {
                        // Пользователь отклонил разрешение с выбором "Не спрашивать снова"
                        openSettings()
                    }
                }else{
                    if (!shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        // Пользователь отклонил разрешение с выбором "Не спрашивать снова"
                        openSettings()
                    }
                }
            }
        }

        if (shouldShowRequestPermissionRationale(Manifest.permission.READ_MEDIA_AUDIO)) {
            // Показываем диалоговое окно с объяснением
            AlertDialog.Builder(requireActivity())
                .setTitle("Необходимо разрешение")
                .setMessage("Это приложение нуждается в доступе к камере для съемки фотографий.")
                .setPositiveButton("OK") { dialog, which ->
                    requestPermissionLauncher.launch(
                        if (Build.VERSION.SDK_INT>32)
                        Manifest.permission.READ_MEDIA_AUDIO
                        else  Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                }
                .setNegativeButton("Отмена") { dialog, which ->
                    openSettings()
                    dialog.dismiss()
                }
                .create()
                .show()
        } else {
            // Запрашиваем разрешение
            requestPermissionLauncher.launch(if (Build.VERSION.SDK_INT>32)
                Manifest.permission.READ_MEDIA_AUDIO
            else  Manifest.permission.READ_EXTERNAL_STORAGE)
        }
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", requireActivity().packageName, null)
        intent.setData(uri)
        startActivity(intent)
    }

}

