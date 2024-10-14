package com.example.search.widget.widget

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.domain.model.SearchResult
import com.example.search.R
import com.example.search.databinding.AdapterPageBinding
import com.example.search.databinding.AdapterSearchFeedBinding
import com.example.search.widget.utill.Type_Image
import com.example.search.widget.utill.Type_video

class SearchAdapter(
    private val requestManger: RequestManager,
    private val onClickFavoriteItem: (SearchResult) -> Unit,
) : PagingDataAdapter<SearchResult, RecyclerView.ViewHolder>(diffCallback) {
    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<SearchResult>() {
            override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult) =
                oldItem == newItem
        }
    }
    private class FeedViewHolder(
        private val binding: AdapterSearchFeedBinding,
        private val requestManger: RequestManager,
        private val onClickFavoriteItem: (SearchResult) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchResult) {
            with(binding) {
                val context = root.context
                url.text = item.url
                title.text = item.title
                datetime.text = item.dateTime
                setFavoriteImage(item)
                setBadge(item)
                setThumbNail(item, context)
                favorite.setOnClickListener {
                    item.isLike = !item.isLike
                    setFavoriteImage(item)
                    onClickFavoriteItem(item)
                }
            }
        }

        private fun AdapterSearchFeedBinding.setFavoriteImage(item: SearchResult) {
            favorite.setImageResource(
                if (item.isLike) R.drawable.ic_like_on
                else R.drawable.ic_like_off
            )
        }

        private fun AdapterSearchFeedBinding.setBadge(item: SearchResult) {
            when (item.type) {
                Type_Image -> {
                    category.text = item.type
                    badge.setImageResource(R.drawable.ic_img)
                    category.isVisible = true
                }

                Type_video -> {
                    badge.setImageResource(R.drawable.ic_video)
                    category.isVisible = false
                }
            }
        }

        private fun AdapterSearchFeedBinding.setThumbNail(item: SearchResult, context: Context) {
            requestManger.load(item.thumbNail).centerCrop().apply(
                RequestOptions().transform(
                    MultiTransformation(
                        CenterCrop(),
                        RoundedCorners(context.dpToPx(14))
                    )
                )
            ).into(thumbnail)
        }

        private fun Context.dpToPx(dp: Int): Int {
            return (dp * resources.displayMetrics.density).toInt()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder){
            is FeedViewHolder -> holder.bind(getItem(position) as SearchResult)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return FeedViewHolder(
            AdapterSearchFeedBinding.inflate(inflater , parent , false),
            requestManger,
            onClickFavoriteItem
        )
    }
}