package ui

import MainActivityUiState
import MainViewModel
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import app.data.model.ThemeConfig
import favoriteStringResource
import moreStringResource
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.KoinContext
import org.koin.compose.viewmodel.koinViewModel
import permissions.RequestLocationPermission
import ui.favorite.favoritesNavigation
import ui.favorite.favoritesRoute
import ui.more.moreNavigation
import ui.more.moreRoute
import ui.theme.AppBackground
import ui.theme.JianMoTheme
import ui.weather.weatherNavigation
import ui.weather.weatherRoute
import weatherStringResource


@Composable
fun App() {
    KoinContext {
        val viewModel: MainViewModel = koinViewModel()
        val uiState: MainActivityUiState by viewModel.uiState.collectAsStateWithLifecycle()

        JianMoTheme(useDarkColors = shouldUseDarkTheme(uiState), useDynamicColors = true) {
            AppBackground {
                RequestLocationPermission()
            }
        }
    }
}

@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityUiState,
): Boolean = when (uiState) {
    MainActivityUiState.Error -> isSystemInDarkTheme()
    is MainActivityUiState.Success -> when (uiState.themeSettings.themeConfig) {
        ThemeConfig.FOLLOW_SYSTEM -> isSystemInDarkTheme()
        ThemeConfig.LIGHT -> false
        ThemeConfig.Dark -> true
    }
}

@Composable
fun MainScreen(
    navController: NavHostController = rememberNavController()
) {

    val bottomBarState = rememberSaveable { mutableStateOf(true) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()

    // 处理底部导航栏的显示与隐藏
    when (navBackStackEntry?.destination?.route) {
        weatherRoute -> bottomBarState.value = true
        favoritesRoute -> bottomBarState.value = true
        moreRoute -> bottomBarState.value = true
        else -> bottomBarState.value = false // 其它页面默认不显示底部栏
    }

    Scaffold(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onBackground,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
        bottomBar = { AppBottomBar(navController, bottomBarState) }
    ) { innerPadding ->
        Row(  // 左右布局的目的是为了日后适配平板设备时方便调整 BottomBar 位置
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .consumeWindowInsets(innerPadding)
                .windowInsetsPadding(
                    WindowInsets.safeDrawing.only(
                        WindowInsetsSides.Horizontal
                    )
                )
        ) {
            AppNavHost(
                navController = navController,
            )
        }
    }
}

/**
 * 底部 tab 点击切换页面
 * Modifier.navigationBarsPadding() :
 * 如果父 Layout 不设置，子View设置了，则子 View 会让父 Layout 膨胀变大（父 Layout 高度增加），但父Layout依然占据systemBar空间
 * 如果父 Layout 设置了，子 View 不设置，则子 view 并不会去占据systemBar空间
 * 总结：子 View 永远不会改变 父Layout的空间位置，但可以更改父Layout的大小
 */
@Composable
fun AppBottomBar(
    navController: NavController,
    bottomBarState: MutableState<Boolean>
) {
    AnimatedVisibility(
        visible = bottomBarState.value,
        enter = slideInVertically(initialOffsetY = { it }),
        exit = slideOutVertically(targetOffsetY = { it })
    ) {
        val currentSelectedItem by navController.currentScreenAsState() // 由 remember 处理之后
        MainScreenNavigation(
            selectedNavigation = currentSelectedItem,
            onNavigateToBottomBarDestination = { item: MainScreenNavItem ->
                navController.navigate(route = item.screen) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        )
    }
}

/**
 * 给BottomNavBar设置监听、图标、文字等
 */
@Composable
internal fun MainScreenNavigation(
    selectedNavigation: String, //传入 当前正在选中的 item
    onNavigateToBottomBarDestination: (MainScreenNavItem) -> Unit, //传出 用户点击之后的新 item
) {
    NavigationBar(
        // Material 3
        // navigationBarsPadding 远离导航栏,
        // 但下层的 Surface 或 Box 依然填充占用导航栏空间
        // 利用这个方法,另外设置导航栏为透明,就可以设置出统一好看的 ui
        //modifier = modifier.navigationBarsPadding(),
        containerColor = Color.Transparent
    ) {
        bottomBarItems.forEach { item: MainScreenNavItem ->
            NavigationBarItem(
                label = { Text(text = stringResource(item.labelRes)) },
                selected = selectedNavigation == item.screen,
                onClick = { onNavigateToBottomBarDestination(item) },
                icon = {
                    MainScreenNavItemIcon(
                        item = item,
                        selected = selectedNavigation == item.screen
                    )
                }
            )
        }
    }
}

/**
 * 重写监听事件，让选中的页面具有 State 特性
 *
 */
@Composable
private fun NavController.currentScreenAsState(): State<String> {
    val selectedItem = remember { mutableStateOf(weatherNavigation) }
    DisposableEffect(this) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            when {
                //当导航的 route 等于 bottomBar 对应的绑定的route 时
                destination.hierarchy.any { it.route == weatherNavigation } -> {
                    selectedItem.value = weatherNavigation
                }

                destination.hierarchy.any { it.route == favoritesNavigation } -> {
                    selectedItem.value = favoritesNavigation
                }

                destination.hierarchy.any { it.route == moreNavigation } -> {
                    selectedItem.value = moreNavigation
                }
            }
        }
        addOnDestinationChangedListener(listener)
        onDispose { removeOnDestinationChangedListener(listener) }
    }
    return selectedItem
}

