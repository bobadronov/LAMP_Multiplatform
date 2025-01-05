package org.bigblackowl.lamp.data

import kotlinx.serialization.Serializable

@Serializable
sealed class DataKey(
    val keyName: String
) {
    data object KEY : DataKey("DataKey")
    data object ANDROID : DataKey("AppDataStorage")
    data object IOS : DataKey("AppDataStorage")
    data object DESKTOP : DataKey("appData.properties")
}
