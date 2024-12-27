package org.bigblackowl.lamp.data

import kotlinx.serialization.Serializable

@Serializable
data class SetupEspCredential(
    val ssid: String,
    val password: String,
    val deviceName: String,
)