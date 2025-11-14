package com.tongziwu.app.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tongziwu.app.ui.components.AppleCard

@Composable
fun HomeScreen(onHandwriting: () -> Unit, onSearchChar: (String) -> Unit) {
    val query = remember { mutableStateOf("") }
    val context = LocalContext.current
    Column(
        modifier = Modifier.fillMaxSize().padding(20.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "童字屋", style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(16.dp))
        AppleCard(title = "快速入口", content = "手写识字、拍照识字、搜索")
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(value = query.value, onValueChange = { query.value = it }, modifier = Modifier.fillMaxWidth(), label = { Text("搜索汉字") })
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { if (query.value.isNotBlank()) onSearchChar(query.value) }, modifier = Modifier.fillMaxWidth()) { Text("查询") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onHandwriting, modifier = Modifier.fillMaxWidth()) { Text("手写识字") }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = {
            try {
                val cls = Class.forName("com.tongziwu.ocr.OcrActivity")
                val intent = android.content.Intent(context, cls)
                context.startActivity(intent)
            } catch (_: Throwable) {}
        }, modifier = Modifier.fillMaxWidth()) { Text("拍照识字") }
    }
}