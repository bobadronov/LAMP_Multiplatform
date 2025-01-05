package org.bigblackowl.lamp.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.bigblackowl.lamp.core.network.WiFiServiceImpl
import org.bigblackowl.lamp.datastorage.AndroidDataStorage
import org.bigblackowl.lamp.datastorage.DataStorageFactory
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single<HttpClientEngine> { OkHttp.create() }
        single { WiFiServiceImpl(androidContext()) }
        single { DataStorageFactory(androidContext()).createDataStorage() }
        single { AndroidDataStorage(androidContext()) }
    }
