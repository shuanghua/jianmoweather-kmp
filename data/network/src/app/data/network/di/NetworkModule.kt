package app.data.network.di

import app.data.network.api.Api
import app.data.network.NetworkDataSource
import io.github.aakira.napier.DebugAntilog
import io.github.aakira.napier.Napier
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.util.*
import kotlinx.serialization.json.Json
import org.koin.dsl.module

val networkModule = module {
    single<NetworkDataSource> { NetworkDataSourceImpl(get()) }

    // ktor 使用文档 https://ktor.io/docs/client-requests.html#upload_file
    single<HttpClient> {
        HttpClient(CIO) {
            // 默认配置
            defaultRequest {
                url(Api.BASE_URL) // 和 Retrofit 类似，在这里设置 baseUrl
                headers.appendIfNameAbsent(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
            }

            // 打印日志
            install(Logging) {
                level = LogLevel.ALL
                logger = object : Logger {
                    override fun log(message: String) {
//                        Napier.e("Ktor Network:", null, message)
                        println("Ktor Network debug: $message")
                    }
                }
            }

            // 序列化配置
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true    // 忽略服务器下发的多余字段
                    explicitNulls = false       // 不序列化 null 值
                    prettyPrint = true  // 美化输出
                    isLenient = true // 容忍模式
                })
            }

            // 超时配置
            install(HttpTimeout) {
                socketTimeoutMillis = 10000
                connectTimeoutMillis = 10000
            }

        }.also { Napier.base(DebugAntilog()) }
    }
}