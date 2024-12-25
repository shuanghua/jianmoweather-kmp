package ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import getColorScheme
import io.github.aakira.napier.Napier


@Composable
fun JianMoTheme(
    useDarkColors: Boolean,
    useDynamicColors: Boolean,
    content: @Composable () -> Unit,
) {

    Napier.d("Theme-->>$useDarkColors + $useDynamicColors")

    val colorScheme: ColorScheme = getColorScheme(useDarkColors, useDynamicColors)

    val defaultBackgroundTheme = BackgroundTheme(
        color = colorScheme.surface,
        tonalElevation = 2.dp
    )
    val backgroundTheme = when {
        useDynamicColors -> defaultBackgroundTheme
        useDarkColors -> DarkAndroidBackgroundTheme
        else -> LightAndroidBackgroundTheme
    }

    CompositionLocalProvider(
        LocalBackgroundTheme provides backgroundTheme
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = JianMoTypography,
            content = content
        )
    }
}

@Immutable
data class BackgroundTheme(
    val color: Color = Color.Unspecified,
    val tonalElevation: Dp = Dp.Unspecified,
    val primaryGradientColor: Color = Color.Unspecified,
    val secondaryGradientColor: Color = Color.Unspecified,
    val tertiaryGradientColor: Color = Color.Unspecified,
    val neutralGradientColor: Color = Color.Unspecified,
)

val LocalBackgroundTheme = staticCompositionLocalOf { BackgroundTheme() }

val LightAndroidBackgroundTheme = BackgroundTheme(color = DarkGreenGray95)

/**
 * Dark Android background theme
 */
val DarkAndroidBackgroundTheme = BackgroundTheme(color = Color.Black)