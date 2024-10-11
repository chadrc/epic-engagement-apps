package com.yulepanda.wargamebuilder

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import java.math.RoundingMode

const val APP_DATA_DIR = "Wargame"
const val SHEETS_DIR = "Sheets"
const val CATALOGS_DIR = "Catalogs"

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

actual fun getDatasheets(): List<Datasheet> {
    val userDir = System.getProperty("user.home")
    val directories = "${userDir}/AppData/Local/${APP_DATA_DIR}/${SHEETS_DIR}"

    val paths = FileSystem.SYSTEM.list(directories.toPath())

    return paths.stream().map {
        Json.decodeFromString<Datasheet>(FileSystem.SYSTEM.read(it) {
            readUtf8()
        })
    }.toList()
}

actual fun deleteDatasheetFile(fileName: String) {
    val userDir = System.getProperty("user.home")
    val filePath = "${userDir}/AppData/Local/${APP_DATA_DIR}/${SHEETS_DIR}/${fileName}".toPath()

    if (FileSystem.SYSTEM.exists(filePath)) {
        FileSystem.SYSTEM.delete(filePath)
    }
}

actual fun logInfo(message: String) {
    println(message)
}

actual fun formatDouble(num: Double): String {
    return num.toBigDecimal().setScale(4, RoundingMode.HALF_UP).toDouble().toString()
}

actual fun deleteDatasheetCatalog(catalogName: String) {
    val filePath = makeDataPath() / CATALOGS_DIR / "${catalogName}.json"
    if (FileSystem.SYSTEM.exists(filePath)) {
        FileSystem.SYSTEM.delete(filePath)
    }
}

actual fun getDatasheetCatalogs(): List<DatasheetCatalog> {
    val directories = makeDataPath() / CATALOGS_DIR

    if (!FileSystem.SYSTEM.exists(directories)) {
        return emptyList()
    }

    val paths = FileSystem.SYSTEM.list(directories)

    return paths.stream().map {
        Json.decodeFromString<DatasheetCatalog>(FileSystem.SYSTEM.read(it) {
            readUtf8()
        })
    }.toList()
}

actual fun saveDatasheetCatalog(catalogName: String, catalog: DatasheetCatalog) {
    val directories = makeDataPath() / CATALOGS_DIR

    FileSystem.SYSTEM.createDirectories(directories)

    FileSystem.SYSTEM.write(directories / "${catalogName}.json") {
        writeUtf8(Json.encodeToString(catalog))
    }
}

fun makeDataPath(): Path {
    val userDir = System.getProperty("user.home")
    return "${userDir}/AppData/Local/${APP_DATA_DIR}".toPath()
}