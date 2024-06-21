package com.example.mymusicservise.presenter.screen.music
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.database.Cursor
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.palette.graphics.Palette
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.mymusicservise.R
import com.example.mymusicservise.data.model.MusicData
import com.example.mymusicservise.data.model.ServiceEnum
import com.example.mymusicservise.data.sourse.MyShar
import com.example.mymusicservise.databinding.ScreenMusicBinding
import com.example.mymusicservise.presenter.service.MyService
import com.example.mymusicservise.utils.MyMusicModel
import com.example.uzummarketclient.utils.durationConverter
import com.example.uzummarketclient.utils.myLog
import com.example.uzummarketclient.utils.setChangeProgress
import com.example.uzummarketclient.utils.setStatusBar
import com.example.uzummarketclient.utils.textScrool
import kotlinx.coroutines.launch


class MusicScreen:Fragment() {
    private val binding by viewBinding(ScreenMusicBinding::bind)
    private lateinit var currentMusic:MusicData
    private val myShar by lazy{ MyShar.getInstance()}
    private val currentTimeObserver=Observer<Long>{
        binding.apply {
            "$it  it".myLog()
            seekBar.progress = it.toInt()
            binding.chronometer.text= it.durationConverter()
            back.setOnClickListener {
                findNavController().navigateUp()
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.screen_music, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().setStatusBar(binding.spase)

        binding.apply {
            lifecycleScope.launch {
                autor.textScrool()
            }
            binding.next.setOnClickListener {
                startMyService(ServiceEnum.NEXT)
            }
            binding.prev.setOnClickListener {
                startMyService(ServiceEnum.PREV)
            }
            binding.play.setOnClickListener {
                startMyService(ServiceEnum.MERGE)
            }
            MyMusicModel.playMusicLiveData.observe(viewLifecycleOwner){ data->
                currentMusic=data
                binding.apply {
                    play.setImageResource(R.drawable.pause)
                    seekBar.max=MyMusicModel.fullTime.toInt()
                    seekBar.progress = 0
                    chronometer.text=0L.durationConverter()
                    timeMusic.text= MyMusicModel.fullTime.durationConverter()
                    name.text=data?.title
                    autor.text=data?.artist
                    if (data?.image!=null){
                        val palette: Palette = Palette.from(data.image).generate()
                        val dominantColor = palette.getDominantColor(0x000000)
                        root.setBackgroundColor(dominantColor)
                        image.setImageBitmap(data.image)
                    }else{
                        root.setBackgroundColor(MyMusicModel.BG_COLOR)
                        image.setImageResource(com.example.mymusicservise.R.drawable.music_icon)
                    }
                }
                initButton()
            }
            MyMusicModel.currentTimeLiveData.observe(viewLifecycleOwner,currentTimeObserver)
            MyMusicModel.isPlayingLiveData.observe(viewLifecycleOwner){
                if (it){
                    binding.play.setImageResource(R.drawable.pause)
                }else{
                    binding.play.setImageResource(R.drawable.play)
                }
            }


            seekBar.setChangeProgress { progress, fromUser ->

                if (fromUser){
                    binding.chronometer.text=progress.toLong().durationConverter()
                    MyMusicModel.currentTime=progress.toLong()
                    startMyService(ServiceEnum.SEEK)
                }
            }
            val myShar=MyShar.getInstance()
            shuffle.setOnClickListener {
                myShar.setShuffle(!myShar.isShuffle())
                if (myShar.isShuffle()){
                    shuffle.imageTintList= ColorStateList.valueOf(Color.parseColor("#30FB0D"))
                }else{
                    shuffle.imageTintList= ColorStateList.valueOf(Color.WHITE)
                }
            }
            if (myShar.isShuffle()){
                shuffle.imageTintList= ColorStateList.valueOf(Color.parseColor("#30FB0D"))
            }else{
                shuffle.imageTintList= ColorStateList.valueOf(Color.WHITE)
            }
        }
    }
    private fun startMyService(play: ServiceEnum) {
        "startMyService".myLog()
        val intent = Intent(requireContext(), MyService::class.java)
        intent.putExtra("COMMAND", play)
        if (Build.VERSION.SDK_INT >= 26) {
            requireActivity().startForegroundService(intent)
            "startForegroundService".myLog()
        } else {
            "startService".myLog()
            requireActivity().startService(intent)
        }
    }

fun getPathFromUri(context: Context, uri: Uri): String? {
    var cursor: Cursor?=null
    try {
        // Проверяем, является ли URI content:// URI
        if (uri.scheme.equals("content", ignoreCase = true)) {
            val projection = arrayOf(MediaStore.Images.Media.DATA)
            cursor = context.contentResolver.query(uri, projection,null, null, null)
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                return cursor.getString(columnIndex)
            }
        }
        // Проверяем, является ли URI file:// URI
        else if (uri.scheme.equals("file", ignoreCase = true)) {
            return uri.path
        }
    } finally {
        cursor?.close()
    }
    return null
}
    var isFavorite=false
    fun initButton(){
        binding.apply {
            if (myShar.getIsFavorite().contains(currentMusic.id)){
                isFavorite=true
                favorite.setBackgroundResource(R.drawable.favorite2)
            }else{
                isFavorite=false
                favorite.setBackgroundResource(R.drawable.favorite1)
            }

            favorite.setOnClickListener {
//                animFavorite()
                if (isFavorite){
                    isFavorite=false
                    favorite.setBackgroundResource(R.drawable.favorite1)
                }else{
                    isFavorite=true
                    favorite.setBackgroundResource(R.drawable.favorite2)
                }
                saveCurrentMusic()
            }
        }
    }

    fun animFavorite()=binding.apply {
        if (isFavorite){
            isFavorite=false
            favorite.animate()
                .setDuration(100)
                .rotation(-45f)
                .withEndAction {
                    favorite.rotation=45f
                    favorite.setBackgroundResource(R.drawable.plus_circle)
                    favorite.animate()
                        .setDuration(100)
                        .rotation(-0f)
                        .start()

                }
                .start()
        }else{
            isFavorite=true
            favorite.animate()
                .setDuration(100)
                .rotation(45f)
                .withEndAction {
                    favorite.rotation=-45f
                    favorite.setBackgroundResource(R.drawable.baseline_check_circle_24)
                    favorite.animate()
                        .setDuration(100)
                        .rotation(0f).start()

                }
                .start()
        }
    }
    private fun saveCurrentMusic(){
        val data=ArrayList(myShar.getIsFavorite())
        if (isFavorite){
            if (!data.contains(currentMusic.id)){
                data.add(currentMusic.id)
            }
        }else{
            if (data.contains(currentMusic.id)){
                data.remove(currentMusic.id)
            }
        }
        myShar.setIsFavorite(data)
    }

    override fun onResume() {
        super.onResume()
    }

}
