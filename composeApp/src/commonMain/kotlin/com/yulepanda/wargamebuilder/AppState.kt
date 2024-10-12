package com.yulepanda.wargamebuilder

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AppState(
    val catalogs: List<DatasheetCatalog> = listOf(DatasheetCatalog("Init")),
    val selectedCatalog: Int = 0,
    val selectedSheet: Int = 0,
    val newSheetName: String = "",
    val sheetEdits: Map<String, Pair<Boolean, Datasheet>> = mapOf(),
    val tableRowHeight: Dp = 32.dp,
    val showingAbilities: Boolean = false,
    val currentTag: String = ""
)

class AppViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(AppState())
    val uiState: StateFlow<AppState> = _uiState.asStateFlow()

    fun loadAllSheets() {
        _uiState.update { state ->
            val catalogs = getDatasheetCatalogs().toMutableList()

            // put default first
            val index = catalogs.findIndex { it.name == "Default" }
            if (index == null) {
                val defaultCatalog = DatasheetCatalog("Default")
                saveDatasheetCatalog("Default", defaultCatalog)
                catalogs.add(0, defaultCatalog)
            } else {
                val default = catalogs.removeAt(index)
                catalogs.add(0, default)
            }

            state.copy(
                catalogs = catalogs
            )
        }

        setSelectedSheet(uiState.value.selectedSheet)
    }

    fun addCatalog(name: String) {
        _uiState.update { state ->
            val catalogs = state.catalogs.toMutableList()
            val catalog = DatasheetCatalog(name)

            saveDatasheetCatalog(name, catalog)
            catalogs.add(DatasheetCatalog(name))

            state.copy(catalogs = catalogs)
        }
    }

    fun removeCatalog() {
        if (uiState.value.selectedCatalog >= uiState.value.catalogs.size) {
            return
        }

        _uiState.update { state ->
            val catalogs = state.catalogs.toMutableList()
            val removed = catalogs.removeAt(state.selectedCatalog)
            deleteDatasheetCatalog(removed.name)

            var newSelected = state.selectedCatalog
            if (newSelected >= catalogs.size) {
                newSelected = catalogs.size - 1
            }

            state.copy(catalogs = catalogs, selectedCatalog = newSelected)
        }
    }

    fun renameCatalog(name: String) {
        if (uiState.value.selectedCatalog >= uiState.value.catalogs.size) {
            return
        }

        _uiState.update { state ->
            val catalogs = state.catalogs.toMutableList()
            val catalog = catalogs[state.selectedCatalog]
            val new = DatasheetCatalog(name)
            new.datasheets = catalog.datasheets
            catalogs[state.selectedCatalog] = new

            saveDatasheetCatalog(catalog.name, catalog)

            state.copy(catalogs = catalogs)
        }
    }

    fun setSelectedCatalog(index: Int) {
        if (index >= uiState.value.catalogs.size) return

        _uiState.update { it.copy(selectedCatalog = index) }
    }

    fun addNewSheet() {
        if (uiState.value.newSheetName.isBlank()) {
            // error
            return
        }

        if (!currentCatalogHasName(uiState.value.newSheetName)) {
            _uiState.update { state ->
                val catalogs = state.catalogs.toMutableList()
                val catalog = catalogs[uiState.value.selectedCatalog].copy()
                val sheets = catalog.datasheets.toMutableList()
                val edits = state.sheetEdits.toMutableMap()
                val datasheet = Datasheet()
                datasheet.name = uiState.value.newSheetName

                val newIndex = sheets.size

                edits[datasheet.name] = Pair(false, datasheet)

                // insert in alphabetical order
                if (sheets.size == 0) {
                    sheets.add(datasheet)
                } else {
                    val neighbor = sheets.mapIndexed { i, v -> Pair(i, v) }.find { pair -> datasheet.name < pair.second.name }
                    if (neighbor != null) {
                        val index = neighbor.first
                        sheets.add(index, datasheet)
                    } else {
                        sheets.add(datasheet)
                    }
                }

                catalog.datasheets = sheets
                catalogs[state.selectedCatalog] = catalog

                saveDatasheetCatalog(catalog.name, catalog)

                state.copy(
                    newSheetName = "",
                    selectedSheet = newIndex,
                    sheetEdits = edits,
                    catalogs = catalogs
                )
            }
        } else {
            // error
        }
    }

    fun toggleShowAbilities() {
        _uiState.update { it.copy(showingAbilities = !it.showingAbilities) }
    }

    fun setNewSheetName(text: String) {
        _uiState.update { state ->
            state.copy(newSheetName = text)
        }
    }

    fun deleteSelectedSheet() {
        _uiState.update { state ->
            val catalogs = state.catalogs.toMutableList()
            val catalog = catalogs[uiState.value.selectedCatalog].copy()
            val sheets = catalog.datasheets.toMutableList()
            val edits = state.sheetEdits.toMutableMap()
            sheets.removeAt(_uiState.value.selectedSheet)

            val selected = if (_uiState.value.selectedSheet >= sheets.size) {
                sheets.size - 1
            } else {
                _uiState.value.selectedSheet
            }

            if (selected >= 0 && !edits.containsKey(sheets[selected].name)) {
                edits[sheets[selected].name] = Pair(false, cloneDatasheet(sheets[selected]))
            }

            catalog.datasheets = sheets
            catalogs[state.selectedCatalog] = catalog

            saveDatasheetCatalog(catalog.name, catalog)

            state.copy(
                selectedSheet = selected,
                sheetEdits = edits,
                catalogs = catalogs
            )
        }
    }

    fun saveSelectedSheet() {
        _uiState.update { state ->
            val catalogs = state.catalogs.toMutableList()
            val catalog = catalogs[uiState.value.selectedCatalog].copy()
            val sheets = catalog.datasheets.toMutableList()
            val edits = state.sheetEdits.toMutableMap()
            val selectedSheet = catalog.datasheets[state.selectedSheet]
            val oldName = selectedSheet.name
            val editSheet = edits[selectedSheet.name]?.second?.copy() ?: return
            sheets[state.selectedSheet] = editSheet

            catalog.datasheets = sheets

            edits.remove(oldName)
            edits[editSheet.name] = Pair(false, sheets[state.selectedSheet])

            catalogs[state.selectedCatalog] = catalog

            saveDatasheetCatalog(catalog.name, catalog)

            state.copy(catalogs = catalogs, sheetEdits = edits)
        }
    }

    fun setSelectedSheet(index: Int) {
        _uiState.update { state ->
            val catalogs = state.catalogs.toMutableList()
            val catalog = catalogs[uiState.value.selectedCatalog].copy()
            if (catalog.datasheets.getOrNull(index) == null) {
                return
            }

            val edits = state.sheetEdits.toMutableMap()
            val selectedSheet = catalog.datasheets[index]
            if (!edits.containsKey(selectedSheet.name)) {
                edits[selectedSheet.name] = Pair(false, selectedSheet.copy())
            }

            state.copy(
                catalogs = catalogs,
                selectedSheet = index,
                sheetEdits = edits
            )
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

    fun setModelCount(value: Int?) {
        if (value == null) {
            // show error
        } else {
            editSheetValue { it.modelCount = value }
        }
    }

    fun setAbilities(value: String) {
        editSheetValue { it.abilities = value }
    }

    fun setCurrentTag(value: String) {
        _uiState.update { it.copy(currentTag = value) }
    }

    fun addTag() {
        if (uiState.value.currentTag.isBlank()) {
            return
        }

        editSheetValue { sheet ->
            val tags = sheet.tags.toMutableList()
            tags.add(uiState.value.currentTag)
            sheet.tags = tags.toTypedArray()
        }

        _uiState.update { it.copy(currentTag = "") }
    }

    fun removeTag(index: Int) {
        editSheetValue { sheet ->
            val tags = sheet.tags.toMutableList()
            tags.removeAt(index)
            sheet.tags = tags.toTypedArray()
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

    fun addResult() {
        _uiState.update { state ->
            val catalogs = state.catalogs.toMutableList()
            val catalog = catalogs[uiState.value.selectedCatalog].copy()
            val edits = state.sheetEdits.toMutableMap()
            val selectedSheet = catalog.datasheets[state.selectedSheet]
            val newSheet = edits[selectedSheet.name]?.second ?: return

            val editSheet = cloneDatasheet(newSheet)

            editSheet.statTable.resultBreaks = editSheet.statTable.resultBreaks.plus(0)
            editSheet.statTable.toSave = editSheet.statTable.toSave.plus(4)
            editSheet.statTable.toResist = editSheet.statTable.toResist.plus(null)
            editSheet.statTable.hardness = editSheet.statTable.hardness.plus(null)
            editSheet.statTable.enhancements = editSheet.statTable.enhancements.plus(null)

            for (weapon in editSheet.statTable.weapons) {
                weapon.attacks = weapon.attacks.plus(0)
                weapon.toHit = weapon.toHit.plus(4)
                weapon.range = weapon.range.plus(5)
                weapon.damage = weapon.damage.plus(1)
                weapon.enhancements = weapon.enhancements.plus(null)
            }

            edits[selectedSheet.name] = Pair(true, editSheet)

            state.copy(sheetEdits = edits)
        }
    }

    fun removeResult(index: Int) {
        _uiState.update { state ->
            val catalogs = state.catalogs.toMutableList()
            val catalog = catalogs[uiState.value.selectedCatalog].copy()
            val edits = state.sheetEdits.toMutableMap()
            val selectedSheet = catalog.datasheets[state.selectedSheet]
            val newSheet = edits[selectedSheet.name]?.second ?: return

            val editSheet = cloneDatasheet(newSheet)

            editSheet.statTable.resultBreaks = editSheet.statTable.resultBreaks.pluck(index)
            editSheet.statTable.toSave = editSheet.statTable.toSave.pluck(index)
            editSheet.statTable.toResist = editSheet.statTable.toResist.pluck(index)
            editSheet.statTable.hardness = editSheet.statTable.hardness.pluck(index)
            editSheet.statTable.enhancements = editSheet.statTable.enhancements.pluck(index)

            for (weapon in editSheet.statTable.weapons) {
                weapon.attacks = weapon.attacks.pluck(index)
                weapon.toHit = weapon.toHit.pluck(index)
                weapon.range = weapon.range.pluck(index)
                weapon.damage = weapon.damage.pluck(index)
                weapon.enhancements = weapon.enhancements.pluck(index)
            }

            edits[selectedSheet.name] = Pair(true, editSheet)

            state.copy(sheetEdits = edits)
        }
    }

    fun addWeapon() {
        _uiState.update { state ->
            val catalogs = state.catalogs.toMutableList()
            val catalog = catalogs[uiState.value.selectedCatalog].copy()
            val edits = state.sheetEdits.toMutableMap()
            val selectedSheet = catalog.datasheets[state.selectedSheet]
            val newSheet = edits[selectedSheet.name]?.second ?: return

            val editSheet = cloneDatasheet(newSheet)

            val weapon = WeaponTable()
            val additionColumns = editSheet.statTable.resultBreaks.size - 1

            for (i in IntRange(0, additionColumns - 1)) {
                weapon.attacks = weapon.attacks.plus(1)
                weapon.toHit = weapon.toHit.plus(4)
                weapon.range = weapon.range.plus(5)
                weapon.damage = weapon.damage.plus(1)
                weapon.enhancements = weapon.enhancements.plus(null)
            }

            editSheet.statTable.weapons = editSheet.statTable.weapons.plus(weapon)

            edits[selectedSheet.name] = Pair(true, editSheet)

            state.copy(sheetEdits = edits)
        }
    }

    fun removeWeapon(index: Int) {
        _uiState.update { state ->
            val catalogs = state.catalogs.toMutableList()
            val catalog = catalogs[uiState.value.selectedCatalog].copy()
            val edits = state.sheetEdits.toMutableMap()
            val selectedSheet = catalog.datasheets[state.selectedSheet]
            val newSheet = edits[selectedSheet.name]?.second ?: return

            val editSheet = cloneDatasheet(newSheet)
            val mutable = editSheet.statTable.weapons.toMutableList()
            mutable.removeAt(index)
            editSheet.statTable.weapons = mutable.toTypedArray()

            edits[selectedSheet.name] = Pair(true, editSheet)

            state.copy(sheetEdits = edits)
        }
    }

    private fun editSheetValue(editFunc: (Datasheet) -> Unit) {
        _uiState.update { state ->
            val catalogs = state.catalogs.toMutableList()
            val catalog = catalogs[uiState.value.selectedCatalog].copy()
            val edits = state.sheetEdits.toMutableMap()
            val selectedSheet = catalog.datasheets[state.selectedSheet]
            val newSheet = edits[selectedSheet.name]?.second ?: return
            val editSheet = cloneDatasheet(newSheet)

            editFunc(editSheet)
            edits[selectedSheet.name] = Pair(true, editSheet)

            state.copy(sheetEdits = edits)
        }
    }

    private fun cloneDatasheet(datasheet: Datasheet): Datasheet {
        return datasheet.copy(
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

    private fun currentCatalogHasName(name: String): Boolean {
        val catalog = uiState.value.catalogs[uiState.value.selectedCatalog]
        return catalog.datasheets.find { it.name == name } != null
    }
}