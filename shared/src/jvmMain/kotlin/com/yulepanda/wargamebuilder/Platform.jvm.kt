package com.yulepanda.wargamebuilder

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path.Companion.toPath

const val APP_DATA_DIR = "Wargame"
const val SHEETS_DIR = "Sheets"

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

actual fun platformSaveDatasheet(fileName: String, datasheet: Datasheet) {
    val userDir = System.getProperty("user.home")
    val directories = "${userDir}/AppData/Local/${APP_DATA_DIR}/${SHEETS_DIR}"

    FileSystem.SYSTEM.createDirectories(directories.toPath())

    val fullPath = "${directories}/${fileName}"

    FileSystem.SYSTEM.write(fullPath.toPath()) {
        writeUtf8(Json.encodeToString(datasheet))
    }
}