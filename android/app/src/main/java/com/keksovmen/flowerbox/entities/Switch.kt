package com.keksovmen.flowerbox.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import com.keksovmen.flowerbox.Nameable
import java.io.Serializable

@Entity(
    primaryKeys = ["parent_id", "id"],
    foreignKeys = [ForeignKey(
        entity = FlowerBox::class,
        parentColumns = ["unique_id"],
        childColumns = ["parent_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
data class Switch(
    val name: String,
    val description: String,
    var parent_id: Int,
    val id: Int,
    val tid: Int,
    val state: Int,
    val property_ids: ArrayList<Int>,
    val sensors_ids: ArrayList<Int>,
) : Serializable, Nameable {
    override fun getUserName(): String {
        return name
    }
}