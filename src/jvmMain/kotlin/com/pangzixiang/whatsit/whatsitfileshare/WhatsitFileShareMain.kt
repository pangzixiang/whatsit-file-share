package com.pangzixiang.whatsit.whatsitfileshare

import androidx.compose.runtime.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.*
import com.pangzixiang.whatsit.whatsitfileshare.ui.common.ApplicationState
import com.pangzixiang.whatsit.whatsitfileshare.ui.mainUI
import com.pangzixiang.whatsit.whatsitfileshare.utils.CacheUtils
import com.pangzixiang.whatsit.whatsitfileshare.vertx.VertxApplication
import com.typesafe.config.Config
import com.typesafe.config.ConfigFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

val configLoader: Config = ConfigFactory.load()

val logger: Logger = LoggerFactory.getLogger("Main-UI")

fun main() = application {
    val applicationState = remember {
        ApplicationState()
    }
    val trayState = rememberTrayState()

    if (applicationState.isMainWindowOpen) {
        Tray(
            state = trayState,
            icon = painterResource("assets/icon.png"),
            tooltip = "Whatsit File Share",
            menu = {
                Item(
                    "Exit",
                    onClick = {
                        applicationState.toggleWindowOpen()
                    }
                )
            }
        )

        LaunchedEffect(Unit) {
            CacheUtils.put("health", true)
            VertxApplication.start(applicationState)
        }

        mainUI(applicationState)
    } else {
        logger.info("Window is closed, starting to shutdown...")
    }

}
