package org.bigblackowl.lamp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import org.bigblackowl.lamp.ui.navigation.SystemNavigation
import org.bigblackowl.lamp.ui.theme.LampTheme
import org.bigblackowl.lamp.ui.viewmodel.LedControlViewModel
import org.bigblackowl.lamp.ui.viewmodel.MdnsViewModel
import org.bigblackowl.lamp.ui.viewmodel.WifiViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App(
    ledControlViewModel: LedControlViewModel = koinViewModel(),
    mdnsViewModel: MdnsViewModel = koinViewModel(),
    wifiViewModel: WifiViewModel = koinViewModel()
) {
    LampTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
            SystemNavigation(ledControlViewModel, mdnsViewModel, wifiViewModel, paddingValues)
        }
    }

}

