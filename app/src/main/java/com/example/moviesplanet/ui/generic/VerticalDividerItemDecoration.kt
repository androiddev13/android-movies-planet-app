package com.example.moviesplanet.ui.generic

import android.content.Context
import android.graphics.Canvas
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesplanet.R

class VerticalDividerItemDecoration(context: Context,
                                    private val includeItemPadding: Boolean = false,
                                    private val drawAfterLastItem: Boolean = false,
                                    @DrawableRes private val drawableId: Int = R.drawable.list_vertical_divider) : RecyclerView.ItemDecoration() {

    private val divider = ContextCompat.getDrawable(context, drawableId)

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        val left = if (includeItemPadding) parent.paddingLeft else 0
        val right = if (includeItemPadding) parent.width - parent.paddingRight else parent.width

        val count = if (drawAfterLastItem) parent.childCount else parent.childCount - 1
        for (i in 0 until count) {
            val view = parent.getChildAt(i)
            val layoutParams = view.layoutParams as RecyclerView.LayoutParams
            val top = view.bottom + layoutParams.bottomMargin
            val bottom = top + (divider?.intrinsicHeight?: 0)
            divider?.apply {
                setBounds(left, top, right, bottom)
                draw(c)
            }
        }
    }
}