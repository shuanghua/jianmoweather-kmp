package preview

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import app.data.model.previewWeather
import ui.components.*

@Preview
@Composable
internal fun PreviewMainTemperature() {
    MainTemperature(
        weather = previewWeather
    )
}

@Preview
@Composable
internal fun PreviewOneItem() {
    Surface(tonalElevation = 2.dp) {
        LazyRow {
            repeat(10) {
                item {
                    OneItem(
                        topText = "明日",
                        centerText = "1/24",
                        bottomText = "20°~26°",
                    )
                }
            }
        }
    }
}

@Preview
@Composable
internal fun PreviewDayItem() {
    Surface(tonalElevation = 2.dp) {
        LazyRow {
            repeat(10) {
                item {
                    DayItem(
                        topText = "明日",
                        centerIconUrl = "",
                        bottomText = "20°~26°",
                    )
                }
            }
        }
    }
}


@Preview
@Composable
internal fun PreviewExponentItem() {
    ExponentItem(
        title = "舒适度指数",
        levelDesc = "舒服"
    )
}

@Composable
internal fun PreViewTextItem(
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier.fillMaxSize()) {
        repeat(20) {
            item {
                AppTextItem(
                    text = "北京",
                    onClick = {}
                )
            }
        }
    }
}