package exception


actual fun platformErrorHandler(): ErrorHandler = IOSAppExceptionHandler()

class IOSAppExceptionHandler : ErrorHandler {
    override fun handleError(error: AppException) {

    }
}