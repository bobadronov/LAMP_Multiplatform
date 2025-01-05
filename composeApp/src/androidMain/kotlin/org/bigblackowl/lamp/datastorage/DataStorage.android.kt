package org.bigblackowl.lamp.datastorage

import android.content.Context
import android.content.SharedPreferences
import org.bigblackowl.lamp.data.DataKey

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class DataStorageFactory(private val context: Context) {
    actual fun createDataStorage(): DataStorage {
        return AndroidDataStorage(context)
    }
}

class AndroidDataStorage(context: Context) : DataStorage {
    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(DataKey.ANDROID.keyName, Context.MODE_PRIVATE)

    override fun save(key: String, value: String) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    override fun get(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    override fun remove(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }

    override fun clear() {
        sharedPreferences.edit().clear().apply()
    }
}