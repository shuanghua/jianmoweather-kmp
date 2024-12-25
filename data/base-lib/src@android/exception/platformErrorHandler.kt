package exception

import android.content.Context
import io.github.aakira.napier.Napier
import org.koin.java.KoinJavaComponent.inject

actual fun platformErrorHandler(): ErrorHandler = AndroidAppExceptionHandler()

class AndroidAppExceptionHandler : ErrorHandler {
    private val context: Context by inject(Context::class.java)
    override fun handleError(error: AppException) {
        when (error) {
            is AppException.NetworkError -> {
                Napier.e(error.message)
            }

            is AppException.LocationError -> {
                Napier.e(error.message)
            }

            is AppException.DatabaseException -> {
                Napier.e(error.message)
            }

            is AppException.BusinessError -> {
                Napier.e(error.message)
            }

            is AppException.UnexpectedError -> {
                Napier.e(error.message)
            }
        }
    }
}