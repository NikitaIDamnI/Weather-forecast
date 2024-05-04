package com.example.weatherforecastapp.presentation

import android.content.Context
import android.graphics.Color
import android.text.SpannableString
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.widget.TextView

class SettingsClickableSpan(val context: Context, private val fuz:()->Unit) : ClickableSpan() {

    override fun onClick(widget: View) {
       fuz.invoke()
    }

    override fun updateDrawState(ds: TextPaint) {
        super.updateDrawState(ds)
        // Настраиваем цвет и подчеркивание текста
        ds.color = Color.BLUE
        ds.isUnderlineText = true
    }
}

fun TextView.setSettingsClickableSpan(text: String, fuz: () -> Unit) {
    val spannableString = SpannableString(text)

    // Ищем индекс слова "settings"
    val settingsIndex = text.indexOf("settings")

    // Создаем clickableSpan для слова "settings"
    val clickableSpan = SettingsClickableSpan(context,fuz)

    // Устанавливаем clickableSpan для слова "settings"
    spannableString.setSpan(
        clickableSpan,
        settingsIndex,
        settingsIndex + "settings".length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    // Устанавливаем текст в TextView
    setText(spannableString)
    movementMethod = LinkMovementMethod.getInstance()
}

