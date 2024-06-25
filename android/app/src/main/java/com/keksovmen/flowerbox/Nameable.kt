package com.keksovmen.flowerbox

import java.io.Serializable

interface Nameable : Serializable {
    fun getUserName(): String
}
