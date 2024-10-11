package com.yulepanda.wargamebuilder

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()
actual fun platformSaveDatasheet(fileName: String, datasheet: Datasheet) {
}

actual fun getDatasheets(): List<Datasheet> {
    TODO("Not yet implemented")
}

actual fun deleteDatasheetFile(fileName: String) {
}

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