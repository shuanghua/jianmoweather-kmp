import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import app.data.model.ThemeConfig
import app.data.repo.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class MainViewModel(
    settingsRepo: SettingsRepository,
) : ViewModel() {

    val uiState: StateFlow<MainActivityUiState> =
        settingsRepo.getTheme().map {
            MainActivityUiState.Success(
                themeSettings = ThemeSettings(it)
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MainActivityUiState.Error,
        )
}

data class ThemeSettings(
    val themeConfig: ThemeConfig,
)

sealed interface MainActivityUiState {
    data object Error : MainActivityUiState
    data class Success(val themeSettings: ThemeSettings) : MainActivityUiState
}