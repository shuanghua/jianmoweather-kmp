package exception

sealed class AppException : Exception() {
    data class NetworkError(
        override val message: String,
        val code: Int = -1
    ) : AppException()

    data class LocationError(
        override val message: String,
        val code: Int = -1
    ) : AppException()

    data class BusinessError(
        override val message: String,
        val code: Int
    ) : AppException()

    data class UnexpectedError(
        override val message: String,
        val throwable: Throwable? = null
    ) : AppException()

    data class DatabaseException(
        override val message: String,
        val code: Int = -1
    ) : AppException()
}