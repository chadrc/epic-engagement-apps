package com.yulepanda.wargamebuilder

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Modifier

fun Modifier.fillMaxWidthPart(parts: Int): Modifier {
    return this.fillMaxWidth(1.0f / parts)
}