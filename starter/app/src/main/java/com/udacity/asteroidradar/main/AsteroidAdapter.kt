package com.udacity.asteroidradar.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.databinding.ListItemAsteroidBinding

class AsteroidAdapter(private val clickListener: AsteroidListener): ListAdapter<Asteroid, AsteroidViewHolder>(DiffCallBack) {

    override fun onBindViewHolder(holder: AsteroidViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val asteroidItem  = getItem(position)
                holder.itemView.setOnClickListener{clickListener.onClick(asteroidItem)
                holder.bind(asteroidItem)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AsteroidViewHolder {
        return AsteroidViewHolder.from(parent)
    }

    companion object DiffCallBack: DiffUtil.ItemCallback<Asteroid>(){
        override fun areContentsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem == newItem
        }

        override fun areItemsTheSame(oldItem: Asteroid, newItem: Asteroid): Boolean {
            return oldItem.id == newItem.id
         }
    }

}

class AsteroidListener(val clickListener: (asteroid: Asteroid) -> Unit ){
 fun onClick(asteroid: Asteroid) = clickListener(asteroid)
}

class AsteroidViewHolder private constructor(val binding: ListItemAsteroidBinding): ViewHolder(binding.root){
    fun bind(asteroid: Asteroid) {
        binding.asteroid = asteroid
        binding.executePendingBindings()
    }

    companion object {
        fun from(parent: ViewGroup): AsteroidViewHolder {
            val layoutInflater = LayoutInflater.from(parent.context)
            val binding = ListItemAsteroidBinding.inflate(layoutInflater, parent, false)

            return AsteroidViewHolder(binding)
        }
    }

}