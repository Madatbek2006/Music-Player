package com.example.mymusicservise.presenter.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.mymusicservise.presenter.screen.home.HomeScreen
import com.example.mymusicservise.presenter.screen.media.MediaScreen

class MainAdapter(
    fm:Fragment,
  private val homeScreen: HomeScreen,
   private val mediaScreen: MediaScreen
):FragmentStateAdapter(fm) {
    override fun getItemCount(): Int =2

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0->homeScreen
            else->mediaScreen
        }
    }
}