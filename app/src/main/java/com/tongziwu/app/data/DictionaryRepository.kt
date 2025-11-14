package com.tongziwu.app.data

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import org.json.JSONArray
import org.json.JSONObject

class DictionaryRepository {
    data class Entry(
        val char: String,
        val pinyin: List<String>,
        val radical: String,
        val strokes: Int,
        val phrases: List<String>,
        val examples: List<String>
    )

    fun getEntry(char: String): Entry? {
        val list = cache ?: emptyList()
        return list.firstOrNull { it.char == char }
    }

    companion object Loader {
        private var cache: List<Entry>? = null
        fun init(context: Context) {
            if (cache != null) return
            val input = context.assets.open("dict.json").bufferedReader().use { it.readText() }
            val arr = JSONArray(input)
            val list = mutableListOf<Entry>()
            for (i in 0 until arr.length()) {
                val o = arr.getJSONObject(i)
                val p = o.getJSONArray("pinyin")
                val phrases = o.getJSONArray("phrases")
                val examples = o.getJSONArray("examples")
                list.add(
                    Entry(
                        char = o.getString("char"),
                        pinyin = List(p.length()) { p.getString(it) },
                        radical = o.getString("radical"),
                        strokes = o.getInt("strokes"),
                        phrases = List(phrases.length()) { phrases.getString(it) },
                        examples = List(examples.length()) { examples.getString(it) }
                    )
                )
            }
            cache = list
        }
    }
}

@Composable
fun rememberDictionary(): DictionaryRepository {
    val context = LocalContext.current
    remember(context) { DictionaryRepository.init(context) }
    return DictionaryRepository()
}