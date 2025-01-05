# General ProGuard configuration
-dontwarn okhttp3.internal.platform.**
-dontwarn android.security.**
-dontwarn android.os.Build$VERSION
-dontwarn org.conscrypt.**
-dontwarn org.bouncycastle.**
-dontwarn org.openjsse.**
-dontwarn android.net.ssl.SSLSockets
-dontwarn android.net.http.X509TrustManagerExtensions
-dontwarn android.util.Log
-dontwarn sun.font.CFont
-dontwarn io.ktor.client.plugins.logging.LoggerJvmKt
-dontwarn io.ktor.utils.io.jvm.javaio.PollersKt
-dontwarn dalvik.system.CloseGuard
-dontwarn androidx.lifecycle.viewmodel.internal.JvmViewModelProviders
-dontwarn androidx.savedstate.Recreator
-dontwarn androidx.savedstate.SavedStateRegistry
-dontwarn io.ktor.http.content.BlockingBridgeKt
-dontwarn io.ktor.network.sockets.UnixSocketAddress
-dontwarn kotlinx.serialization.internal.PlatformKt
-dontwarn okhttp3.internal.platform.android.AndroidSocketAdapter
-dontwarn org.slf4j.LoggerFactory
-dontwarn org.slf4j.helpers.SubstituteLogger

# Ignore warnings about duplicate class definitions
-ignorewarnings

# Keep Ktor classes
-keep class io.ktor.** { *; }
-dontwarn io.ktor.**