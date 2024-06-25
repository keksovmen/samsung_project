package com.keksovmen.flowerbox

import android.content.Context
import androidx.room.Room.databaseBuilder
import com.keksovmen.flowerbox.entities.FlowerBoxDatabase
import java.text.SimpleDateFormat
import java.util.concurrent.Executors

object Globals {
    const val ARGUMENT_BOX = "box"
    const val ARGUMENT_PARENT = "parent"

    @JvmField
    val executor = Executors.newFixedThreadPool(4)
    @JvmField
    val formatDataView = SimpleDateFormat("HH:mm:ss\ndd/MM/YY")
    @JvmField
    val formatTimeView = SimpleDateFormat("HH:mm:ss")

    private var dataBase: FlowerBoxDatabase? = null

    @JvmStatic
    fun getDataBase(context: Context): FlowerBoxDatabase {
        if (dataBase == null) {
            dataBase =
                databaseBuilder(context, FlowerBoxDatabase::class.java, "data_base").build()
        }
        return dataBase!!
    }

    @JvmStatic
    fun tidToIcon(tid: Int): Int {
        when (tid) {
            1, 514, 515 -> return R.drawable.temperature
            2 -> return R.drawable.pressure
            3, 516 -> return R.drawable.humidity
            256 -> return R.drawable.light
            257 -> return R.drawable.heater
            258 -> return R.drawable.wenting
            512, 513 -> return R.drawable.time
            1024 -> return R.drawable.period
        }
        return R.drawable.ic_launcher_foreground
    }
}
