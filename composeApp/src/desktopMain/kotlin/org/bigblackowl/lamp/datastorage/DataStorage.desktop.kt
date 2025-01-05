package org.bigblackowl.lamp.datastorage

import org.bigblackowl.lamp.data.DataKey
import java.io.File
import java.util.Properties


@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DataStorageFactory {
    actual fun createDataStorage(): DataStorage {
        return DesktopDataStorage()
    }
}

class DesktopDataStorage : DataStorage {
    private val file = File(DataKey.DESKTOP.keyName)
    private val properties = Properties()

    init {
        if (file.exists()) {
            file.inputStream().use { properties.load(it) }
        }
    }

    override fun save(key: String, value: String) {
        properties[key] = value
        file.outputStream().use { properties.store(it, null) }
    }

    override fun get(key: String): String? {
        return properties.getProperty(key)
    }

    override fun remove(key: String) {
        properties.remove(key)
        file.outputStream().use { properties.store(it, null) }
    }

    override fun clear() {
        properties.clear()
        file.outputStream().use { properties.store(it, null) }
    }
}
