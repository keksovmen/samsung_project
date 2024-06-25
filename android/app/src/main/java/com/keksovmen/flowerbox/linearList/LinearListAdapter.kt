package com.keksovmen.flowerbox.linearList

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.function.BiConsumer

class LinearListAdapter<T>(
    override val data: List<T>,
    private val onBind: BiConsumer<T, View>,
    private val layoutId: Int
) : LinearListAdapterIface<T>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolderIface<T> {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return BindableViewHolder(view, onBind)
    }

    override fun onBindViewHolder(holder: BindableViewHolderIface<T>, position: Int) {
        holder.bind(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}
