package com.example.weatherforecastapp.data.gps

import android.app.AlertDialog
import android.content.Context

object DialogManager {
    fun locationSettingsDialog(context: Context, listener: Listener) {

        val builder = AlertDialog.Builder(context )
        val dialog = builder.create()
        dialog.setTitle("Enable location?")
        dialog.setMessage("Location disabled, do you want enable location?")
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK") { _, _ ->
            listener.onClickPositive()
            dialog.dismiss()
        }
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel") { _, _ ->
            listener.onClickNegative()
            dialog.dismiss()
        }
        dialog.show()

    }

    interface Listener {
        fun onClickPositive()
        fun onClickNegative()

    }
}