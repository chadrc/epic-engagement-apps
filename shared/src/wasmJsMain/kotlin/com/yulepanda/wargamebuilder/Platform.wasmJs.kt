package com.yulepanda.wargamebuilder

class WasmPlatform: Platform {
    override val name: String = "Web with Kotlin/Wasm"
}

actual fun getPlatform(): Platform = WasmPlatform()
actual fun platformSaveDatasheet(fileName: String, datasheet: Datasheet) {
}