package com.yulepanda.wargamebuilder

import android.os.Build

class AndroidPlatform : Platform {
    override val name: String = "Android ${Build.VERSION.SDK_INT}"
}

actual fun getPlatform(): Platform = AndroidPlatform()

actual fun logInfo(message: String) {
}

actual fun formatDouble(num: Double): String {
    TODO("Not yet implemented")
}

actual fun deleteDatasheetCatalog(catalogName: String) {
}

actual fun getDatasheetCatalogs(): List<DatasheetCatalog> {
    TODO("Not yet implemented")
}

actual fun saveDatasheetCatalog(catalogName: String, catalog: DatasheetCatalog) {
}