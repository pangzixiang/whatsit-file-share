package com.pangzixiang.whatsit.whatsitfileshare.ui.page

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.pangzixiang.whatsit.whatsitfileshare.logger
import com.pangzixiang.whatsit.whatsitfileshare.ui.common.ApplicationState
import com.pangzixiang.whatsit.whatsitfileshare.utils.Utils
import java.io.File
import javax.swing.JFileChooser

@Composable
@Preview
fun home(applicationState: ApplicationState) {
    val fileList = applicationState.outputFileList
    if (fileList.size == 0) {
        Text("Nothing!")
    } else {
            fileList.forEach {
                Card(
                    backgroundColor = Color.DarkGray,
                    contentColor = Color.White,
                    elevation = 1.dp,
                    modifier = Modifier.fillMaxWidth().height(80.dp),
                ) {
                    Column {
                        Text(it.name)
                        Button(onClick = {
                            saveFileToPath(applicationState, it)
                        }) {
                            Text("save")
                        }
                    }
                }
            }
    }
    Button(onClick = {
        applicationState.outputFileList.clear()
        Utils.deleteDir(File("./outputFile"))
    }) {
        Text("houseKeep")
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


