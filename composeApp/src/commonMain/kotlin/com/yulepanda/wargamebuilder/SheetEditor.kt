package com.yulepanda.wargamebuilder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.ArrowDropDown
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.AssistChip
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SheetEditor(model: AppViewModel, state: AppState, sheet: Datasheet) {
    TextField(
        sheet.name,
        singleLine = true,
        label = { Text("Name") },
        modifier = Modifier.fillMaxWidth(),
        onValueChange = { change -> model.editName(change) },
    )
    Row {
        TextField(
            sheet.modelCount.toString(),
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
            sheet.skill.toString(),
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
            sheet.health.toString(),
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
    SectionDropDown(
        "Abilities & Tags",
        state.showingAbilities,
        { model.toggleShowAbilities() },
        state,
        titleDescription = "Show Abilities"
    ) {
        val height = state.tableRowHeight * 10
        Column(
            modifier = Modifier.height(height).fillMaxWidthPart(2)
        ) {
            // Abilities
            Text("Abilities", modifier = Modifier.padding(6.dp, 3.dp))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.height(1.dp).fillMaxWidth()
            )

            BasicTextField(
                sheet.abilities,
                singleLine = false,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSecondaryContainer,
                    fontSize = 16.sp,
                ),
                modifier = Modifier.fillMaxSize().padding(6.dp),
                onValueChange = { model.setAbilities(it) },
            )
        }
        VerticalDivider(
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.width(1.dp).height(height)
        )

        Column(
            modifier = Modifier.height(height).fillMaxWidth()
        ) {
            // Tags
            Text("Tags", modifier = Modifier.padding(6.dp, 3.dp))
            HorizontalDivider(
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.height(1.dp).fillMaxWidth()
            )

            Column(
                modifier = Modifier.fillMaxSize().padding(6.dp),
            ) {
                Row(modifier = Modifier.height(state.tableRowHeight)) {
                    Icon(
                        Icons.Rounded.Add,
                        contentDescription = "Add Result",
                        modifier = Modifier
                            .width(state.tableRowHeight)
                            .fillMaxHeight()
                            .clickable {
                                model.addTag()
                            }
                    )
                    BasicTextField(
                        state.currentTag,
                        singleLine = false,
                        textStyle = TextStyle(
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            fontSize = 16.sp,
                        ),
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(6.dp, 3.dp)
                            .background(MaterialTheme.colorScheme.secondaryContainer),
                        onValueChange = { model.setCurrentTag(it) },
                    )
                }
                Row(modifier = Modifier.fillMaxSize().padding(6.dp)) {
                    sheet.tags.forEachIndexed { index, s ->
                        AssistChip(
                            onClick = { model.removeTag(index) },
                            label = { Text(s) },
                            trailingIcon = {
                                Icon(Icons.Rounded.Close, contentDescription = "Remove Tag")
                            }
                        )
                    }
                }
            }
        }
    }

    Row {
        StatTableLeftGutter(state.tableRowHeight, sheet.statTable.weapons.size, model)

        Column {
            StatTableResultOptionsRow(
                state.tableRowHeight,
                sheet.statTable.resultBreaks,
                model
            )

            StatTableEditRow(
                MaterialTheme.colorScheme.onPrimary,
                state.tableRowHeight,
                "Results",
                sheet.statTable.resultBreaks.map { toStringPositiveOrEmpty(it) },
                onCellValueChanged = { index, value ->
                    commitChangedInt(value) { num ->
                        model.setResultBreak(
                            num,
                            index
                        )
                    }
                }
            )

            StatTableEditRow(
                MaterialTheme.colorScheme.onSecondary,
                state.tableRowHeight,
                "To Save",
                sheet.statTable.toSave.map { toStringPositiveOrEmpty(it) },
                onCellValueChanged = { index, value ->
                    commitChangedInt(value) { num -> model.setToSave(num, index) }
                }
            )

            StatTableEditRow(
                MaterialTheme.colorScheme.secondaryContainer,
                state.tableRowHeight,
                "To Resist",
                sheet.statTable.toResist.map { toStringPositiveOrEmpty(it ?: 0) },
                onCellValueChanged = { index, value ->
                    commitChangedInt(value) { num -> model.setToResist(num, index) }
                }
            )

            StatTableEditRow(
                MaterialTheme.colorScheme.onSecondary,
                state.tableRowHeight,
                "Hardness",
                sheet.statTable.hardness.map { toStringPositiveOrEmpty(it ?: 0) },
                onCellValueChanged = { index, value ->
                    commitChangedInt(value) { num -> model.setHardness(num, index) }
                }
            )

            StatTableEditRow(
                MaterialTheme.colorScheme.secondaryContainer,
                state.tableRowHeight,
                "Enhancements",
                sheet.statTable.enhancements.map { it ?: "" },
                onCellValueChanged = { index, value ->
                    model.setEnhancements(value, index)
                }
            )

            sheet.statTable.weapons.forEachIndexed { weaponIndex, weapon ->
                StatTableHeaderRow(
                    MaterialTheme.colorScheme.surfaceContainerHighest,
                    state.tableRowHeight,
                    weapon.name
                ) { model.setWeaponName(it, weaponIndex) }

                StatTableEditRow(
                    MaterialTheme.colorScheme.secondaryContainer,
                    state.tableRowHeight,
                    "Attacks",
                    weapon.attacks.map { toStringPositiveOrEmpty(it) }
                ) { index, value ->
                    commitChangedInt(value) {
                        model.setAttacks(
                            it,
                            index,
                            weaponIndex
                        )
                    }
                }

                StatTableEditRow(
                    MaterialTheme.colorScheme.onSecondary,
                    state.tableRowHeight,
                    "Range",
                    weapon.range.map { toStringPositiveOrEmpty(it, "M") }
                ) { index, value ->
                    commitChangedInt(stripM(value)) {
                        model.setRange(
                            it,
                            index,
                            weaponIndex
                        )
                    }
                }

                StatTableEditRow(
                    MaterialTheme.colorScheme.secondaryContainer,
                    state.tableRowHeight,
                    "To Hit",
                    weapon.toHit.map { toStringPositiveOrEmpty(it) }
                ) { index, value ->
                    commitChangedInt(value) {
                        model.setToHit(
                            it,
                            index,
                            weaponIndex
                        )
                    }
                }

                StatTableEditRow(
                    MaterialTheme.colorScheme.onSecondary,
                    state.tableRowHeight,
                    "Damage",
                    weapon.damage.map { toStringPositiveOrEmpty(it) }
                ) { index, value ->
                    commitChangedInt(value) {
                        model.setDamage(
                            it,
                            index,
                            weaponIndex
                        )
                    }
                }

                StatTableEditRow(
                    MaterialTheme.colorScheme.secondaryContainer,
                    state.tableRowHeight,
                    "Enhancements",
                    weapon.enhancements.map { it ?: "" }
                ) { index, value ->
                    model.setWeaponEnhancements(
                        value,
                        index,
                        weaponIndex
                    )
                }
            }
        }
    }
}

