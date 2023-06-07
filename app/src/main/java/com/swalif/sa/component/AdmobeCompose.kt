package com.swalif.sa.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import com.swalif.sa.BuildConfig


@Composable
fun AdmobComposable() {

    AndroidView(modifier = Modifier.fillMaxWidth(),factory = {
        AdView(it)
    }) {
        it.adUnitId = BuildConfig.ADMOBE_BANNER_UNIT_ID
        it.setAdSize(AdSize.BANNER)
        it.loadAd(AdRequest.Builder().build())
    }
}