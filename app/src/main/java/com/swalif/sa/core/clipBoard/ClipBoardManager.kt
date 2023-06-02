package com.swalif.sa.core.clipBoard

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Build
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ClipBoardManager @Inject constructor(
    @ApplicationContext
    private val context: Context
) {

    private val clipBoardManager = context.getSystemService(ClipboardManager::class.java)

    fun copyText(text:String){
        val clip = ClipData.newPlainText("Id",text)
        clipBoardManager.setPrimaryClip(clip)

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.S_V2)
            Toast.makeText(context, "Copied", Toast.LENGTH_SHORT).show()
    }
}