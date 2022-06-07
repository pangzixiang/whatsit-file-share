package com.pangzixiang.whatsit.whatsitfileshare

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.*
import com.pangzixiang.whatsit.whatsitfileshare.ui.app
import com.pangzixiang.whatsit.whatsitfileshare.ui.component.openQRCodeDialogButton
import com.pangzixiang.whatsit.whatsitfileshare.vertx.VertxApplication
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory

val configLoader: Config = ConfigFactory.load()

fun main() = application {
    var isOpen by remember { mutableStateOf(true) }
//    var expanded by remember { mutableStateOf(false) }
    val windowState = rememberWindowState(
        width = Dp(configLoader.getInt("application.width").toFloat()),
        height = Dp(configLoader.getInt("application.height").toFloat()),
        position = WindowPosition(Alignment.Center),
    )

    if (isOpen) {
        val trayState = rememberTrayState()
        VertxApplication.start()
        Tray(
            state = trayState,
            icon = painterResource("assets/icon.png"),
            tooltip = "Whatsit File Share",
            menu = {
                Item(
                    "Exit",
                    onClick = {
                        isOpen = false
                    }
                )
            }
        )

        Window(
//            onCloseRequest = ::exitApplication,
            onCloseRequest = { isOpen = false },
            title = configLoader.getString("application.name"),
            state = windowState,
            icon = painterResource("assets/icon.png"),
            undecorated = true,
            resizable = false,
            transparent = true,
        ) {
            Surface(
                color = MaterialTheme.colors.surface,
            ) {
                Column {
                    WindowDraggableArea {
                        TopAppBar(
                            title = {
//                                DropdownMenu(
//                                    expanded = expanded,
//                                    onDismissRequest = { expanded = false }
//                                ) {
//                                    DropdownMenuItem(onClick = { /* Handle refresh! */ }) {
//                                        Text("Refresh")
//                                    }
//                                    DropdownMenuItem(onClick = { /* Handle settings! */ }) {
//                                        Text("Settings")
//                                    }
//                                    Divider()
//                                    DropdownMenuItem(onClick = { /* Handle send feedback! */ }) {
//                                        Text("Send Feedback")
//                                    }
//                                }
                                Text(text = "Whatsit FileShare")
//                                IconButton(onClick = { expanded = true }) {
//                                    Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
//                                }
                            },
                            navigationIcon = {
                                openQRCodeDialogButton()
                            },
                            actions = {
                                IconButton(
                                    onClick = { windowState.isMinimized = true },
                                ) {
                                    Icon(Icons.Filled.CloseFullscreen, contentDescription = "Minimize")
                                }
                                if (windowState.placement == WindowPlacement.Maximized) {
                                    IconButton(
                                        onClick = { windowState.placement = WindowPlacement.Floating },
                                    ) {
                                        Icon(Icons.Filled.FullscreenExit, contentDescription = "CloseMaximize")
                                    }
                                } else {
                                    IconButton(
                                        onClick = { windowState.placement = WindowPlacement.Maximized },
                                    ) {
                                        Icon(Icons.Filled.Fullscreen, contentDescription = "Maximize")
                                    }
                                }
                                IconButton(
                                    onClick = { isOpen = false },
                                ) {
                                    Icon(Icons.Filled.Close, contentDescription = "Close")
                                }
                            }
                        )
                    }
                    app()
                }
            }
        }
    }

}
