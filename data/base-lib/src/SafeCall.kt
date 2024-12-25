import exception.AppException
import exception.AppLog
import exception.ErrorHandler
import kotlinx.io.IOException

suspend fun <T> safeCall(
    errorHandler: ErrorHandler? = null,
    block: suspend () -> T
): Result<T> = try {
    Result.success(block())
} catch (e: Exception) {
    val appException = when (e) {
        is IOException -> AppException.NetworkError("网络连接失败: ${e.message}")
        else -> AppException.UnexpectedError(e.message ?: "未知错误", e)
    }
    errorHandler?.handleError(appException)
    Result.failure(appException)
}

suspend fun <T> exceptionCall(
    tag: String? = null,
    block: suspend () -> T,
): T = try {
    block()
} catch (e: Exception) {
    val error = "${tag ?: ""} ${e.printStackTrace()}”)"
    AppLog.e(error)
    throw Exception(tag) // 传递给上层捕获，然后给 ui 显示，上层不需要关心细节
}