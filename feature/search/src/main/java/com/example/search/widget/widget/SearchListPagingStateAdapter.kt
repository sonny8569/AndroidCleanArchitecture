package com.example.search.widget.widget

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.search.databinding.AdapterPagingStateBinding

internal class SearchListPagingStateAdapter(private val onClickRetry : () -> Unit) : LoadStateAdapter<SearchListPagingStateAdapter.ViewHolder>() {
    class ViewHolder(
        private val binding: AdapterPagingStateBinding,
        private val onClickRetry: () -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(state: LoadState) {
            with(binding) {
                retry.setOnClickListener { onClickRetry() }
                when (state) {
                    is LoadState.Error -> {
                        errorMessage.isVisible = true
                        retry.isVisible = true
                        progress.isVisible = false
                    }
                    LoadState.Loading -> {
                        progress.isVisible = true
                        errorMessage.isVisible = false
                        retry.isVisible = false
                    }
                    is LoadState.NotLoading -> Unit
                }
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return ViewHolder(AdapterPagingStateBinding.inflate(layoutInflater, parent, false), onClickRetry)

    }
}