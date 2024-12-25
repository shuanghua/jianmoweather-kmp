package ui.components

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.data.model.Condition

@Composable
fun ConditionList(
    conditions: List<Condition>,
    modifier: Modifier = Modifier,
) {
    Spacer(modifier = modifier.height(16.dp))
    LazyRow {
        items(
            items = conditions,
            key = { it.name }
        ) {
            ConditionItem(name = it.name, value = it.value)
        }
    }
}