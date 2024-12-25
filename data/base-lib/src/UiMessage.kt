import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@OptIn(ExperimentalUuidApi::class)
fun getUuid(): Long = Uuid.random().toLongs { mostSignificantBits, _ -> mostSignificantBits}

data class UiMessage(
    val message: String,
    val id: Long = getUuid(),
)

fun UiMessage(
    t: Throwable,
    id: Long = getUuid(),
) = UiMessage(
    message = t.message.toString(),
    id = id
)

class UiMessageManager {
    private val mutex = Mutex()

    private val _messages = MutableStateFlow(emptyList<UiMessage>())
    val flow: Flow<UiMessage?> = _messages
        .map { it.firstOrNull() } // 如果集合为空,则返回null ,否则返回集合中第一个元素
        .distinctUntilChanged()//  后续的值如果和第一个值一样,则都将过滤掉(去掉重复)

    suspend fun emitMessage(message: UiMessage) {
        mutex.withLock {
            _messages.value += message
        }
    }

    suspend fun clearMessage(id: Long) {
        //filterNot: 当集合id和 传入id  "相等" 时,就删除该该集合 id 对应的元素
        //filter: 当集合id和 传入id  "相等"  时, 只保留该集合id对于的元素  , 只保留该 id ,丢弃别的 id
        mutex.withLock {
            _messages.value = _messages.value.filterNot { it.id == id }
        }
    }
}