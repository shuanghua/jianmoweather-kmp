package exception

interface ModuleException {
    suspend fun <T> catchCall(block: suspend () -> T): T
}