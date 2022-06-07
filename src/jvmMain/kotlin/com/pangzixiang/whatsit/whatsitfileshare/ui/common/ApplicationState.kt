package com.pangzixiang.whatsit.whatsitfileshare.ui.common

import androidx.compose.runtime.*
import java.io.File

class ApplicationState {
    var isMainWindowOpen by mutableStateOf(true)
    private set

    var dialogInfo = mutableStateListOf<Any>("", "", false)
    private set

    var outputFileList = mutableStateListOf<File>()
    private set

    fun toggleWindowOpen() {
        isMainWindowOpen = !isMainWindowOpen
    }

    fun updateDialog(type:String, message:String) {
        dialogInfo[0] = type
        dialogInfo[1] = message
    }

    fun openDialog(isOpen: Boolean) {
        dialogInfo[2] = isOpen
    }

    fun addOutputFileList(file: File) {
        outputFileList.add(file)
        outputFileList = outputFileList.toSet().toMutableStateList()
    }

}
