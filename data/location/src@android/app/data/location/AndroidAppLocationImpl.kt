package app.data.location

import android.content.Context
import app.data.model.Location
import com.amap.api.location.AMapLocation
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import kotlinx.coroutines.suspendCancellableCoroutine
import org.koin.java.KoinJavaComponent.inject
import kotlin.coroutines.resume

actual fun getLocationDataSource(): LocationDataSource = AndroidAppLocationImpl()

private class AndroidAppLocationImpl : LocationDataSource {
    private val context: Context by inject(clazz = Context::class.java)

    private val client: AMapLocationClient

    init {
        AMapLocationClient.updatePrivacyShow(context, true, true)
        AMapLocationClient.updatePrivacyAgree(context, true)
        val options = AMapLocationClientOption().apply {
            httpTimeOut = 3000
            isNeedAddress = true //详细地址
            isOnceLocation = true
            isOnceLocationLatest = true //获取最近3s内精度最高的"一次"定位结果
            locationMode = AMapLocationClientOption.AMapLocationMode.Hight_Accuracy
        }
        client = AMapLocationClient(context).apply { setLocationOption(options) }
    }


    override suspend fun getLocationResult(): Result<Location> { // 一次性监听
        return suspendCancellableCoroutine { cont ->
            val callback = AMapLocationListener { location ->
                location ?: return@AMapLocationListener
                cont.resume(location.asResult())
            }
            client.setLocationListener(callback)
            client.startLocation()
            cont.invokeOnCancellation {
                client.unRegisterLocationListener(callback)
            }
        }
    }

    private fun AMapLocation.asResult(): Result<Location> = when {
        errorCode != 0 -> Result.failure(Throwable("$errorCode $locationDetail"))
        city.isNullOrEmpty() -> Result.failure(Throwable("定位城市为空"))
        city == "Mountain View" -> Result.failure(Throwable("暂时不支持该城市☞ $city"))
        else -> Result.success(asAppLocationModel())
    }

    private fun AMapLocation.asAppLocationModel(): Location {
        return Location(
            cityName = city,
            latitude = latitude.toString(),
            longitude = longitude.toString(),
            district = district,
            address = address,
        )
    }


    /**
     * 一对多定位监听
     */
//	fun observerLocation(): Flow<AMapLocation> = callbackFlow {
//		val callback = AMapLocationListener { location ->
//			when(val result = location.status()) {
//				is LocationSuccess -> {
//					trySendBlocking(location)
//				}
//				is LocationError -> cancel(CancellationException(result.throwable.message))
//			}
//		}
//		client.setLocationListener(callback)
//		awaitClose { client.unRegisterLocationListener(callback) } // 当订阅者停止监听，利用挂起函数 "awaitClose" 来解除订阅
//	}


    /**
     * 一对多定位监听  Lambda
     */
//	suspend fun observeLocation(observer: suspend (Result<AMapLocation>) -> Unit) = coroutineScope {
//		val done = CompletableDeferred<Unit>()
//		val callback = object : AMapLocationListener {
//			val mutex = Mutex() //互斥锁，先到先获得锁
//			var observeJob: Job? = null
//			override fun onLocationChanged(location: AMapLocation) {
//				observeJob?.cancel()
//				observeJob = launch {
//					mutex.withLock {
//						//observer(location.status())
//					}
//				}
//			}
//		}
//		try {
//			client.setLocationListener(callback)
//			delay(3000L)
//			client.startLocation()//如果设置的定位时间间隔较长，请先添加监听再start, 不然有可能错过第一次回调
//			done.await()//确保每一个CallBack都能完成回调
//		} finally {
//			client.unRegisterLocationListener(callback)
//		}
//	}
}