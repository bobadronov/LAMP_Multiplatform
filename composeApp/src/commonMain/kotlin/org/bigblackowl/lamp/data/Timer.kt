package org.bigblackowl.lamp.data

import kotlinx.serialization.Serializable

@Serializable
data class Timer(
    val day: Int = -1,
    val month: Int = -1,
    val year: Int = -1,
    val hour: Int = -1,
    val minute: Int = -1
)
