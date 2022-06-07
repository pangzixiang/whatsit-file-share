package com.pangzixiang.whatsit.whatsitfileshare.ui.component

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flight
import androidx.compose.material.icons.filled.FlutterDash
import androidx.compose.material.icons.filled.Loyalty
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberDialogState
import com.pangzixiang.whatsit.whatsitfileshare.configLoader
import com.pangzixiang.whatsit.whatsitfileshare.utils.QRUtils
import com.pangzixiang.whatsit.whatsitfileshare.utils.Utils
import java.net.URI

@Composable
@Preview
fun openQRCodeDialogButton() {
    var isDialogOpen by remember { mutableStateOf(false) }
    Column {
        IconButton(
            onClick = {isDialogOpen = true}
        ) {
            Icon(Icons.Filled.Loyalty, contentDescription = "Open QRCode")
        }

        if (isDialogOpen) {
            Dialog(
                title = "QR Code",
                onCloseRequest = { isDialogOpen = false},
                state = rememberDialogState(position = WindowPosition(Alignment.Center), size = DpSize(400.dp, 400.dp))
            ) {
                val localIP = URI.create("http://${Utils.getLocalIpAddress()}:${configLoader.getString("server.port")}").toString()
                Image(
                    QRUtils.generateQR(localIP),
                    contentDescription = "QR Code",
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
}
