package com.keksovmen.flowerbox.entities

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [FlowerBox::class, Sensor::class, Switch::class, Property::class, DataEntry::class],
    version = 1
)
@TypeConverters(Converter::class)
abstract class FlowerBoxDatabase : RoomDatabase() {
    abstract fun flowerBoxDao(): FlowerBoxDao
}
