package com.yulepanda.wargamebuilder

import okio.FileSystem

fun saveDatasheet(path: String, datasheet: Datasheet) {
    platformSaveDatasheet(path, datasheet)
}