package com.yulepanda.wargamebuilder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.PlayArrow
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
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                                Column(modifier = Modifier
                                    .verticalScroll(rememberScrollState())) {
                                    if (editSheet == null) {
                                        // sheet preview
                                        Text("Select a datasheet")
                                    } else {
                                        EditSheet(model, state, editSheet)
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

@Composable
fun EditSheet(model: AppViewModel, state: AppState, sheet: Datasheet) {
    TextField(
        sheet.name,
        singleLine = true,
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { change -> model.editName(change) },
    )
    Row {
        TextField(
            toStringPositiveOrEmpty(sheet.modelCount),
            singleLine = true,
            label = { Text("Model Count") },
            modifier = Modifier.fillMaxWidthPart(4),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            onValueChange = {
                model.setModelCount(parseChangedInt(it))
            },
        )
        TextField(
            toStringPositiveOrEmpty(sheet.skill),
            singleLine = true,
            label = { Text("Skill") },
            modifier = Modifier.fillMaxWidthPart(3),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            onValueChange = {
                model.editSkill(parseChangedInt(it))
            },
        )
        TextField(
            toStringPositiveOrEmpty(sheet.health),
            singleLine = true,
            label = { Text("Health") },
            modifier = Modifier.fillMaxWidthPart(2),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            ),
            onValueChange = {
                model.editHealth(parseChangedInt(it))
            },
        )
        TextField(
            toStringPositiveOrEmpty(sheet.speed),
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
    SectionDropDown(
        "Abilities & Tags",
        state.showingAbilities,
        { model.toggleShowAbilities() },
        state,
        titleDescription = "Show Abilities"
    ) {
        Column(
            modifier = Modifier.height(state.tableRowHeight * 5).fillMaxWidthPart(2)
        ) {
            // Abilities
            Text("Abilities")
        }

        Column(
            modifier = Modifier.height(state.tableRowHeight * 5).fillMaxWidth()
        )  {
            // Tags
            Text("Tags")
        }
    }
    Row {
        Column(
            modifier = Modifier.width(state.tableRowHeight),
        ) {
            val baseRows = 5
            val weaponRows = 6

            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .fillMaxWidth()
                    .height(state.tableRowHeight)
                    .clickable {
                        model.addResult()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = "Add Result",
                    modifier = Modifier
                )
            }

            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .fillMaxWidth()
                    .height(state.tableRowHeight * baseRows),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {}

            for (i in IntRange(0, sheet.statTable.weapons.size - 1)) {
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .fillMaxWidth()
                        .height(state.tableRowHeight * weaponRows)
                        .clickable {
                            model.removeWeapon(i)
                        },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        Icons.Rounded.Delete,
                        contentDescription = "Delete Weapon",
                        modifier = Modifier
                    )
                }
            }
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .fillMaxWidth()
                    .height(state.tableRowHeight)
                    .clickable {
                        model.addWeapon()
                    },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = "Add Weapon",
                    modifier = Modifier
                )
            }
        }

        // Row Headers
        Column(
            modifier = Modifier.fillMaxWidth(.2f),
        ) {
            EmptyTableRow(MaterialTheme.colorScheme.surfaceContainerHigh, state)
            StatTableHeaderRow("Result", MaterialTheme.colorScheme.onPrimary, state)
            StatTableHeaderRow("To Save", MaterialTheme.colorScheme.onSecondary, state)
            StatTableHeaderRow("To Resist", MaterialTheme.colorScheme.secondaryContainer, state)
            StatTableHeaderRow("Hardness", MaterialTheme.colorScheme.onSecondary, state)
            StatTableHeaderRow("Enhancements", MaterialTheme.colorScheme.secondaryContainer, state)

            for (i in IntRange(0, sheet.statTable.weapons.size - 1)) {
                val weapon = sheet.statTable.weapons[i]
                StatTableWeaponHeaderRow(weapon.name, MaterialTheme.colorScheme.surfaceContainerHighest, state) {
                    model.setWeaponName(it, i)
                }
                StatTableHeaderRow("Attacks", MaterialTheme.colorScheme.secondaryContainer, state)
                StatTableHeaderRow("Range", MaterialTheme.colorScheme.onSecondary, state)
                StatTableHeaderRow("To Hit", MaterialTheme.colorScheme.secondaryContainer, state)
                StatTableHeaderRow("Damage", MaterialTheme.colorScheme.onSecondary, state)
                StatTableHeaderRow("Enhancements", MaterialTheme.colorScheme.secondaryContainer, state)
            }
        }

        // Data Columns
        val parts = sheet.statTable.resultBreaks.size
        val showResultDelete = parts > 1
        for (i in IntRange(0, parts - 1)) {
            val result = sheet.statTable.resultBreaks[i]
            val toSave = sheet.statTable.toSave[i]
            val toResist = sheet.statTable.toResist[i]
            val hardness = sheet.statTable.hardness[i]
            val enhancements = sheet.statTable.enhancements[i]

            Column(
                modifier = Modifier.fillMaxWidthPart(parts - i),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (showResultDelete) {
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            .fillMaxWidth()
                            .height(state.tableRowHeight)
                            .clickable {
                                model.removeResult(i)
                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Rounded.Delete,
                            contentDescription = "Delete Result",
                            modifier = Modifier
                        )
                    }
                } else {
                    Row(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                            .fillMaxWidth()
                            .height(state.tableRowHeight),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) { }
                }
                StatTableRow(toStringPositiveOrEmpty(result), MaterialTheme.colorScheme.onPrimary, state) {
                    commitChangedInt(it) { num -> model.setResultBreak(num, i) }
                }
                StatTableRow(toStringPositiveOrEmpty(toSave), MaterialTheme.colorScheme.onSecondary, state) {
                    commitChangedInt(it) { num -> model.setToSave(num, i) }
                }
                StatTableRow(
                    toStringPositiveOrEmpty(toResist ?: 0),
                    MaterialTheme.colorScheme.secondaryContainer,
                    state
                ) {
                    commitChangedInt(it) { num -> model.setToResist(num, i) }
                }
                StatTableRow(toStringPositiveOrEmpty(hardness ?: 0), MaterialTheme.colorScheme.onSecondary, state) {
                    commitChangedInt(it) { num -> model.setHardness(num, i) }
                }
                StatTableRow(enhancements.orEmpty(), MaterialTheme.colorScheme.secondaryContainer, state) {
                    model.setEnhancements(it, i)
                }

                for (w in IntRange(0, sheet.statTable.weapons.size - 1)) {
                    val weapon = sheet.statTable.weapons[w]
                    val attacks = weapon.attacks[i]
                    val range = weapon.range[i]
                    val toHit = weapon.toHit[i]
                    val damage = weapon.damage[i]
                    val weaponEnhancements = weapon.enhancements[i]

                    EmptyTableRow(MaterialTheme.colorScheme.surfaceContainerHighest, state)
                    StatTableRow(toStringPositiveOrEmpty(attacks), MaterialTheme.colorScheme.secondaryContainer, state) {
                        commitChangedInt(it) { num -> model.setAttacks(num, i, w) }
                    }
                    StatTableRow(toStringPositiveOrEmpty(range, "M"), MaterialTheme.colorScheme.onSecondary, state) {
                        commitChangedInt(it) { num -> model.setRange(num, i, w) }
                    }
                    StatTableRow(toStringPositiveOrEmpty(toHit), MaterialTheme.colorScheme.secondaryContainer, state) {
                        commitChangedInt(it) { num -> model.setToHit(num, i, w) }
                    }
                    StatTableRow(toStringPositiveOrEmpty(damage), MaterialTheme.colorScheme.onSecondary, state) {
                        commitChangedInt(it) { num -> model.setDamage(num, i, w) }
                    }
                    StatTableRow(
                        weaponEnhancements.orEmpty(),
                        MaterialTheme.colorScheme.secondaryContainer,
                        state
                    ) {
                        model.setWeaponEnhancements(it, i, w)
                    }
                }
            }
        }
    }
}

