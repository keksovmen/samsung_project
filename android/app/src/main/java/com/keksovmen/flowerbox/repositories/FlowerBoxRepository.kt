package com.keksovmen.flowerbox.repositories

import com.keksovmen.flowerbox.BoxApi
import com.keksovmen.flowerbox.entities.FlowerBox
import com.keksovmen.flowerbox.entities.FlowerBoxDatabase
import java.io.IOException
import java.util.function.Consumer

class FlowerBoxRepository(private val database: FlowerBoxDatabase) {
    fun connectNewBox(ip: String, port: Int, onResult: Consumer<Boolean>) {
        val api = BoxApi(ip, port.toString())
        api.fetchFlowerBox { flowerBox: FlowerBox? ->
            if (flowerBox == null) {
                onResult.accept(false)
                return@fetchFlowerBox
            }

            try {
                val sensors = api.fetchAllSensors(flowerBox)
                val switches = api.fetchAllSwitches(flowerBox)
                val properties = api.fetchAllProperties(flowerBox)
                database.flowerBoxDao().addFlowerBox(flowerBox, sensors, switches, properties)

            } catch (e: IOException) {
                e.printStackTrace()
                onResult.accept(false)
            }

            onResult.accept(true)
        }
    }
}
