package com.yulepanda.wargamebuilder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(model: AppViewModel = AppViewModel()) {
    MaterialTheme(
        colorScheme = darkColorScheme(
        )
    ) {
        val state by model.uiState.collectAsState()

        val currentCatalog = state.catalogs[state.selectedCatalog]
        val editSheet = if (
            state.selectedSheet >= 0 &&
            state.selectedSheet < currentCatalog.datasheets.size
        ) {
            val sheet = currentCatalog.datasheets[state.selectedSheet]
            state.sheetEdits[sheet.name]?.second
        } else {
            null
        }

        Scaffold {
            Row(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .fillMaxHeight()
                        .fillMaxWidth(.25f)
                        .padding(5.dp)
                ) {
                    SheetExplorer(currentCatalog, state, model)
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(.67f)
                        .padding(5.dp)
                ) {
                    // center panel
                    Column {
                        // sheet options
                        Row(
                            modifier = Modifier.fillMaxWidth()
                                .fillMaxHeight(.9f)
                        ) {
                            Column(
                                modifier = Modifier
                                    .verticalScroll(rememberScrollState())
                            ) {
                                if (editSheet != null) {
                                    SheetEditor(model, state, editSheet)
                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxSize(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            FloatingActionButton(onClick = { model.saveSelectedSheet() }) {
                                Icon(Icons.Filled.Done, contentDescription = "Save")
                            }
                            Spacer(Modifier.width(10.dp))
                            FloatingActionButton(onClick = { model.deleteSelectedSheet() }) {
                                Icon(Icons.Rounded.Delete, contentDescription = "Delete")
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainer)
                        .fillMaxSize()
                ) {
                    // center panel
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                    ) {
                        // sheet preview
                        StatisticsPanel(
                            state,
                            model,
                            editSheet
                        )
                    }
                }
            }
        }
    }

    model.loadAllSheets()
}
