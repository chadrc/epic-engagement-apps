package com.yulepanda.wargamebuilder

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()
actual fun logInfo(message: String) {
}


actual fun formatDouble(num: Double): String {
    return "Not implemented"
}

actual fun deleteDatasheetCatalog(catalogName: String) {
}

actual fun getDatasheetCatalogs(): List<DatasheetCatalog> {
    TODO("Not yet implemented")
}

actual fun saveDatasheetCatalog(catalogName: String, catalog: DatasheetCatalog) {
}