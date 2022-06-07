package com.pangzixiang.whatsit.whatsitfileshare.ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.pangzixiang.whatsit.whatsitfileshare.ui.page.home
import com.pangzixiang.whatsit.whatsitfileshare.ui.page.settings

@Composable
@Preview
fun app() {
    var selectedItem by remember { mutableStateOf(0) }
    val menus = HashMap<String, ImageVector>()
    menus["home"] = Icons.Filled.Home
    menus["settings"] = Icons.Filled.Settings
    MaterialTheme {
        Box(modifier = Modifier.fillMaxSize()) {
            Row {
                NavigationRail {
                    menus.onEachIndexed { index, entry ->
                        NavigationRailItem(
                            icon = { Icon(entry.value, contentDescription = entry.key) },
                            label = { Text(entry.key) },
                            selected = selectedItem == index,
                            onClick = { selectedItem = index }
                        )
                    }
                }
                Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.padding(PaddingValues(10.dp))) {
                    when(selectedItem) {
                        0 -> home()
                        1 -> settings()
                        else -> home()
                    }
                }
            }
        }
    }
}
