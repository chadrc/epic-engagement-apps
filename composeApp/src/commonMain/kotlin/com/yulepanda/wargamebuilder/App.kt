package com.yulepanda.wargamebuilder

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App(state: AppState) {
    MaterialTheme(
        colorScheme = darkColorScheme(
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
                        .background(MaterialTheme.colorScheme.surfaceContainer)
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
                            Button(
                                shape = ButtonDefaults.shape,
                                onClick = {}
                            ) {
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
                        .background(MaterialTheme.colorScheme.surfaceContainer)
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