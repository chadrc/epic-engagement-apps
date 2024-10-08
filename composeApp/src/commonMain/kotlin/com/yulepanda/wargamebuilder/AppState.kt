package com.yulepanda.wargamebuilder

class AppState(
    val datasheets: Array<Datasheet> = arrayOf(Datasheet())
) {
    var selectedSheet = 0
}