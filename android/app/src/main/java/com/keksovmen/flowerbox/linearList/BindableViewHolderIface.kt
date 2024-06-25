package com.keksovmen.flowerbox.linearList

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BindableViewHolderIface<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
    abstract fun bind(v: T)
}
