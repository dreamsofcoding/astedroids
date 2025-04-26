package com.udacity.asteroidradar.view.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.udacity.asteroidradar.database.PictureOfDayEntity
import com.udacity.asteroidradar.databinding.ItemPictureBinding

class PictureAdapter :
    ListAdapter<PictureOfDayEntity, PictureAdapter.PictureViewHolder>(PictureDiffCallback) {

    class PictureViewHolder(private val binding: ItemPictureBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(picture: PictureOfDayEntity) {
            binding.picture = picture
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PictureViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPictureBinding.inflate(inflater, parent, false)
        return PictureViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PictureViewHolder, position: Int) {
        val picture = getItem(position)
        holder.bind(picture)
    }
}

object PictureDiffCallback : DiffUtil.ItemCallback<PictureOfDayEntity>() {
    override fun areItemsTheSame(
        oldItem: PictureOfDayEntity,
        newItem: PictureOfDayEntity
    ): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(
        oldItem: PictureOfDayEntity,
        newItem: PictureOfDayEntity
    ): Boolean {
        return oldItem == newItem
    }
}