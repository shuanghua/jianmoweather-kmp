package exception

import io.github.aakira.napier.Napier

data class AppMessage(val uiMessage: String, val error: String)

object AppLog {
    fun i(log: String) = Napier.i(log)
    fun d(log: String) = Napier.d(log)
    fun e(log: String) = Napier.e(log)
}