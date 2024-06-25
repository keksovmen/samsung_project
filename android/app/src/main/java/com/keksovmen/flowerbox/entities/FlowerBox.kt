package com.keksovmen.flowerbox.entities

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "flower_box")
class FlowerBox(
    val name: String,
    val version: String,
    @PrimaryKey val unique_id: Int,
    var ip: String,
    var port: String,

) : Serializable{
    @Ignore var switch_ids: ArrayList<Int> = ArrayList(0)
    @Ignore var sensor_ids: ArrayList<Int> = ArrayList(0)
    @Ignore var properties_ids: ArrayList<Int> = ArrayList(0)
}
