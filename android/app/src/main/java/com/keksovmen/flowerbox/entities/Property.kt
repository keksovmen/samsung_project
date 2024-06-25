package com.keksovmen.flowerbox.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["parent_id", "id"],
    foreignKeys = [ForeignKey(
        entity = FlowerBox::class,
        parentColumns = ["unique_id"],
        childColumns = ["parent_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Property(
    val name: String,
    val description: String,
    val value_type: String,
    var parent_id: Int,
    val id: Int,
    val tid: Int,
    val min_value: Float,
    val max_value: Float,
    var value: Float,
)
