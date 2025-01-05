package org.bigblackowl.lamp.datastorage

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect class DataStorageFactory {
    fun createDataStorage(): DataStorage
}

interface DataStorage {
    fun save(key: String, value: String)
    fun get(key: String): String?
    fun remove(key: String)
    fun clear()
}
