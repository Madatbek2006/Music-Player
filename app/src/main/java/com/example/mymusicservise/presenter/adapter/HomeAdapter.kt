package com.example.misicplayer.presenter.adapter

import android.annotation.SuppressLint
import android.database.Cursor
import android.graphics.Color
import android.text.method.MovementMethod
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import androidx.room.util.newStringBuilder
import com.example.mymusicservise.R
import com.example.mymusicservise.data.model.MusicData
import com.example.mymusicservise.databinding.ItemMediaBinding
import com.example.uzummarketclient.utils.getMusicDataByPosition
import com.example.uzummarketclient.utils.getPosMusic
import com.example.uzummarketclient.utils.myLog
import com.example.uzummarketclient.utils.textNotScrool
import com.example.uzummarketclient.utils.textScrool

class HomeAdapter : Adapter<HomeAdapter.Holder>() {
    var onClickItem: ((Int) -> Unit)? = null
    var cursor: Cursor? = null
    var id=-1

    inner class Holder(private val binding: ItemMediaBinding) : ViewHolder(binding.root) {
        fun bind() {
            binding.apply {
                val data =
                    cursor!!.getMusicDataByPosition(pos = adapterPosition, binding.root.context)
                name.text = data.title

                binding.artist.text = data.artist
                if (data.image != null) {
//                    Glide.with(root)
//                        .load(data.image)
//                        .error(R.drawable.music_icon)
//                        .placeholder(R.drawable.music_icon)
//                        .into(image)
                    image.setImageBitmap(data.image)
                } else {
                    image.setImageResource(R.drawable.music_icon)
                }
                root.setOnClickListener {
                    onClickItem?.invoke(adapterPosition)
                }
//                "music id=> ${data.id}".myLog()
////                if (map.get(data.id)==null){
////                    map.set(data.id,adapterPosition)
////                }
//                "currwntPos=>$currentPos".myLog()
                "id=>$id\ndata.id=>${data.id}".myLog()
                if (id==data.id){
                    name.textScrool()
                    artist.textScrool()
                    lottieAnimation.playAnimation()
                    lottieAnimation.isVisible=true
                    name.setTextColor(Color.parseColor("#30FB0D"))
                }else{
                    name.textNotScrool()
                    artist.textNotScrool()
                    lottieAnimation.pauseAnimation()
                    lottieAnimation.isVisible=false
                    name.setTextColor(Color.WHITE)
                }

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false))

    override fun getItemCount(): Int = cursor?.count ?: 0

    override fun onBindViewHolder(holder: Holder, position: Int) {
        cursor?.moveToPosition(position)
        holder.bind()
    }

    @SuppressLint("SuspiciousIndentation")
    fun setPos(id:Int){
      this.id=id
        notifyDataSetChanged()
    }
}