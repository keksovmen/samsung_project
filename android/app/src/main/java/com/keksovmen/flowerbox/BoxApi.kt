package com.keksovmen.flowerbox

import com.keksovmen.flowerbox.entities.DataEntry
import com.keksovmen.flowerbox.entities.FlowerBox
import com.keksovmen.flowerbox.entities.Property
import com.keksovmen.flowerbox.entities.Sensor
import com.keksovmen.flowerbox.entities.Switch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import java.util.function.Consumer

class BoxApi(private val ip: String, private val port: String) {
    private val boxService: BoxService

    init {
        boxService = Retrofit.Builder()
            .baseUrl("http://$ip:$port")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .callTimeout(7, TimeUnit.SECONDS)
                    .connectTimeout(7, TimeUnit.SECONDS)
                    .readTimeout(7, TimeUnit.SECONDS)
                    .writeTimeout(7, TimeUnit.SECONDS)
                    .build()
            )
            .build().create(BoxService::class.java)
    }

    fun fetchFlowerBox(onFinish: Consumer<FlowerBox?>) {
        boxService.fetchBoxInfo().enqueue(createConsumerCb { flowerBox: FlowerBox? ->
            if (flowerBox != null) {
                flowerBox.ip = ip
                flowerBox.port = port
            }
            onFinish.accept(flowerBox)
        })
    }

    fun fetchAllSensors(flowerBox: FlowerBox, onFinish: Consumer<List<Sensor>?>) {
        Globals.executor.submit {
            try {
                val result: MutableList<Sensor> = ArrayList()
                for (id in flowerBox.sensor_ids) {
                    val v = boxService.fetchSensorInfo(id).execute().body()
                    v!!.parent_id = flowerBox.unique_id
                    result.add(v)
                }
                onFinish.accept(result)
            } catch (e: Throwable) {
                e.printStackTrace()
                onFinish.accept(null)
            }
        }
    }

    @Throws(IOException::class)
    fun fetchAllSensors(flowerBox: FlowerBox): List<Sensor> {
        val result: MutableList<Sensor> = ArrayList()
        for (id in flowerBox.sensor_ids) {
            val v = boxService.fetchSensorInfo(id).execute().body()
            v!!.parent_id = flowerBox.unique_id
            result.add(v)
        }

        return result
    }

    fun fetchAllSwitches(flowerBox: FlowerBox, onFinish: Consumer<List<Switch>?>) {
        Globals.executor.submit {
            try {
                val result: MutableList<Switch> = ArrayList()
                for (id in flowerBox.switch_ids) {
                    val v = boxService.fetchSwitchInfo(id).execute().body()
                    v!!.parent_id = flowerBox.unique_id
                    result.add(v)
                }
                onFinish.accept(result)
            } catch (e: Throwable) {
                onFinish.accept(null)
            }
        }
    }

    @Throws(IOException::class)
    fun fetchAllSwitches(flowerBox: FlowerBox): List<Switch> {
        val result: MutableList<Switch> = ArrayList()
        for (id in flowerBox.switch_ids) {
            val v = boxService.fetchSwitchInfo(id).execute().body()
            v!!.parent_id = flowerBox.unique_id
            result.add(v)
        }
        return result
    }

    fun fetchAllProperties(flowerBox: FlowerBox, onFinish: Consumer<List<Property>?>) {
        Globals.executor.submit {
            try {
                val result: MutableList<Property> = ArrayList()
                for (id in flowerBox.properties_ids) {
                    val property = boxService.fetchPropertyInfo(id).execute().body()
                    property!!.parent_id = flowerBox.unique_id
                    result.add(property)
                }
                onFinish.accept(result)
            } catch (e: Throwable) {
                onFinish.accept(null)
            }
        }
    }

    @Throws(IOException::class)
    fun fetchAllProperties(flowerBox: FlowerBox): List<Property> {
        val result: MutableList<Property> = ArrayList()
        for (id in flowerBox.properties_ids) {
            val property = boxService.fetchPropertyInfo(id).execute().body()
            property!!.parent_id = flowerBox.unique_id
            result.add(property)
        }
        return result
    }

    fun fetchSensorData(parent: Sensor, lastTimestamp: Long, onFinish: Consumer<List<DataEntry>?>) {
        boxService.fetchSensorData(parent.id, lastTimestamp).enqueue(
            createConsumerCb { sensorData: List<DataEntry>? ->
                sensorData?.forEach(Consumer { data: DataEntry ->
                    data.box_id = parent.parent_id
                    data.owner_id = parent.id
                })
                onFinish.accept(sensorData)
            }
        )
    }

    fun fetchSwitchData(parent: Switch, lastTimestamp: Long, onFinish: Consumer<List<DataEntry>?>) {
        boxService.fetchSwitchData(parent.id, lastTimestamp).enqueue(
            createConsumerCb { sensorData: List<DataEntry>? ->
                sensorData?.forEach(Consumer { data: DataEntry ->
                    data.box_id = parent.parent_id
                    data.owner_id = parent.id
                })
                onFinish.accept(sensorData)
            }
        )
    }

    fun setPropertyValue(property: Property, value: String, onFinish: Consumer<Boolean>) {
        boxService.setPropertyValue(property.id, value).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                onFinish.accept(response.code() == 200)
            }

            override fun onFailure(call: Call<Void>, throwable: Throwable) {
                onFinish.accept(false)
            }
        })
    }

    private fun <T> createConsumerCb(consumer: Consumer<T?>): Callback<T> {
        return object : Callback<T> {
            override fun onResponse(call: Call<T>, response: Response<T>) {
                Globals.executor.submit { consumer.accept(response.body()) }
            }

            override fun onFailure(call: Call<T>, throwable: Throwable) {
                Globals.executor.submit { consumer.accept(null) }
            }
        }
    }
}
