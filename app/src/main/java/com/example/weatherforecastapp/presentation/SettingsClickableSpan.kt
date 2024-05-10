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
        ds.color = Color.BLUE
        ds.isUnderlineText = true
    }
}

fun TextView.clickableSpan(text: String, index: String, fuz: () -> Unit) {
    val spannableString = SpannableString(text)

    val indexText = text.indexOf(index)

    val clickableSpan = SettingsClickableSpan(context,fuz)

    spannableString.setSpan(
        clickableSpan,
        indexText,
        indexText + index.length,
        SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
    )

    setText(spannableString)
    movementMethod = LinkMovementMethod.getInstance()
}

