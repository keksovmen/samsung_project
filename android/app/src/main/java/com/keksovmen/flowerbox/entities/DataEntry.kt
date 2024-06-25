package com.keksovmen.flowerbox.entities

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "data_entry",
    primaryKeys = ["box_id", "owner_id", "timestamp"],
    foreignKeys = [ForeignKey(
        entity = FlowerBox::class,
        parentColumns = ["unique_id"],
        childColumns = ["box_id"],
        onDelete = ForeignKey.CASCADE
    )]
)

data class DataEntry (
    val value: Float,
    val timestamp: Long,
    var box_id: Int,
    var owner_id: Int,
)
