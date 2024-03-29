package com.bahadir.services.ui.selectmusic

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bahadir.core.data.model.MusicUI
import com.bahadir.services.databinding.ItemMusicBinding

class MusicAdapter(private val musicList: List<MusicUI>, private val onClick: (Int) -> Unit) :
    RecyclerView.Adapter<MusicAdapter.ViewHolder>() {
    inner class ViewHolder(private val binding: ItemMusicBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(musicUI: MusicUI) {
            with(binding) {
                textTitle.text = musicUI.title
                textTime.text = musicUI.duration

                binding.root.setOnClickListener {
                    onClick.invoke(layoutPosition)
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemMusicBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int = musicList.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(musicList[position])

}