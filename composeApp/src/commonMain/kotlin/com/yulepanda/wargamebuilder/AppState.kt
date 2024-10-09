package com.yulepanda.wargamebuilder

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AppState(
    val datasheets: List<Datasheet> = listOf(Datasheet()),
    val selectedSheet: Int = 0,
    val newSheetName: String = ""
)

class AppViewModel: ViewModel() {
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
            state.copy(datasheets = getDatasheets())
        }
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

    fun setSelected(index: Int) {
        _uiState.update { it.copy(selectedSheet = index) }
    }
}