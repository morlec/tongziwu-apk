package com.tongziwu.app.handwriting

import androidx.compose.ui.geometry.Offset
import kotlin.math.abs

object Recognizer {
    fun recognize(strokes: List<List<Offset>>): List<String> {
        val s = strokes.sumOf { it.size }
        val seed = s % sample.size
        val primary = sample[seed]
        val neighbors = sample.shuffled().take(5)
        return listOf(primary) + neighbors
    }

    private val sample = listOf(
        "一","二","三","人","口","日","月","木","水","火","山","田","心","手","足","耳","目","头","大","小"
    )
}