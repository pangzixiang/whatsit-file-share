package com.pangzixiang.whatsit.whatsitfileshare.ui.page

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import com.pangzixiang.whatsit.whatsitfileshare.ui.common.ApplicationState
import com.pangzixiang.whatsit.whatsitfileshare.utils.Utils
import java.io.File

@Composable
@Preview
fun settings(applicationState: ApplicationState) {
    Column {
        Button(onClick = {
            applicationState.outputFileList.clear()
            Utils.deleteDir(File("./outputFile"))
        }) {
            Text("houseKeep")
        }
    }
}
