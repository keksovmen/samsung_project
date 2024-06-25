package com.keksovmen.flowerbox.linearList

import android.view.ViewGroup

open class LinearListAdapterDecorator<T>(val child: LinearListAdapterIface<T>) :
    LinearListAdapterIface<T>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BindableViewHolderIface<T> {
        return child.onCreateViewHolder(parent, viewType)
    }

    override fun onBindViewHolder(holder: BindableViewHolderIface<T>, position: Int) {
        child.onBindViewHolder(holder, position)
    }

    override fun getItemCount(): Int {
        return child.itemCount
    }

    override val data: List<T>
        get() = child.data
}
