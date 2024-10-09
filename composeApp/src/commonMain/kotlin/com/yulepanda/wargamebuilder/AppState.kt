package com.yulepanda.wargamebuilder

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AppState(
    val datasheets: List<Datasheet> = listOf(Datasheet()),
    val selectedSheet: Int = 0,
    val newSheetName: String = "",
    val sheetEdits: Map<String, Pair<Boolean, Datasheet>> = mapOf()
)

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppState())
    val uiState: StateFlow<AppState> = _uiState.asStateFlow()

    fun addNewSheet() {
        if (uiState.value.newSheetName.isBlank()) {
            // error
            return
        }
        val existing = uiState.value.datasheets.find { it.name == uiState.value.newSheetName }

        if (existing == null) {
            _uiState.update { s ->
                val sheets = s.datasheets.toMutableList()
                val datasheet = Datasheet()
                datasheet.name = uiState.value.newSheetName

                val path = "${datasheet.name}.json"
                saveDatasheet(path, datasheet)

                sheets.add(datasheet)
                s.copy(
                    datasheets = sheets,
                    newSheetName = "",
                    selectedSheet = sheets.size - 1
                )
            }
        } else {
            // error
        }
    }

    fun loadAllSheets() {
        _uiState.update { state ->
            var sheets = getDatasheets()
            if (sheets.isEmpty()) {
                sheets = listOf(Datasheet())
            }
            state.copy(datasheets = sheets)
        }

        setSelected(uiState.value.selectedSheet)
    }

    fun setNewSheetName(text: String) {
        _uiState.update { state ->
            state.copy(newSheetName = text)
        }
    }

    fun deleteSelectedSheet() {
        if (_uiState.value.selectedSheet < _uiState.value.datasheets.size) {
            _uiState.update { state ->
                val sheets = state.datasheets.toMutableList()
                val removed = sheets.removeAt(_uiState.value.selectedSheet)

                val selected = if (_uiState.value.selectedSheet >= sheets.size) {
                    sheets.size - 1
                } else {
                    _uiState.value.selectedSheet
                }

                val path = "${removed.name}.json"
                deleteDatasheetFile(path)

                state.copy(datasheets = sheets, selectedSheet = selected)
            }
        }
    }

    fun saveSelectedSheet() {
        if (_uiState.value.selectedSheet < _uiState.value.datasheets.size) {
            _uiState.update { state ->
                val sheets = state.datasheets.toMutableList()
                val edits = state.sheetEdits.toMutableMap()
                val selectedSheet = state.datasheets[state.selectedSheet]
                val oldName = selectedSheet.name
                val editSheet = edits[selectedSheet.name]?.second?.copy() ?: return
                sheets[state.selectedSheet] = editSheet

                // delete first in case of name change
                deleteDatasheetFile("${oldName}.json")
                saveDatasheet("${editSheet.name}.json", editSheet)

                edits.remove(oldName)
                edits[editSheet.name] = Pair(false, sheets[state.selectedSheet])

                state.copy(datasheets = sheets, sheetEdits = edits)
            }
        }
    }

    fun setSelected(index: Int) {
        _uiState.update { state ->
            val edits = state.sheetEdits.toMutableMap()
            val selectedSheet = state.datasheets[index]
            if (!edits.containsKey(selectedSheet.name)) {
                edits[selectedSheet.name] = Pair(false, selectedSheet.copy())
            }

            state.copy(selectedSheet = index, sheetEdits = edits)
        }
    }

    fun editName(name: String) {
        editSheetValue { it.name = name }
    }

    fun editSkill(skill: Int?) {
        if (skill == null) {
            // show error
        } else {
            editSheetValue { it.skill = skill }
        }
    }

    fun editHealth(health: Int?) {
        if (health == null) {
            // show error
        } else {
            editSheetValue { it.health = health }
        }
    }

    fun editSpeed(value: Int?) {
        if (value == null) {
            // show error
        } else {
            editSheetValue { it.speed = value }
        }
    }

    fun setResultBreak(value: Int, index: Int) {
        editSheetValue {
            it.statTable.resultBreaks[index] = value
        }
    }

    fun setToSave(value: Int, index: Int) {
        editSheetValue {
            it.statTable.toSave[index] = value
        }
    }

    fun setToResist(value: Int, index: Int) {
        editSheetValue {
            it.statTable.toResist[index] = value
        }
    }

    fun setHardness(value: Int, index: Int) {
        editSheetValue {
            it.statTable.hardness[index] = value
        }
    }

    fun setEnhancements(value: String, index: Int) {
        editSheetValue {
            it.statTable.enhancements[index] = value
        }
    }

    fun setWeaponName(value: String, weaponIndex: Int) {
        editSheetValue {
            it.statTable.weapons[weaponIndex].name = value
        }
    }

    fun setAttacks(value: Int, index: Int, weaponIndex: Int) {
        editSheetValue {
            it.statTable.weapons[weaponIndex].attacks[index] = value
        }
    }

    fun setRange(value: Int, index: Int, weaponIndex: Int) {
        editSheetValue {
            it.statTable.weapons[weaponIndex].range[index] = value
        }
    }

    fun setToHit(value: Int, index: Int, weaponIndex: Int) {
        editSheetValue {
            it.statTable.weapons[weaponIndex].toHit[index] = value
        }
    }

    fun setDamage(value: Int, index: Int, weaponIndex: Int) {
        editSheetValue {
            it.statTable.weapons[weaponIndex].damage[index] = value
        }
    }

    fun setWeaponEnhancements(value: String, index: Int, weaponIndex: Int) {
        editSheetValue {
            it.statTable.weapons[weaponIndex].enhancements[index] = value
        }
    }

    private fun editSheetValue(editFunc: (Datasheet) -> Unit) {
        _uiState.update { state ->
            val edits = state.sheetEdits.toMutableMap()
            val selectedSheet = state.datasheets[state.selectedSheet]
            val newSheet = edits[selectedSheet.name]?.second ?: return

            val editSheet = cloneDatasheet(newSheet)

            editFunc(editSheet)
            edits[selectedSheet.name] = Pair(true, editSheet)

            state.copy(sheetEdits = edits)
        }
    }

    private fun cloneDatasheet(datasheet: Datasheet): Datasheet {
        return datasheet.copy(
            composition = datasheet.composition.map { it.copy() }.toTypedArray(),
            abilities = datasheet.abilities.map { it.copy() }.toTypedArray(),
            statTable = datasheet.statTable.copy(
                datasheet.statTable.resultBreaks.copyOf(),
                datasheet.statTable.toSave.copyOf(),
                datasheet.statTable.toResist.copyOf(),
                datasheet.statTable.hardness.copyOf(),
                datasheet.statTable.enhancements.copyOf(),
                datasheet.statTable.weapons.map {
                    it.copy(
                        attacks = it.attacks.copyOf(),
                        range = it.range.copyOf(),
                        toHit = it.toHit.copyOf(),
                        damage = it.damage.copyOf(),
                        enhancements = it.enhancements.copyOf()
                    )
                }.toTypedArray()
            ),
            tags = datasheet.tags.copyOf()
        )
    }
}