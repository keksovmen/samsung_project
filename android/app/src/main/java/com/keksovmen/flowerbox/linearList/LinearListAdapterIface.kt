package com.keksovmen.flowerbox.linearList

import androidx.recyclerview.widget.RecyclerView

abstract class LinearListAdapterIface<T> : RecyclerView.Adapter<BindableViewHolderIface<T>>() {
    abstract val data: List<T>
}
