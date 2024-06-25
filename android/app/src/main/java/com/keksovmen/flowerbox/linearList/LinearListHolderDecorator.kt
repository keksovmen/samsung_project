package com.keksovmen.flowerbox.linearList

import java.util.function.BiConsumer

class LinearListHolderDecorator<T>(
    child: LinearListAdapterIface<T>,
    private val decor: BiConsumer<BindableViewHolderIface<T>, Int>
) : LinearListAdapterDecorator<T>(
    child
) {
    override fun onBindViewHolder(holder: BindableViewHolderIface<T>, position: Int) {
        super.onBindViewHolder(holder, position)
        decor.accept(holder, position)
    }
}
