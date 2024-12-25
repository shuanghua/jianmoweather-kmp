import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


actual fun createAppDispatcher(): AppDispatcher = IOSAppDispatcher()

class IOSAppDispatcher : AppDispatcher {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.Default    // iOS 没有 IO Dispatcher，使用 Default
    override val default: CoroutineDispatcher = Dispatchers.Default
}