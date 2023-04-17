package com.rifqi.testpaging3.menu

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import com.rifqi.testpaging3.data.remote.ListStoryData
import com.rifqi.testpaging3.databinding.ItemRowStoryBinding
import com.rifqi.testpaging3.detail.DetailActivity

class StoryAdapter : PagingDataAdapter<ListStoryData, StoryAdapter.MyViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryData>() {
            override fun areItemsTheSame(oldItem: ListStoryData, newItem: ListStoryData): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(
                oldItem: ListStoryData,
                newItem: ListStoryData
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }


    class MyViewHolder(private var binding: ItemRowStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {


        fun bind(story: ListStoryData) {

            Glide.with(itemView.context)
                .load(story.photoUrl)
                .apply(
                RequestOptions().diskCacheStrategy(
                    DiskCacheStrategy.AUTOMATIC
                )
            )
                .listener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {

                        return false
                    }
                }).into(binding.imgStory)
            binding.tvTitleStory.text = story.name
            binding.tvDescStory.text = story.description


            itemView.setOnClickListener {
                val intent = Intent(itemView.context, DetailActivity::class.java)
                intent.putExtra("story", story)

                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.imgStory, "image"),
                        Pair(binding.tvTitleStory, "name"),
                        Pair(binding.tvDescStory, "description"),
                    )
                itemView.context.startActivity(intent, optionsCompat.toBundle())

            }
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding =
            ItemRowStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }
}