@Composable
fun StatTableLeftGutter(
    tableRowHeight: Dp,
    weaponCount: Int,
    model: AppViewModel
) {
    Column(
        modifier = Modifier.width(tableRowHeight),
    ) {
        val baseRows = 5
        val weaponRows = 6

        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                .fillMaxWidth()
                .height(tableRowHeight)
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
                .height(tableRowHeight * baseRows),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {}

        for (i in IntRange(0, weaponCount - 1)) {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .fillMaxWidth()
                    .height(tableRowHeight * weaponRows)
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
                .height(tableRowHeight)
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
}

@Composable
fun StatTableResultOptionsRow(
    tableRowHeight: Dp,
    resultBreaks: Array<Int>,
    model: AppViewModel
) {
    Row(
        modifier = Modifier.height(tableRowHeight)
    ) {
        StatTableCell(
            MaterialTheme.colorScheme.surfaceContainerHigh,
            tableRowHeight,
            Modifier.fillMaxWidth(.2f)
        )
        if (resultBreaks.size > 1) {
            resultBreaks.forEachIndexed { i, _ ->
                Row(
                    modifier = Modifier
                        .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                        .fillMaxHeight()
                        .fillMaxWidthPart(resultBreaks.size - i)
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
            }
        } else {
            Row(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .fillMaxWidth()
                    .height(tableRowHeight),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) { }
        }
    }
}

@Composable
fun StatTableEditRow(
    color: Color,
    tableRowHeight: Dp,
    rowHeader: String,
    values: List<String>,
    onCellValueChanged: (Int, String) -> Unit
) {
    Row(modifier = Modifier.height(tableRowHeight)) {
        StatTableCell(
            color,
            tableRowHeight,
            Modifier.fillMaxWidth(.2f)
        ) {
            Text(rowHeader)
        }

        values.forEachIndexed { i, value ->
            StatTableEditCell(
                color,
                tableRowHeight,
                Modifier.fillMaxWidthPart(values.size - i),
                value,
                onValueChange = { onCellValueChanged(i, it) }
            )
        }
    }
}

@Composable
fun StatTableCell(
    color: Color,
    height: Dp,
    modifier: Modifier = Modifier,
    content: @Composable RowScope.() -> Unit = {}
) {
    Row(
        modifier = modifier
            .background(color)
            .height(height)
            .padding(6.dp, 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        content = content
    )
}

@Composable
fun StatTableHeaderRow(
    color: Color,
    height: Dp,
    value: String,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .background(color)
            .height(height)
            .fillMaxWidth()
            .padding(6.dp, 3.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        BasicTextField(
            value,
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
fun StatTableEditCell(
    color: Color,
    height: Dp,
    modifier: Modifier = Modifier,
    value: String,
    onValueChange: (String) -> Unit = {},
) {
    StatTableCell(
        color,
        height,
        modifier
    ) {
        BasicTextField(
            value,
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

fun stripM(value: String): String {
    return value.replace("M", "")
}