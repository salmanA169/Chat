package com.swalif.sa.utils

import android.media.AudioAttributes
import android.media.AudioManager

val DEFAULT_AUDIO_ATTRIBUTES_SEND_MESSAGE = AudioAttributes.Builder()
    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
    .setUsage(AudioAttributes.USAGE_NOTIFICATION)
    .setLegacyStreamType(AudioManager.STREAM_NOTIFICATION)
    .build()