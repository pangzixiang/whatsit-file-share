package com.pangzixiang.whatsit.whatsitfileshare.ui.page

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FileDownload
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import com.pangzixiang.whatsit.whatsitfileshare.ui.common.ApplicationState
import com.pangzixiang.whatsit.whatsitfileshare.utils.CacheUtils
import io.vertx.core.http.ServerWebSocket
import java.util.*

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun download(applicationState: ApplicationState) {
    val fileList = applicationState.downloadFileList
    Column {
        if (fileList.size != 0) {
            fileList.forEach {
                ListItem(
                    text = { Text(it.name) },
                    icon = {
                        Icon(Icons.Filled.FileDownload, contentDescription = it.name)
                    },
                    secondaryText = { Text("${(it.length() / 1024 / 1024)} MB") },
                    overlineText = { Text("${Date(it.lastModified())}") },
                )
                Divider()
            }
        }
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Button(
                onClick = {
                    val wsClientList = CacheUtils.get("wsClient")
                    if (wsClientList != null && (wsClientList as ArrayList<ServerWebSocket>).isNotEmpty()) {
                        applicationState.toggleFileDialogOpen()
                    } else {
                        applicationState.updateDialog("WARNING", "No Client Connected!")
                        applicationState.openDialog(true)
                    }
                }
            ) {
                Text("Select File")
            }
            if (fileList.size != 0) {
                Button(
                    onClick = {
                        applicationState.downloadFileList.clear()
                        CacheUtils.delete("downloadFileList")
                    }
                ) {
                    Text("Delete All")
                }
            }
        }
    }
}