@Composable
fun StatTableHeaderRow(text: String, color: Color, state: AppState) {
    Row(
        modifier = Modifier
            .background(color)
            .fillMaxWidth()
            .height(state.tableRowHeight)
            .padding(6.dp, 3.dp)
    ) { Text(text) }
}

@Composable
fun StatTableWeaponHeaderRow(
    text: String,
    color: Color,
    state: AppState,
    onValueChange: (String) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .background(color)
            .fillMaxWidth()
            .height(state.tableRowHeight)
            .padding(6.dp, 3.dp)
    ) {
        BasicTextField(
            text,
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 16.sp,
            ),
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onValueChange,
        )
    }
}

@Composable
fun EmptyTableRow(color: Color, state: AppState) {
    Row(
        modifier = Modifier
            .background(color)
            .fillMaxWidth()
            .height(state.tableRowHeight)
            .padding(6.dp, 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {}
}

@Composable
fun StatTableRow(
    text: String,
    color: Color, state: AppState,
    onValueChange: (String) -> Unit = {},
) {
    Row(
        modifier = Modifier
            .background(color)
            .fillMaxWidth()
            .height(state.tableRowHeight)
            .padding(6.dp, 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        BasicTextField(
            text,
            singleLine = true,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                fontSize = 16.sp,
                textAlign = TextAlign.Center
            ),
            modifier = Modifier.fillMaxWidth(),
            onValueChange = onValueChange,
        )
    }
}

@Composable
fun SectionDropDown(
    title: String,
    showing: Boolean,
    clickAction: () -> Unit,
    state: AppState,
    titleDescription: String = title,
    content: @Composable RowScope.() -> Unit
) {
    Row {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(state.tableRowHeight)
            ) {
                Icon(
                    Icons.Rounded.ArrowDropDown,
                    contentDescription = titleDescription,
                    modifier = Modifier
                        .rotate(if (showing) 0.0f else -90.0f)
                        .clickable(onClick = clickAction)
                )
                Text(title)
                Box(modifier = Modifier.fillMaxSize())
            }
            if (showing) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                    content = content
                )
            }
        }
    }
}

fun commitChangedInt(change: String, commit: (Int) -> Unit) {
    commit(parseChangedInt(change) ?: return)
}

fun parseChangedInt(change: String): Int? {
    return if (change.isBlank()) {
        0
    } else {
        change.toIntOrNull()
    }
}

fun toStringPositiveOrEmpty(value: Int, default: String = ""): String {
    return if (value > 0) value.toString() else default
}