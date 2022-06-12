package com.pangzixiang.whatsit.whatsitfileshare.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.CloseFullscreen
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.*
import com.pangzixiang.whatsit.whatsitfileshare.configLoader
import com.pangzixiang.whatsit.whatsitfileshare.logger
import com.pangzixiang.whatsit.whatsitfileshare.ui.common.ApplicationState
import com.pangzixiang.whatsit.whatsitfileshare.ui.component.FileDialog
import com.pangzixiang.whatsit.whatsitfileshare.ui.component.app
import com.pangzixiang.whatsit.whatsitfileshare.ui.component.openQRCodeDialogButton
import com.pangzixiang.whatsit.whatsitfileshare.utils.CacheUtils
import io.vertx.core.http.ServerWebSocket
import io.vertx.core.json.JsonObject
import java.io.File

@Composable
fun mainUI(applicationState: ApplicationState) {
    val windowState = rememberWindowState(
        width = Dp(configLoader.getInt("application.width").toFloat()),
        height = Dp(configLoader.getInt("application.height").toFloat()),
        position = WindowPosition(Alignment.Center),
    )
    if (applicationState.isMainWindowOpen) {
        Window(
//            onCloseRequest = ::exitApplication,
            onCloseRequest = { applicationState.toggleWindowOpen() },
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
                val wsClientList = CacheUtils.get("wsClient")
                if (applicationState.isFileDialogOpen) {
                    if (wsClientList != null && (wsClientList as ArrayList<ServerWebSocket>).isNotEmpty()) {
                        FileDialog(title = "Select File", isLoad = true) {
                            if (it != null) {
                                val file = File(it.toUri())
                                logger.info("Selected File [${file.path}]")
                                applicationState.addDownloadFileList(file)
                                CacheUtils.put("downloadFileList", applicationState.downloadFileList)

                                wsClientList.forEach { ws ->
                                    val data = JsonObject()
                                    data.put("fileName", file.name)
                                    data.put("fileSize", file.length())
                                    data.put("fileLastModified", file.lastModified())
                                    ws.writeTextMessage(data.encode())
                                    logger.info("Sent message [${data}] to [${ws.binaryHandlerID()}]")
                                }
                            }
                            applicationState.toggleFileDialogOpen()
                        }
                    } else {
                        applicationState.updateDialog("WARNING", "No Client Connected!")
                        applicationState.openDialog(true)
                    }
                }
                Column {
                    WindowDraggableArea {
                        TopAppBar(
                            title = {
                                Text(text = "Whatsit FileShare")
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
                                    onClick = { applicationState.toggleWindowOpen() },
                                ) {
                                    Icon(Icons.Filled.Close, contentDescription = "Close")
                                }
                            }
                        )
                    }
                    app(applicationState)
                }
            }
        }
    }
}
