package com.yulepanda.wargamebuilder

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform