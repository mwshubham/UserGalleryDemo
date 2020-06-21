package com.example.usergallerydemo

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager


class StaggeredGridLayoutManagerEqualSpacingItemDecoration(
    private val spaceHeight: Int
) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val lp = view.layoutParams as StaggeredGridLayoutManager.LayoutParams
        val spanIndex = lp.spanIndex
        outRect.top = spaceHeight
        if (spanIndex == 0) {
            outRect.left = spaceHeight
            outRect.right = spaceHeight
        } else {
            outRect.right = spaceHeight
        }
        outRect.bottom = spaceHeight
    }
}