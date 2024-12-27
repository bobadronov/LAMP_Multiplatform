package org.bigblackowl.lamp.data

import kotlinx.serialization.Serializable

@Serializable
data class Timer(
    val hour: Int = -1, val minute: Int = -1
)