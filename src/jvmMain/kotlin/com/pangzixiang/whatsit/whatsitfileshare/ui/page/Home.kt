package com.pangzixiang.whatsit.whatsitfileshare.ui.page

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.pangzixiang.whatsit.whatsitfileshare.configLoader
import com.pangzixiang.whatsit.whatsitfileshare.logger
import com.pangzixiang.whatsit.whatsitfileshare.ui.common.ApplicationState
import com.pangzixiang.whatsit.whatsitfileshare.utils.QRUtils
import com.pangzixiang.whatsit.whatsitfileshare.utils.Utils
import java.io.File
import java.net.URI
import java.util.Date
import javax.swing.JFileChooser

@OptIn(ExperimentalMaterialApi::class)
@Composable
@Preview
fun home(applicationState: ApplicationState) {
    val fileList = applicationState.outputFileList
    if (fileList.size != 0) {
        fileList.forEach {
            ListItem(
                text = { Text(it.name) },
                icon = {
                    Icon(Icons.Filled.AttachFile, contentDescription = it.name)
                },
                secondaryText = { Text("${(it.length() / 1024 / 1024)} MB") },
                overlineText = { Text("${Date(it.lastModified())}") },
                trailing = {
                    Button(onClick = {
                        saveFileToPath(applicationState, it)
                    }) {
                        Text("save")
                    }
                }
            )
            Divider()
        }
        Button(onClick = {
            refreshList(applicationState)
        }) {
            Text("Refresh")
        }
    } else {
        Box(contentAlignment = Alignment.Center) {
            val localIP = URI.create("http://${Utils.getLocalIpAddress()}:${configLoader.getString("server.port")}").toString()
            Image(
                QRUtils.generateQR(localIP),
                contentDescription = "QR Code",
                modifier = Modifier.fillMaxSize(),
            )
        }
    }

}

fun saveFileToPath(applicationState: ApplicationState, file: File) {
    val jFileChooser = JFileChooser()
    jFileChooser.dialogTitle = "choose a folder"
    jFileChooser.fileSelectionMode = JFileChooser.DIRECTORIES_ONLY
    jFileChooser.showOpenDialog(null)
    if (jFileChooser.selectedFile == null) {
        logger.error("No folder selected, hence skip to save file...")
        applicationState.updateDialog("ERROR", "No Folder selected!")
    } else {
        logger.info("Saved ${file.name} to ${jFileChooser.selectedFile.absolutePath}")
        file.copyTo(File("${jFileChooser.selectedFile.absolutePath}/${file.name}"))
        applicationState.updateDialog("SUCCESS", "Saved!")
    }
    applicationState.openDialog(true)
}

fun refreshList(applicationState: ApplicationState) {
    applicationState.outputFileList.clear()
    val folder = File("./outputFile")
    folder.listFiles()?.forEach {
        applicationState.outputFileList.add(it)
    }
}


