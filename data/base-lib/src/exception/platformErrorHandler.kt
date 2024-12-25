package exception

interface ErrorHandler {
    fun handleError(error: AppException)
}

expect fun platformErrorHandler(): ErrorHandler