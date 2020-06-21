package com.example.usergallerydemo

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintSet
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.usergallerydemo.databinding.ItemGalleryBinding


class MainRvAdapter : ListAdapter<Media, MainRvAdapter.ViewHolder>(
    DIFF_CALLBACK
) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(
            parent
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class ViewHolder private constructor(private val binding: ItemGalleryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val set = ConstraintSet()

        @SuppressLint("CheckResult")
        fun bind(
            media: Media
        ) {
            with(set) {
                @SuppressLint("DefaultLocale")
                val ratio = String.format("%d:%d", media.width, media.height)
                clone(binding.clRoot)
                setDimensionRatio(binding.ivImage.id, ratio)
                applyTo(binding.clRoot)
            }

            GlideApp
                .with(binding.ivImage.context)
                .load(media.uri)
                .into(binding.ivImage)

            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ItemGalleryBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(
                    binding
                )
            }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Media>() {
            override fun areItemsTheSame(oldItem: Media, newItem: Media): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: Media, newItem: Media): Boolean =
                oldItem == newItem
        }
    }
}