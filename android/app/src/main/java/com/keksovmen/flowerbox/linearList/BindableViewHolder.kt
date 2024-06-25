package com.keksovmen.flowerbox.linearList

import android.view.View
import java.util.function.BiConsumer

class BindableViewHolder<T>(itemView: View, private val onBind: BiConsumer<T, View>) :
    BindableViewHolderIface<T>(itemView) {

    override fun bind(v: T) {
        onBind.accept(v, itemView)
    }
}
