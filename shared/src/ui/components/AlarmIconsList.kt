package ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import app.data.model.AlarmIcon
import ui.components.AlarmIconsItem

@Composable
fun AlarmIconList(
    alarms: List<AlarmIcon>,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        for (alarm in alarms) {
            AlarmIconsItem(iconUrl = alarm.iconUrl)
        }
    }
    Spacer(modifier = modifier.height(24.dp))
}