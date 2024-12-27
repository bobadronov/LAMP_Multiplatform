package org.bigblackowl.lamp.di

import org.bigblackowl.lamp.core.mdns.MdnsServiceFactory
import org.bigblackowl.lamp.core.network.WebSocketClient
import org.bigblackowl.lamp.ui.viewmodel.LedControlViewModel
import org.bigblackowl.lamp.ui.viewmodel.MdnsViewModel
import org.bigblackowl.lamp.ui.viewmodel.SetupDeviceViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module


expect val platformModule: Module

val sharedModule = module {
    single { WebSocketClient() }
    single { MdnsServiceFactory().createMdnsService() }
    viewModel { LedControlViewModel(get()) }
    viewModel { MdnsViewModel(get()) }
    single { SetupDeviceViewModel(get()) }
}

