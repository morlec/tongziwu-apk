package com.tongziwu.app.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.tongziwu.app.ui.components.AppleCard
import com.tongziwu.app.data.DictionaryRepository

@Composable
fun QueryScreen(char: String) {
    val repo = remember { DictionaryRepository() }
    val entryState = remember { mutableStateOf<DictionaryRepository.Entry?>(null) }
    LaunchedEffect(char) {
        entryState.value = repo.getEntry(char)
    }
    Column(modifier = Modifier.fillMaxSize().padding(20.dp)) {
        Text(text = char, style = MaterialTheme.typography.headlineLarge)
        Spacer(modifier = Modifier.height(12.dp))
        val e = entryState.value
        if (e != null) {
            AppleCard(title = "拼音", content = e.pinyin.joinToString("、"))
            Spacer(modifier = Modifier.height(8.dp))
            AppleCard(title = "部首与笔画", content = "${e.radical} · ${e.strokes} 笔")
            Spacer(modifier = Modifier.height(8.dp))
            AppleCard(title = "常用词组", content = e.phrases.joinToString("、"))
            Spacer(modifier = Modifier.height(8.dp))
            AppleCard(title = "例句", content = e.examples.joinToString("\n"))
        } else {
            AppleCard(title = "未找到", content = "请尝试手写识字或更换查询内容")
        }
    }
}