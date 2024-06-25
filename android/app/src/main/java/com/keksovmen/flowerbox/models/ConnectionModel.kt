package com.keksovmen.flowerbox.models

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.keksovmen.flowerbox.Globals
import com.keksovmen.flowerbox.repositories.FlowerBoxRepository

class ConnectionModel : ViewModel() {
    enum class Result {
        NONE,
        IN_PROGRESS,
        SUCCESS,
        FAILURE
    }

    private val state: MutableLiveData<Result> = MutableLiveData(Result.NONE)


    fun connectNewBox(ip: String, port: Int, context: Context) {
        //TODO: check parameters and return immediate result for their validity
        state.value = Result.IN_PROGRESS
        FlowerBoxRepository(Globals.getDataBase(context)).connectNewBox(ip, port) {
            state.postValue(if (it) Result.SUCCESS else Result.FAILURE)
        }
    }

    fun resetConnection() {
        state.value = Result.NONE
    }

    fun getStateLiveData(): LiveData<Result> {
        return state
    }
}
