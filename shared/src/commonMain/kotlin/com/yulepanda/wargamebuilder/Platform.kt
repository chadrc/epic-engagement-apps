package com.yulepanda.wargamebuilder

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform

expect fun platformSaveDatasheet(fileName: String, datasheet: Datasheet)