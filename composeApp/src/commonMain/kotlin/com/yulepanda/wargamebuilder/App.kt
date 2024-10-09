package com.yulepanda.wargamebuilder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
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
                                        EditSheet(model, editSheet)
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

@Composable
fun EditSheet(model: AppViewModel, sheet: Datasheet) {
    TextField(
        sheet.name,
        singleLine = true,
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { change -> model.editName(change) },
    )
    Row {
        TextField(
            sheet.skill.toString(),
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
            sheet.health.toString(),
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
            sheet.speed.toString(),
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
    Row {
        Column(
            modifier = Modifier.fillMaxWidth(.2f),
        ) {
            StatTableHeaderRow("Result", MaterialTheme.colorScheme.onPrimary)
            StatTableHeaderRow("To Save", MaterialTheme.colorScheme.onSecondary)
            StatTableHeaderRow("To Resist", MaterialTheme.colorScheme.secondaryContainer)
            StatTableHeaderRow("Hardness", MaterialTheme.colorScheme.onSecondary)
            StatTableHeaderRow("Enhancements", MaterialTheme.colorScheme.secondaryContainer)

            for (i in IntRange(0, sheet.statTable.weapons.size - 1)) {
                val weapon = sheet.statTable.weapons[i]
                StatTableHeaderRow(weapon.name, MaterialTheme.colorScheme.primaryContainer)
                StatTableHeaderRow("Attacks", MaterialTheme.colorScheme.secondaryContainer)
                StatTableHeaderRow("Range", MaterialTheme.colorScheme.onSecondary)
                StatTableHeaderRow("To Hit", MaterialTheme.colorScheme.secondaryContainer)
                StatTableHeaderRow("Damage", MaterialTheme.colorScheme.onSecondary)
                StatTableHeaderRow("Enhancements", MaterialTheme.colorScheme.secondaryContainer)
            }
        }

        val parts = sheet.statTable.resultBreaks.size
        for (i in IntRange(0, parts - 1)) {
            val result = sheet.statTable.resultBreaks[i]
            val toSave = sheet.statTable.toSave[i]
            val toResist = sheet.statTable.toResist?.get(i)
            val hardness = sheet.statTable.hardness?.get(i)
            val enhancements = sheet.statTable.enhancements?.get(i)

            Column(
                modifier = Modifier.fillMaxWidth(1.0f / parts),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                StatTableRow(result.toString(), MaterialTheme.colorScheme.onPrimary)
                StatTableRow(toSave.toString(), MaterialTheme.colorScheme.onSecondary)
                StatTableRow(toResist?.toString() ?: "", MaterialTheme.colorScheme.secondaryContainer)
                StatTableRow(hardness?.toString() ?: "", MaterialTheme.colorScheme.onSecondary)
                StatTableRow(enhancements.orEmpty(), MaterialTheme.colorScheme.secondaryContainer)

                for (w in IntRange(0, sheet.statTable.weapons.size - 1)) {
                    val weapon = sheet.statTable.weapons[w]
                    val attacks = weapon.attacks[i]
                    val range = weapon.range[i]
                    val toHit = weapon.toHit[i]
                    val damage = weapon.damage[i]
                    val weaponEnhancements = weapon.enhancements?.get(i)

                    StatTableRow("", MaterialTheme.colorScheme.primaryContainer)
                    StatTableRow(attacks.toString(), MaterialTheme.colorScheme.secondaryContainer)
                    StatTableRow(range.toString(), MaterialTheme.colorScheme.onSecondary)
                    StatTableRow(toHit.toString(), MaterialTheme.colorScheme.secondaryContainer)
                    StatTableRow(damage.toString(), MaterialTheme.colorScheme.onSecondary)
                    StatTableRow(weaponEnhancements.orEmpty(), MaterialTheme.colorScheme.secondaryContainer)
                }
            }
        }
    }
}

@Composable
fun StatTableHeaderRow(text: String, color: Color) {
    Row(
        modifier = Modifier
            .background(color)
            .fillMaxWidth()
            .padding(6.dp, 3.dp)
    ) { Text(text) }
}

@Composable
fun StatTableRow(text: String, color: Color) {
    Row(
        modifier = Modifier
            .background(color)
            .fillMaxWidth()
            .padding(6.dp, 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) { Text(text) }
}