package com.yulepanda.wargamebuilder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
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

        val editSheet = if (state.selectedSheet < state.datasheets.size) {
            val sheet = state.datasheets[state.selectedSheet]
            state.sheetEdits[sheet.name]?.second
        } else {
            null
        }
        println("current sheet: ${editSheet?.name}")

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

                    TextField(
                        value = state.newSheetName,
                        singleLine = true,
                        placeholder = { Text("New Datasheet") },
                        onValueChange = { model.setNewSheetName(it) },
                        modifier = Modifier.fillMaxWidth(),
                        trailingIcon = {
                            Icon(
                                Icons.Rounded.Add,
                                contentDescription = "New Datasheet",
                                modifier = Modifier
                                    .padding(0.dp)
                                    .clickable { model.addNewSheet() }
                            )
                        }
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        itemsIndexed(state.datasheets) { index, datasheet ->
                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .clickable { model.setSelected(index) }
                            ) {
                                Text(
                                    datasheet.name,
                                    textAlign = TextAlign.Left,
                                    modifier = Modifier
                                        .padding(6.dp, 3.dp)
                                )
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(.67f)
                ) {
                    // center panel
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(5.dp)
                    ) {
                        Column {
                            // sheet options
                            Row(
                                modifier = Modifier.fillMaxWidth()
                                    .fillMaxHeight(.9f)
                            ) {
                                Column {
                                    if (editSheet == null) {
                                        // sheet preview
                                        Text("Select a datasheet")
                                    } else {
                                        TextField(
                                            editSheet.name,
                                            singleLine = true,
                                            label = { Text("Name") },
                                            modifier = Modifier.fillMaxWidth(),
                                            onValueChange = { change -> model.editName(change) },
                                        )
                                        Row {
                                            TextField(
                                                editSheet.skill.toString(),
                                                singleLine = true,
                                                label = { Text("Skill") },
                                                modifier = Modifier.fillMaxWidth(.34f),
                                                keyboardOptions = KeyboardOptions(
                                                    keyboardType = KeyboardType.Number
                                                ),
                                                onValueChange = {
                                                    model.editSkill(parseChangedInt(it))
                                                },
                                            )
                                            TextField(
                                                editSheet.health.toString(),
                                                singleLine = true,
                                                label = { Text("Health") },
                                                modifier = Modifier.fillMaxWidth(.5f),
                                                keyboardOptions = KeyboardOptions(
                                                    keyboardType = KeyboardType.Number
                                                ),
                                                onValueChange = {
                                                    model.editHealth(parseChangedInt(it))
                                                },
                                            )
                                            TextField(
                                                editSheet.speed.toString(),
                                                singleLine = true,
                                                label = { Text("Speed") },
                                                modifier = Modifier.fillMaxWidth(),
                                                keyboardOptions = KeyboardOptions(
                                                    keyboardType = KeyboardType.Number
                                                ),
                                                onValueChange = {
                                                    model.editSpeed(parseChangedInt(it))
                                                },
                                            )
                                        }
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
                                FloatingActionButton(onClick = { model.deleteSelectedSheet() }) {
                                    Icon(Icons.Rounded.Delete, contentDescription = "Delete")
                                }
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
                        Text("Statistics")
                    }
                }
            }
        }
    }

    model.loadAllSheets()
}

fun parseChangedInt(change: String): Int? {
    return if (change.isBlank()) {
        0
    } else {
        change.toIntOrNull()
    }
}