package com.keksovmen.flowerbox

import com.keksovmen.flowerbox.entities.DataEntry
import com.keksovmen.flowerbox.entities.FlowerBox
import com.keksovmen.flowerbox.entities.Property
import com.keksovmen.flowerbox.entities.Sensor
import com.keksovmen.flowerbox.entities.Switch
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface BoxService {
    @GET("/info")
    fun fetchBoxInfo(): Call<FlowerBox>

    @GET("/sensor/{id}")
    fun fetchSensorInfo(@Path("id") id: Int): Call<Sensor>

    @GET("/switch/{id}")
    fun fetchSwitchInfo(@Path("id") id: Int): Call<Switch>

    @GET("/property/{id}")
    fun fetchPropertyInfo(@Path("id") id: Int): Call<Property>

    @GET("/sensor/{id}/{timestamp}")
    fun fetchSensorData(
        @Path("id") id: Int,
        @Path("timestamp") timestamp: Long
    ): Call<List<DataEntry>>

    @GET("/switch/{id}/{timestamp}")
    fun fetchSwitchData(
        @Path("id") id: Int,
        @Path("timestamp") timestamp: Long
    ): Call<List<DataEntry>>

    @POST("/property/{id}/set/{value}")
    fun setPropertyValue(@Path("id") id: Int, @Path("value") value: String): Call<Void>
}
