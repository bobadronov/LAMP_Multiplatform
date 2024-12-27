package org.bigblackowl.lamp.data.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.text.intl.Locale


@Composable
fun Locale() {
    LaunchedEffect(Unit) {
        println(Locale.current.language)
        Locale.current.platformLocale
        
    }

//expect val myLang:String?
//expect val myCountry:String?
//iOS
//
//actual val myLang:String?
//    get() = NSLocale.currentLocale.languageCode
//
//actual val myCountry:String?
//    get() = NSLocale.currentLocale.countryCode
//Android
//
//actual val myLang:String?
//    get() = Locale.getDefault().language
//
//actual val myCountry:String?
//    get() = Locale.getDefault().country
}
