package com.yulepanda.wargamebuilder

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun saveDatasheetCatalog(catalogName: String, catalog: DatasheetCatalog)
expect fun getDatasheetCatalogs(): List<DatasheetCatalog>
expect fun deleteDatasheetCatalog(catalogName: String)

expect fun logInfo(message: String)

expect fun formatDouble(num: Double): String