/**
 * 设置 bottomBar 图标和文字
 * 用 class 来表示 navigationItem, 每个 navigationItem 就是一个类，类中包含 item 图标和标题以及对应的 Screen route
 */
sealed class MainScreenNavItem(
    val screen: String,
    val labelRes: StringResource,
    val contentDescriptionRes: StringResource
) {
    class ResourceIcon( //普通图片
        screen: String,
        labelRes: StringResource,
        contentDescriptionRes: StringResource,
        val iconResId: DrawableResource,
        val selectedIconResId: DrawableResource? = null
    ) : MainScreenNavItem(screen, labelRes, contentDescriptionRes)

    class VectorIcon( //矢量图片
        screen: String,
        labelRes: StringResource,
        contentDescriptionRes: StringResource,
        val iconImageVector: ImageVector,
        val selectedImageVector: ImageVector? = null
    ) : MainScreenNavItem(screen, labelRes, contentDescriptionRes)
}

private val bottomBarItems = listOf(// 收集 NavigationItem, 并设置对应 screen 、图标和文字
    MainScreenNavItem.VectorIcon(
        screen = favoritesNavigation,
        labelRes = favoriteStringResource,
        contentDescriptionRes = favoriteStringResource,
        iconImageVector = Icons.Outlined.Favorite,
        selectedImageVector = Icons.Default.Favorite
    ),
    MainScreenNavItem.VectorIcon(
        screen = weatherNavigation,
        labelRes = weatherStringResource,
        contentDescriptionRes = weatherStringResource,
        iconImageVector = Icons.Outlined.Home,
        selectedImageVector = Icons.Default.Home
    ),
    MainScreenNavItem.VectorIcon(
        screen = moreNavigation,
        labelRes = moreStringResource,
        contentDescriptionRes = moreStringResource,
        iconImageVector = Icons.Outlined.Menu,
        selectedImageVector = Icons.Default.Menu
    )
)

/**
 * 根据 NavigationItem 设置的图标类型来确定的载入显示图标
 */
@Composable
private fun MainScreenNavItemIcon(item: MainScreenNavItem, selected: Boolean) {
    val painter = when (item) {
        is MainScreenNavItem.ResourceIcon -> painterResource(item.iconResId)
        is MainScreenNavItem.VectorIcon -> rememberVectorPainter(item.iconImageVector)
    }

    val selectedPainter = when (item) {
        is MainScreenNavItem.ResourceIcon -> item.selectedIconResId?.let {
            painterResource(
                it
            )
        }

        is MainScreenNavItem.VectorIcon -> item.selectedImageVector?.let {
            rememberVectorPainter(it)
        }
    }

    if (selectedPainter != null) {
        Crossfade(targetState = selected) {
            Icon(
                painter = if (it) selectedPainter else painter,
                contentDescription = stringResource(item.contentDescriptionRes)
            )
        }
    } else {
        Icon(
            painter = painter,
            contentDescription = stringResource(item.contentDescriptionRes)
        )
    }
}
