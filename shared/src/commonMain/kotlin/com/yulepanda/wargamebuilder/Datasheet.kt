package com.yulepanda.wargamebuilder

class Datasheet {
    var name = "New Datasheet"
    var skill = 0
    var health = 0
    var speed = 0
    var composition: Array<ModelGroup> = arrayOf(ModelGroup() )
    var abilities: Array<UnitAbility> = emptyArray()
    var statTable: RollTable = RollTable()
    var tags: Array<String> = emptyArray()
}

class UnitAbility {
    var name = ""
    var description: String? = null
}

class ModelGroup {
    var name = "Model Name"
    var count = 0
}

class RollTable {
    var resultBreaks: Array<Int> = arrayOf(1)
    var toSave: Array<Int> = arrayOf(4)
    var toResist: Array<Int>? = null
    var hardness: Array<Int>? = null
    var weapons: Array<WeaponTable> = arrayOf(WeaponTable())
}

class WeaponTable {
    var name = ""
    var attacks: Array<Int> = arrayOf(1)
    var range: Array<Int> = arrayOf(3)
    var toHit: Array<Int> = arrayOf(4)
    var damage: Array<Int> = arrayOf(1)
    var enhancements: Array<String>? = null
}
