package com.yulepanda.wargamebuilder

import kotlinx.serialization.Serializable

@Serializable
data class DatasheetCatalog(
    var name: String,
    var datasheets: MutableList<Datasheet> = mutableListOf(Datasheet())
)

@Serializable
data class Datasheet(
    var name: String = "New Datasheet",
    var skill: Int = 0,
    var health: Int = 1,
    var speed: Int = 5,
    var modelCount: Int = 5,
    var abilities: String = "",
    var statTable: RollTable = RollTable(),
    var tags: Array<String> = emptyArray()
)

@Serializable
data class RollTable(
    var resultBreaks: Array<Int> = arrayOf(1, 5, 10, 15),
    var toSave: Array<Int> = arrayOf(4, 4, 4, 4),
    var toResist: Array<Int?> = arrayOf(null, null, null, null),
    var hardness: Array<Int?> = arrayOf(null, null, null, null),
    var enhancements: Array<String?> = arrayOf(null, null, null, null),
    var weapons: Array<WeaponTable> = arrayOf(
        WeaponTable(name = "Melee Weapon", range = arrayOf(0, 0, 0, 0)),
        WeaponTable()
    )
)

@Serializable
data class WeaponTable(
    var name: String = "Ranged Weapon",
    var attacks: Array<Int> = arrayOf(1, 1, 1, 1),
    var range: Array<Int> = arrayOf(10, 10, 10, 10),
    var toHit: Array<Int> = arrayOf(4, 4, 4, 4),
    var damage: Array<Int> = arrayOf(1, 1, 1, 1),
    var enhancements: Array<String?> = arrayOf(null, null, null, null)
)
