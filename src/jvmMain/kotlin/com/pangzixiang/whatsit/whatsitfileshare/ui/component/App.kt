package com.pangzixiang.whatsit.whatsitfileshare.ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.pangzixiang.whatsit.whatsitfileshare.ui.common.ApplicationState
import com.pangzixiang.whatsit.whatsitfileshare.ui.page.home
import com.pangzixiang.whatsit.whatsitfileshare.ui.page.settings
import java.util.*


@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun app(applicationState: ApplicationState) {
    var selectedItem by remember { mutableStateOf(0) }
    val stateVertical = rememberScrollState(0)
    val menus = TreeMap<String, ImageVector>()
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
                Column(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.padding(PaddingValues(5.dp))) {
                    Box(modifier = Modifier.verticalScroll(stateVertical)) {
                        Column(verticalArrangement = Arrangement.spacedBy(5.dp), modifier = Modifier.padding(PaddingValues(5.dp))) {
                            when(selectedItem) {
                                0 -> home(applicationState)
                                1 -> settings()
                                else -> home(applicationState)
                            }
                        }
                    }
                    VerticalScrollbar(
                        adapter = rememberScrollbarAdapter(stateVertical)
                    )
                    if (applicationState.dialogInfo[2] as Boolean) {
                        AlertDialog(
                            onDismissRequest = {
//                                applicationState.toggleErrorDialog("")
                            },
                            title = {
                                Text(text = applicationState.dialogInfo[0] as String, color = Color.Red)
                            },
                            text = {
                                Text(applicationState.dialogInfo[1] as String)
                            },
                            confirmButton = {
                                TextButton(
                                    onClick = {
                                        applicationState.openDialog(false)
                                    }
                                ) {
                                    Text("Confirm")
                                }
                            },
                            dismissButton = {
//                                TextButton(
//                                    onClick = {
//                                        applicationState.toggleErrorDialog()
//                                    }
//                                ) {
//                                    Text("Dismiss")
//                                }
                            },
                            modifier = Modifier.size(400.dp, 200.dp)
                        )
                    }
                }
            }
        }
    }
}
