package com.yulepanda.wargamebuilder

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SheetExplorer(state: AppState, model: AppViewModel) {
    var addCatalogDialogOpen by remember { mutableStateOf(false) }
    var renameCatalogDialogOpen by remember { mutableStateOf(false) }

    var catalogOptionsOpen by remember { mutableStateOf(false) }
    var catalogListOpen by remember { mutableStateOf(false) }
    val selectedCatalog = state.catalogs[state.selectedCatalog]

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        Box {
            IconButton(onClick = { catalogOptionsOpen = true }) {
                Icon(Icons.Default.MoreVert, contentDescription = "Localized description")
            }
            DropdownMenu(
                expanded = catalogOptionsOpen,
                onDismissRequest = { catalogOptionsOpen = false },
            ) {
                DropdownMenuItem(
                    text = { Text("New Catalog") },
                    onClick = {
                        addCatalogDialogOpen = true
                        catalogOptionsOpen = false
                    },
                )
                DropdownMenuItem(
                    enabled = state.selectedCatalog > 0,
                    text = { Text("Rename Catalog") },
                    onClick = {
                        renameCatalogDialogOpen = true
                        catalogOptionsOpen = false
                    },
                )
                DropdownMenuItem(
                    enabled = state.selectedCatalog > 0,
                    text = { Text("Delete Catalog") },
                    onClick = {
                        model.removeCatalog()
                        catalogOptionsOpen = false
                    },
                )
            }
        }
        Box {
            TextButton(
                shape = RectangleShape,
                onClick = { catalogListOpen = true }
            ) {
                Text(selectedCatalog.name)
            }
            DropdownMenu(
                expanded = catalogListOpen,
                onDismissRequest = { catalogListOpen = false },
            ) {
                state.catalogs.forEachIndexed { index, datasheetCatalog ->
                    DropdownMenuItem(
                        text = { Text(datasheetCatalog.name) },
                        onClick = {
                            model.setSelectedCatalog(index)
                            catalogListOpen = false
                        },
                    )
                }
            }
        }
    }

    if (addCatalogDialogOpen) {
        SingleLineTextDialog(
            "New Catalog",
            { addCatalogDialogOpen = false },
            {
                model.addCatalog(it)
                addCatalogDialogOpen = false
            }
        )
    } else if (renameCatalogDialogOpen) {
        SingleLineTextDialog(
            "Rename Catalog",
            { renameCatalogDialogOpen = false },
            {
                model.renameCatalog(it)
                renameCatalogDialogOpen = false
            }
        )
    }

    TextField(
        value = state.newSheetName,
        singleLine = true,
        placeholder = { Text("New Datasheet") },
        onValueChange = { model.setNewSheetName(it) },
        modifier = Modifier.fillMaxWidth(),
        trailingIcon = {
            IconButton(
                onClick = { model.addNewSheet() },
                modifier = Modifier
                    .padding(0.dp)
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "New Datasheet")
            }
        }
    )
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
    ) {
        itemsIndexed(state.datasheets) { index, datasheet ->
            val isSelected = state.selectedSheet == index
            val color = if (isSelected) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.surfaceContainer

            Row(modifier = Modifier
                .fillMaxWidth()
                .background(color)
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SingleLineTextDialog(
    title: String,
    onDismissRequest: () -> Unit,
    onConfirmRequest: (String) -> Unit,
    onCancelRequest: () -> Unit = onDismissRequest,
    placeholder: String = "Name"
) {
    var value by remember { mutableStateOf("") }

    BasicAlertDialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = Modifier.wrapContentSize(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(12.dp)
            ) {
                Text(title)

                Spacer(modifier = Modifier.height(16.dp))

                TextField(
                    value = value,
                    singleLine = true,
                    placeholder = { Text(placeholder) },
                    onValueChange = { value = it },
                    modifier = Modifier.fillMaxWidth(),
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row {
                    TextButton(
                        enabled = value.isNotBlank(),
                        onClick = { onConfirmRequest(value) },
                        shape = RectangleShape
                    ) {
                        Text("Create")
                    }

                    TextButton(
                        onClick = onCancelRequest,
                        shape = RectangleShape
                    ) {
                        Text("Cancel")
                    }
                }
            }
        }
    }
}