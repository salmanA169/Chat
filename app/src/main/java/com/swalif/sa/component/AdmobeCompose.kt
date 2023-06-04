package com.swalif.sa.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView


@Composable
fun AdmobComposable() {

    AndroidView(modifier = Modifier.fillMaxWidth(),factory = {
        AdView(it)
    }) {
        // TODO: replace it with real id
        it.adUnitId = "ca-app-pub-3940256099942544/6300978111"
        it.setAdSize(AdSize.BANNER)
        it.loadAd(AdRequest.Builder().build())
    }
}