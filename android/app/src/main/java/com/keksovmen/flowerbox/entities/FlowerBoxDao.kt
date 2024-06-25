package com.keksovmen.flowerbox.entities

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import java.util.function.Consumer

@Dao
interface FlowerBoxDao {
    @Query("SELECT * FROM flower_box")
    fun getAllBoxes(): LiveData<List<FlowerBox>>

    @Query("SELECT * FROM sensor WHERE parent_id = :box_id")
    fun getAllSensors(box_id: Int): LiveData<List<Sensor>>

    @Query("SELECT * FROM switch WHERE parent_id = :box_id")
    fun getAllSwitches(box_id: Int): LiveData<List<Switch>>

    @Query("SELECT * FROM property WHERE parent_id = :box_id")
    fun getAllProperties(box_id: Int): LiveData<List<Property>>

    @Query("SELECT * FROM data_entry WHERE box_id = :box_id AND owner_id = :sensor_id ORDER BY timestamp DESC")
    fun getAllSensorData(box_id: Int, sensor_id: Int): LiveData<List<DataEntry>>

    @Query("SELECT * FROM data_entry WHERE box_id = :box_id AND owner_id = :switch_id ORDER BY timestamp DESC")
    fun getAllSwitchData(box_id: Int, switch_id: Int): LiveData<List<DataEntry>>

    @Query("SELECT * FROM data_entry WHERE box_id = :box_id AND owner_id = :sensor_id ORDER BY timestamp DESC LIMIT 1")
    fun getLastSensorData(box_id: Int, sensor_id: Int): DataEntry?

    @Query("SELECT * FROM data_entry WHERE box_id = :box_id AND owner_id = :switch_id ORDER BY timestamp DESC LIMIT 1")
    fun getLastSwitchData(box_id: Int, switch_id: Int): DataEntry?

    @Query("SELECT * FROM data_entry WHERE (box_id = :box_id AND owner_id = :sensor_id) AND (timestamp BETWEEN :start AND :end) ORDER BY timestamp DESC")
    fun getSensorDataOfPeriod(
        box_id: Int,
        sensor_id: Int,
        start: Long,
        end: Long
    ): LiveData<List<DataEntry>>

    @Transaction
    fun addFlowerBox(
        box: FlowerBox,
        sensors: List<Sensor>,
        switches: List<Switch>,
        properties: List<Property>
    ) {
        addFlowerBox(box)
        properties.forEach(Consumer { property: Property -> addProperty(property) })
        sensors.forEach(Consumer { sensor: Sensor -> addSensor(sensor) })
        switches.forEach(Consumer { sensor: Switch -> addSwitch(sensor) })
    }

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addFlowerBox(box: FlowerBox)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addSensor(sensor: Sensor)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addSwitch(sensor: Switch)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addProperty(property: Property)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun addSensorData(data: DataEntry)

    @Update
    fun updateProperty(property: Property)

    @Delete
    fun deleteFlowerBox(boxId: FlowerBox)
}
