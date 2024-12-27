package org.bigblackowl.lamp.ui.theme

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

val DefaultColorList = listOf(
    Color.White,
    Color.Red,
    Color.Green,
    Color.Blue,
    Color.Yellow,
    Color.Cyan,
    Color.Magenta,
    Color(0xFF7700b3),
    Color(0xFFff5722),  // Orange
    Color(0xFF8bc34a),  // Light Green
    Color(0xFF009688),  // Teal
    Color(0xFF3f51b5),  // Indigo
    Color(0xFFFF9800),  // Amber
    Color(0xFF2196F3),  // Light Blue
    Color(0xffb023ff)
)


val DarkBackground = Color(0xff171717)
val LightBackground = Color(0xffcacaca)

val PrimaryColor = Color(0xFF00FF00)  // Neon Green
val SecondaryColor = Color(0xFF00FFFF)  // Neon Blue
val ErrorColor = Color(0xFFFF0000)  // Red
val SurfaceColor = Color(0xff434343)  // Dark Surface
val OnPrimaryColor = Color(0xFFFFFFFF)  // White Text on Primary
val OnSurfaceColor = Color(0xFFFFFFFF)  // White Text on Surface
val neonGreen = Color(0xFF00FF00)
val neonPink = Color(0xFFFF00FF)
val neonBlue = Color(0xFF00FFFF)
val darkGray = Color(0xFF2C2C2C)
val neonYellow = Color(0xFFFFFF00)

val neonTextFieldColors = TextFieldColors(
    focusedTextColor = neonGreen,
    unfocusedTextColor = neonBlue,
    disabledTextColor = darkGray,
    errorTextColor = ErrorColor,

    focusedContainerColor = DarkBackground,
    unfocusedContainerColor = DarkBackground,
    disabledContainerColor = darkGray,
    errorContainerColor = darkGray,

    cursorColor = neonPink,
    errorCursorColor = ErrorColor,

    textSelectionColors = TextSelectionColors(
        handleColor = neonYellow,
        backgroundColor = neonYellow.copy(alpha = 0.4f)
    ),

    focusedIndicatorColor = neonGreen,
    unfocusedIndicatorColor = neonBlue,
    disabledIndicatorColor = darkGray,
    errorIndicatorColor = ErrorColor,

    focusedLeadingIconColor = neonGreen,
    unfocusedLeadingIconColor = neonBlue,
    disabledLeadingIconColor = darkGray,
    errorLeadingIconColor = ErrorColor,

    focusedTrailingIconColor = neonGreen,
    unfocusedTrailingIconColor = neonBlue,
    disabledTrailingIconColor = darkGray,
    errorTrailingIconColor = ErrorColor,

    focusedLabelColor = neonGreen,
    unfocusedLabelColor = neonBlue,
    disabledLabelColor = darkGray,
    errorLabelColor = ErrorColor,

    focusedPlaceholderColor = neonGreen.copy(alpha = 0.7f),
    unfocusedPlaceholderColor = neonBlue.copy(alpha = 0.7f),
    disabledPlaceholderColor = darkGray,
    errorPlaceholderColor = ErrorColor.copy(alpha = 0.7f),

    focusedSupportingTextColor = neonGreen,
    unfocusedSupportingTextColor = neonBlue,
    disabledSupportingTextColor = darkGray,
    errorSupportingTextColor = ErrorColor,

    focusedPrefixColor = neonGreen,
    unfocusedPrefixColor = neonBlue,
    disabledPrefixColor = darkGray,
    errorPrefixColor = ErrorColor,

    focusedSuffixColor = neonGreen,
    unfocusedSuffixColor = neonBlue,
    disabledSuffixColor = darkGray,
    errorSuffixColor = ErrorColor
)
val darkColorPalette = darkColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    error = ErrorColor,
    background = DarkBackground,
    surface = SurfaceColor,
    onPrimary = OnPrimaryColor,
    onSurface = OnSurfaceColor,
)


// Light Theme Colors
val lightColorPalette = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    error = ErrorColor,
    background = LightBackground,
    surface = SurfaceColor,
    onPrimary = OnPrimaryColor,
    onSurface = OnSurfaceColor,
)