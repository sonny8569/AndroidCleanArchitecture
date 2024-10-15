package com.example.like.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class FavoriteLikeItemDecoration : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val context = view.context
        with(outRect){
            bottom = context.dpToPx(40)
        }
    }
}