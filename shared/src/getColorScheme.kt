import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
internal expect fun getColorScheme(
    useDarkColors: Boolean,
    useDynamicColors: Boolean,
): ColorScheme

