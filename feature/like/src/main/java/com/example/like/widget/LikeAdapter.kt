package com.example.like.widget

import android.icu.util.IslamicCalendar
import android.view.LayoutInflater
import android.view.RoundedCorner
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.MultiTransformation
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.domain.model.SearchResult
import com.example.like.R
import com.example.like.databinding.AdapterLikeBinding

internal class LikeAdapter(
    private val requestManager: RequestManager,
    private val onClickLike: (SearchResult) -> Unit,
    private val onClickItem: (SearchResult) -> Unit,
) : ListAdapter<SearchResult, RecyclerView.ViewHolder>(diffCallback) {

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<SearchResult>() {
            override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult) =
                oldItem == newItem
        }
    }

    private class LikeViewHolder(
        private val binding: AdapterLikeBinding,
        private val requestManager: RequestManager,
        private val onClickLike: (SearchResult) -> Unit,
        private val onClickItem: (SearchResult) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchResult) {
            with(binding) {
                val context = root.context
                title.text = item.title
                datetime.text = item.dateTime
                setFavoriteImage(item)
                requestManager.load(item.thumbNail).centerCrop().apply(
                    RequestOptions().transform(
                        MultiTransformation(
                            CenterCrop(),
                            RoundedCorners(context.dpToPx(14))
                        )
                    )
                ).into(thumbnail)
                favorite.setOnClickListener {
                    item.isLike = !item.isLike
                    setFavoriteImage(item)
                    onClickLike(item)
                }
                root.setOnClickListener {
                    onClickItem(item)
                }
            }
        }

        private fun AdapterLikeBinding.setFavoriteImage(item: SearchResult) {
            favorite.setImageResource(
                if (item.isLike) R.drawable.ic_like_on
                else R.drawable.ic_like_off
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = AdapterLikeBinding.inflate(inflater, parent, false)
        return LikeViewHolder(binding, requestManager, onClickLike, onClickItem)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is LikeViewHolder -> holder.bind(getItem(position))
        }
    }
}