package org.bigblackowl.lamp.di

import org.bigblackowl.lamp.core.network.WiFiServiceImpl
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module
    get() = module {
        single { WiFiServiceImpl() }
    }