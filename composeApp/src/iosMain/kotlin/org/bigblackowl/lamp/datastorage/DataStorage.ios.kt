package org.bigblackowl.lamp.datastorage

import platform.Foundation.NSUserDefaults

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DataStorageFactory {
    actual fun createDataStorage(): DataStorage {
        return IOSDataStorage()
    }
}

class IOSDataStorage : DataStorage {
    private val userDefaults = NSUserDefaults.standardUserDefaults

    override fun save(key: String, value: String) {
        userDefaults.setObject(value, forKey = key)
    }

    override fun get(key: String): String? {
        return userDefaults.stringForKey(key)
    }

    override fun remove(key: String) {
        userDefaults.removeObjectForKey(key)
    }

    override fun clear() {
        userDefaults.dictionaryRepresentation().keys.forEach { key ->
            userDefaults.removeObjectForKey(key.toString())
        }
    }
}