package permissions

import android.Manifest
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import io.github.aakira.napier.Napier
import ui.MainScreen

@OptIn(ExperimentalPermissionsApi::class)
@Composable
internal actual fun RequestLocationPermission() {
    var openMainScreen by remember { mutableStateOf(false) }
    var shouldShowRequest by remember { mutableStateOf(false) }
    val locationPermissionState = rememberMultiplePermissionsState(
        listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    if (openMainScreen) {
        shouldShowRequest = false
        MainScreen()
    }

    // 处理 settings 页面返回后的逻辑
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        openMainScreen = when {
            permissions.all { it.value } -> true
            // 勾选不再询问后  允许用户以大概位置或不使用任何位置权限也可进入 App
            permissions.any { !it.value && !locationPermissionState.shouldShowRationale } -> true
            else -> true
        }
    }

    // App 启动时立刻检查权限
    // locationPermissionState.shouldShowRationale = true 意味着可以再次申请
    LaunchedEffect(key1 = locationPermissionState) {
        // 第一次打开 App :
        // locationPermissionState.allPermissionsGranted = false  权限是否已授予
        // locationPermissionState.shouldShowRationale = false    是否需要进一步像用户介绍权限申请理由并再次申请权限
        when {
            // 权限已授予 + 用户上次拒绝了某一个权限，可以再次申请， 但这里我们就不再打扰用户了，让用户直接进入首页
            locationPermissionState.allPermissionsGranted or
                    locationPermissionState.shouldShowRationale  -> openMainScreen = true
            // 用户第一次申请权限，或者不能再次申请
            // 这里只考虑用户第一次申请权限
            // 如果是后续不能调起系统 dialog 了，需要单独记录标记， 然后引导用户去设置页面， 该功能以后会放到 SettingsScreen 页面
            else -> shouldShowRequest = true
        }
    }

    if (shouldShowRequest) {
        PermissionDialog(
            titleText = "需要位置权限",
            leftButtonText = "我想先体验",
            rightButtonText = "请求权限",
            contentText = "应用功能很依赖精确位置权限，请授予精确定位权限\n[省份 区县 及经纬度]",
            dismissButtonAction = { openMainScreen = true },
            confirmButtonAction = {
                // 申请权限触发代码，不管拒绝还是允许，都可以进入 app
                // 因此就不在这里处理: 当只允许大概位置权限的情况
                launcher.launch(
                    listOf(
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                    ).toTypedArray()
                )
            }
        )
    }
}

@Composable
fun PermissionDialog(
    titleText: String,
    leftButtonText: String,
    rightButtonText: String,
    contentText: String,
    confirmButtonAction: () -> Unit = {},
    dismissButtonAction: () -> Unit = {},
) {
    AlertDialog(
        onDismissRequest = { },
        dismissButton = {
            TextButton(onClick = {
                dismissButtonAction()
            }) {
                Text(text = leftButtonText)
            }
        },
        confirmButton = {
            TextButton(onClick = confirmButtonAction) {
                Text(text = rightButtonText)
            }
        },
        title = { Text(text = titleText) },
        text = { Text(text = contentText) }
    )
}