package com.yulepanda.wargamebuilder

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun SheetExplorer(state: AppState, model: AppViewModel) {
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