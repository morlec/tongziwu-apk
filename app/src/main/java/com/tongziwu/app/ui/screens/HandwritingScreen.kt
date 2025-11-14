package com.tongziwu.app.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import com.tongziwu.app.handwriting.Recognizer

@Composable
fun HandwritingScreen(onPick: (String) -> Unit, onBack: () -> Unit) {
    val paths = remember { mutableStateListOf<List<Offset>>() }
    val candidates = remember { mutableStateListOf<String>() }
    val recognizing = remember { mutableStateOf(false) }
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text(text = "手写识字", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(12.dp))
        Canvas(modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(Color(0xFFF5F5F7), RoundedCornerShape(16.dp))
            .pointerInput(Unit) {
                coroutineScope {
                    var current = mutableListOf<Offset>()
                    awaitPointerEventScope {
                        while (true) {
                            val event = awaitPointerEvent()
                            val changes = event.changes
                            changes.forEach { c ->
                                val p = c.position
                                if (c.pressed) {
                                    current.add(p)
                                } else {
                                    if (current.isNotEmpty()) {
                                        paths.add(current.toList())
                                        current = mutableListOf()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        ) {
            paths.forEach { stroke ->
                val path = Path()
                if (stroke.isNotEmpty()) {
                    path.moveTo(stroke.first().x, stroke.first().y)
                    stroke.drop(1).forEach { p -> path.lineTo(p.x, p.y) }
                }
                drawPath(path, Color.Black, style = Stroke(width = 6f))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(onClick = {
                paths.clear()
                candidates.clear()
            }) { Text("清空") }
            Button(onClick = {
                recognizing.value = true
                candidates.clear()
                val result = Recognizer.recognize(paths.map { it.toList() })
                candidates.addAll(result)
                recognizing.value = false
            }) { Text("识别") }
            Button(onClick = onBack) { Text("返回") }
        }
        Spacer(modifier = Modifier.height(12.dp))
        if (candidates.isNotEmpty()) {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                candidates.take(6).forEach { c ->
                    Button(onClick = { onPick(c) }, modifier = Modifier.fillMaxWidth()) { Text(c) }
                }
            }
        }
    }
}