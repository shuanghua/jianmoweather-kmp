import app.data.database.LocalDataSource

class TestRepo(private val weatherDb: LocalDataSource) {
    fun test() {
        println("Network: Hello,${weatherDb}")
    }
}