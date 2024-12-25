import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import ui.theme.DarkDefaultColorScheme
import ui.theme.LightDefaultColorScheme

@Composable
internal actual fun getColorScheme(
    useDarkColors: Boolean,
    useDynamicColors: Boolean
): ColorScheme = when {
    Build.VERSION.SDK_INT >= 31 && useDynamicColors && useDarkColors -> {
        dynamicDarkColorScheme(LocalContext.current) // 暗色的动态主题
    }

    Build.VERSION.SDK_INT >= 31 && useDynamicColors && !useDarkColors -> {
        dynamicLightColorScheme(LocalContext.current) // 亮色的动态主题
    }

    useDarkColors -> DarkDefaultColorScheme
    else -> LightDefaultColorScheme
}


