package com.azharkova.photoram.util

import android.content.Context
import android.content.DialogInterface
import androidx.appcompat.app.AlertDialog

class Dialog {
    companion object {
        val instance = Dialog()
    }

    fun show(message: String, context: Context, hasCancel: Boolean = false ) {
        val builder = AlertDialog.Builder(context)
        builder.setMessage(message)
        builder.apply {
            setPositiveButton("OK",
                DialogInterface.OnClickListener { dialog, id ->
                    // User clicked OK button
                })
            if (hasCancel) {
                setNegativeButton("CANCEL",
                    DialogInterface.OnClickListener { dialog, id ->
                        // User cancelled the dialog
                    })
            }
        }
        builder.create().show()
    }
}
