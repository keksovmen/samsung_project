package com.keksovmen.flowerbox

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater

object WaitDialog {

    @JvmStatic
    fun createWaitDialog(context: Context, inflater: LayoutInflater): AlertDialog {
        return AlertDialog.Builder(context)
            .setTitle(R.string.wait_process)
            .setMessage(R.string.wait_msg)
            .setCancelable(false)
            .setView(inflater.inflate(R.layout.wait_dialog, null))
            .create()
    }
}
