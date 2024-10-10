package com.yulepanda.wargamebuilder

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import kotlin.math.max
import kotlin.math.round

@Composable
fun StatisticsPanel(
    state: AppState,
    model: AppViewModel,
    sheet: Datasheet?,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text("Statistics")

        if (sheet == null) {
            Text("Statistics")
            return
        }

        val ratingBase = 30
        val weaponRatings: MutableList<Pair<String, Double>> = mutableListOf()

        for (weapon in sheet.statTable.weapons) {
            val attackAvg = weapon.attacks.map(Int::toDouble).average()
            val damageAvg = weapon.damage.map(Int::toDouble).average()
            val toHitAvg = weapon.toHit.map(Int::toDouble).average()

            val hitChance = 1 - ((toHitAvg - 1) / 6)
            val successfulHits = sheet.modelCount * attackAvg * hitChance
            val avgDamage = damageAvg * successfulHits
            val rating = avgDamage / ratingBase

            weaponRatings.add(Pair(weapon.name, rating))
        }

        val toSaveAvg = sheet.statTable.toSave.map(Int::toDouble).average()
        val toResistAvg = sheet.statTable.toResist.map{ it ?: 7 }.map(Int::toDouble).average()
        val hardnessAvg = sheet.statTable.hardness.map{ it ?: 0 }.map(Int::toDouble).average()
        val totalHealth = (sheet.modelCount * sheet.health).toDouble()

        val failedSaveChance = (toSaveAvg - 1) / 6
        val failedResistChance = max(toResistAvg - 1.0, 0.0) / 6

        val damagesPerHit = arrayOf(1, 2, 3, 5, 6, 10)

        val survivalRatings = damagesPerHit.map {
            val hits = ratingBase / it
            val avgFailedSaves = round(hits * failedSaveChance)
            val avgDamageTaken = it * avgFailedSaves
            val minusHardness = avgDamageTaken - hardnessAvg
            val damageAfterResist = failedResistChance * minusHardness
            damageAfterResist / totalHealth
        }

        val avgSurvivalRating = survivalRatings.average()

        Column(modifier = Modifier.fillMaxWidth()) {
            Text("Summary")
            DataRow("Avg. To Save", formatStatDouble(toSaveAvg))
            DataRow("Avg. To Resist", formatStatDouble(toResistAvg))
            DataRow("Avg. Hardness", formatStatDouble(hardnessAvg))
            DataRow("Total Health", totalHealth.toInt().toString())
            DataRow("Fail Save Chance", formatStatDouble(failedSaveChance))
            DataRow("Fail Resist Chance", formatStatDouble(failedResistChance))
        }

        Column {
            Text("30 Damage Ratings")
            DataRow("Defense", formatStatDouble(avgSurvivalRating))
            weaponRatings.forEach {
                DataRow(it.first, formatStatDouble(it.second))
            }
        }
    }
}

@Composable
fun DataRow(label: String, data: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.fillMaxWidth(.5f)) {
            Text(label)
        }
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.End
        ) {
            Text(data, textAlign = TextAlign.Right)
        }
    }
}

fun formatStatDouble(value: Double): String {
    return if (value.isFinite()) formatDouble(value) else "N/A"
}