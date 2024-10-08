package com.yulepanda.wargamebuilder

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class AppState(
    val datasheets: List<Datasheet> = listOf(Datasheet())
) {
    var selectedSheet = 0
}

class AppViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AppState())
    val uiState: StateFlow<AppState> = _uiState.asStateFlow()

    fun addNewSheet() {
        _uiState.update { s ->
            val sheets = s.datasheets.toMutableList()
            val datasheet = Datasheet()

            val path = "${datasheet.name}.json"
            saveDatasheet(path, datasheet)

            sheets.add(datasheet)
            s.copy(datasheets = sheets)
        }
    }

    fun saveSheets() {
        for (datasheet in _uiState.value.datasheets) {
            val path = "${datasheet.name}.json"
            saveDatasheet(path, datasheet)
        }
    }
}