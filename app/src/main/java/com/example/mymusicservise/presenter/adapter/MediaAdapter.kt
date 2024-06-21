package com.example.misicplayer.presenter.adapter

import android.database.Cursor
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.mymusicservise.R
import com.example.mymusicservise.data.model.MusicData
import com.example.mymusicservise.databinding.ItemMediaBinding
import com.example.uzummarketclient.utils.getMusicDataByPosition
import com.example.uzummarketclient.utils.myLog

class MediaAdapter : ListAdapter<MusicData,MediaAdapter.Holder>(MediaAdapter.MyDiffUtil) {
    object MyDiffUtil:DiffUtil.ItemCallback<MusicData>() {
        override fun areItemsTheSame(oldItem: MusicData, newItem: MusicData): Boolean =oldItem.id==newItem.id

        override fun areContentsTheSame(oldItem: MusicData, newItem: MusicData): Boolean =false

    }

    var onClickItem: ((Int) -> Unit)? = null
    var id=-1

    inner class Holder(private val binding: ItemMediaBinding) : ViewHolder(binding.root) {
        fun bind() {
            binding.apply {
                val data =getItem(adapterPosition)


                binding.artist.text = data.artist
                binding.name.text = data.title
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
//                if (map.get(data.id)==null){
//                    map.set(data.id,adapterPosition)
//                }
                if (id==data.id){
                    lottieAnimation.playAnimation()
                    lottieAnimation.isVisible=true
                    name.setTextColor(Color.parseColor("#30FB0D"))
                }else{
                    lottieAnimation.pauseAnimation()
                    lottieAnimation.isVisible=false
                    name.setTextColor(Color.WHITE)
                }

            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder =
        Holder(ItemMediaBinding.inflate(LayoutInflater.from(parent.context), parent, false))


    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind()
    }

    fun setPos(id:Int){
        this.id=id
        notifyDataSetChanged()
    }
}