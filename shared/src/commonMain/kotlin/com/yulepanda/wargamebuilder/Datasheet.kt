package com.yulepanda.wargamebuilder

import kotlinx.serialization.Serializable

@Serializable
data class Datasheet(
    var name: String = "New Datasheet",
    var skill: Int = 0,
    var health: Int = 0,
    var speed: Int = 0,
    var modelCount: Int = 0,
    var abilities: Array<UnitAbility> = emptyArray(),
    var statTable: RollTable = RollTable(),
    var tags: Array<String> = emptyArray()
)

@Serializable
data class UnitAbility(
    var name: String = "",
    var description: String? = null
)

@Serializable
data class RollTable(
    var resultBreaks: Array<Int> = arrayOf(1),
    var toSave: Array<Int> = arrayOf(4),
    var toResist: Array<Int?> = arrayOf(null),
    var hardness: Array<Int?> = arrayOf(null),
    var enhancements: Array<String?> = arrayOf(null),
    var weapons: Array<WeaponTable> = arrayOf(WeaponTable())
)

@Serializable
data class WeaponTable(
    var name: String = "New Weapon",
    var attacks: Array<Int> = arrayOf(1),
    var range: Array<Int> = arrayOf(3),
    var toHit: Array<Int> = arrayOf(4),
    var damage: Array<Int> = arrayOf(1),
    var enhancements: Array<String?> = arrayOf(null)
)
