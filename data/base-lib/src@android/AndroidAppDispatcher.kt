import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers


actual fun createAppDispatcher(): AppDispatcher = AndroidAppDispatcher()

class AndroidAppDispatcher : AppDispatcher {
    override val main: CoroutineDispatcher = Dispatchers.Main
    override val io: CoroutineDispatcher = Dispatchers.IO
    override val default: CoroutineDispatcher = Dispatchers.Default
}