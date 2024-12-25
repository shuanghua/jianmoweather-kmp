import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import ui.theme.DarkDefaultColorScheme
import ui.theme.LightDefaultColorScheme

@Composable
internal actual fun getColorScheme(
    useDarkColors: Boolean,
    useDynamicColors: Boolean
): ColorScheme = when {
    useDarkColors -> DarkDefaultColorScheme
    else -> LightDefaultColorScheme
}

