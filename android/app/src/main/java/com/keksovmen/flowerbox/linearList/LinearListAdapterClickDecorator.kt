package com.keksovmen.flowerbox.linearList

import android.view.View
import java.util.function.Consumer

class LinearListAdapterClickDecorator<T>(
    child: LinearListAdapterIface<T>,
    private val onClickListener: Consumer<T>
) : LinearListAdapterDecorator<T>(
    child
) {
    override fun onBindViewHolder(holder: BindableViewHolderIface<T>, position: Int) {
        super.onBindViewHolder(holder, position)
        holder.itemView.setOnClickListener { v: View -> onClickListener.accept(child.data[position]) }
    }
}
