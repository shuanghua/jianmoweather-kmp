import kotlinx.coroutines.CoroutineDispatcher

interface AppDispatcher {
    val main: CoroutineDispatcher
    val io: CoroutineDispatcher
    val default: CoroutineDispatcher
}