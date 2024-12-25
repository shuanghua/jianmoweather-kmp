package app.data.model

data class WeatherScreenModel(
    val weather: Weather,
    val alarmIcons: List<AlarmIcon>,
    val oneDays: List<OneDay>,
    val oneHours: List<OneHour>,
    val conditions: List<Condition>,
    val exponents: List<Exponent>
)