package com.yulepanda.wargamebuilder

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.darkColors
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import wargamebuilder.composeapp.generated.resources.Res
import wargamebuilder.composeapp.generated.resources.compose_multiplatform

@Composable
@Preview
fun App(state: AppState) {
    MaterialTheme(
        colors = darkColors(
            surface = Color(0xFF222222),
        )
    ) {
//        Scaffold(topBar = {
//            TopAppBar(
//                title = { Text("Wargame") },
//                navigationIcon = {
//                    IconButton(onClick = {}) {
//                        Icon(
//                            imageVector = Icons.Filled.Menu,
//                            contentDescription = "Main Menu"
//                        )
//                    }
//                }
//            )
//        }) {
//
//        }

//        var sheets by remember { mutableStateListOf(Datasheet()) }

        val selectedSheet = if (state.selectedSheet < state.datasheets.size) {
            state.datasheets[state.selectedSheet]
        } else {
            null
        }

        Scaffold {
            Row(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth()
//                .background(Color(0, 0, 0))
            ) {
                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colors.surface)
//                    .background(Color(255, 0, 0))
                        .fillMaxHeight()
                        .fillMaxWidth(.25f)
                ) {

                    // center panel
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        Column {
                            // sheet options
                            Button(onClick = {}) {
                                Text("New Sheet")
                            }

                            for (sheet in state.datasheets) {
                                Text(sheet.name)
                            }
                        }
                    }
                }

                Column(
                    modifier = Modifier
//                    .background(Color(0, 255, 0))
                        .fillMaxHeight()
                        .fillMaxWidth(.67f)
                ) {
                    // center panel
                    Box(
                        modifier = Modifier
//                        .background(Color(0, 150, 0))
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        if (selectedSheet == null) {
                            // sheet preview
                            Text("Select a datasheet to see its info")
                        } else {
                            Text("Name: ${selectedSheet.name}")
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .background(MaterialTheme.colors.surface)
//                    .background(Color(0, 0, 255))
                        .fillMaxHeight()
                        .fillMaxWidth()
                ) {
                    // center panel
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth()
                            .padding(5.dp)
                    ) {
                        // sheet preview
                        Text("Statistics")
                    }
                }
            }
        }

//        var showContent by remember { mutableStateOf(false) }

//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(Res.drawable.compose_multiplatform), null)
//                    Text("Compose: $greeting")
//                }
//            }
//        }
    }
}