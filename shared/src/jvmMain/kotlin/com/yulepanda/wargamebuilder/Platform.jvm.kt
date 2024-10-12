package com.yulepanda.wargamebuilder

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import java.math.RoundingMode

const val APP_DATA_DIR = "Wargame"
const val CATALOGS_DIR = "Catalogs"

class JVMPlatform: Platform {
    override val name: String = "Java ${System.getProperty("java.version")}"
}

actual fun getPlatform(): Platform = JVMPlatform()

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