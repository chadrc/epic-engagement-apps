package com.yulepanda.wargamebuilder

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()
actual fun platformSaveDatasheet(fileName: String, datasheet: Datasheet) {
}

actual fun getDatasheets(): List<Datasheet> {
    TODO("Not yet implemented")
}

actual fun deleteDatasheetFile(fileName: String) {
}

actual fun logInfo(message: String) {
}

actual fun formatDouble(value: String, vararg num: Double): String {
}

actual fun formatDouble(num: Double): String {
    TODO("Not yet implemented")
}