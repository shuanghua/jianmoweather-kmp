package app.data.network.model

import kotlinx.serialization.Serializable

@Serializable
data class Alarm(
    val icon: String = "",
    val name: String = "",
    val url: String = "",
)