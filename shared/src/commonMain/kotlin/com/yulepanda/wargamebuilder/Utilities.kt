package com.yulepanda.wargamebuilder

fun saveDatasheet(path: String, datasheet: Datasheet) {
    platformSaveDatasheet(path, datasheet)
}

inline fun <reified T> Array<T>.pluck(index: Int): Array<T> {
    val new = this.toMutableList()
    new.removeAt(index)
    return new.toTypedArray()
}

inline fun <reified T> Iterable<T>.findIndex(predicate: (T) -> Boolean): Int? {
    return this.mapIndexed {i, v -> Pair(i, v)}
        .find { pair -> predicate(pair.second) }
        ?.first
}