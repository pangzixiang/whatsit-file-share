import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "com.pangzixiang.whatsit"
version = "1.0.0"

val vertxVersion = "4.3.1"
val typesafeConfigVersion = "1.4.2"
val log4jVersion = "2.17.2"
val slf4jVersion = "1.7.36"
val zxingVersion = "3.5.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

dependencies {
    // vertx dependencies
    commonMainImplementation(platform("io.vertx:vertx-stack-depchain:$vertxVersion"))
//    commonMainImplementation("io.vertx:vertx-web-client")
//    commonMainImplementation("io.vertx:vertx-web-templ-thymeleaf")
    commonMainImplementation("io.vertx:vertx-web")
    commonMainImplementation("io.vertx:vertx-lang-kotlin")
    commonMainImplementation(kotlin("stdlib-jdk8"))

    // log4j
    commonMainImplementation("org.apache.logging.log4j:log4j-api:$log4jVersion")
    commonMainImplementation("org.apache.logging.log4j:log4j-core:$log4jVersion")
    commonMainImplementation("org.apache.logging.log4j:log4j-slf4j-impl:$log4jVersion")
    commonMainImplementation("org.slf4j:slf4j-api:$slf4jVersion")

    // QR code
    commonMainImplementation("com.google.zxing:core:$zxingVersion")
    commonMainImplementation("com.google.zxing:javase:$zxingVersion")

    // config
    commonMainImplementation("com.typesafe:config:$typesafeConfigVersion")

    //compose
    commonMainImplementation("org.jetbrains.compose.material:material-icons-extended-desktop:1.1.0")

    // Cache
    commonMainImplementation("com.github.ben-manes.caffeine:caffeine:3.1.1")
//    commonMainImplementation("com.github.ben-manes.caffeine:guava:3.1.1")
//    commonMainImplementation("com.github.ben-manes.caffeine:jcache:3.1.1")
}

kotlin {
    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "17"
        }
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "$group.${rootProject.name.replace("-", "")}.WhatsitFileShareMainKt"
        jvmArgs += listOf("-Xmx2G", "-XX:+UseZGC", "-Xlog:gc:stdout:time")
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "whatsit-file-share"
            packageVersion = version.toString()
            includeAllModules = true
            windows {
                iconFile.set(project.file("icon.ico"))
            }
        }
    }
}
