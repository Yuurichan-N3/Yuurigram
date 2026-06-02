/**
 * This is the source code of Yuurigram for Android.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Please, be respectful and credit the original author if you use this code.
 *
 * Copyright github.com/arsLan4k1390, 2022-2026.
 */

package id.idn.yuurigram.chats

import android.media.MediaRecorder
import id.idn.yuurigram.core.configs.YuurigramDebugConfig

object AudioEnhance {

    fun getAudioSource(): Int = when (YuurigramDebugConfig.audioSource) {
        YuurigramDebugConfig.AUDIO_SOURCE_CAMCORDER -> MediaRecorder.AudioSource.CAMCORDER
        YuurigramDebugConfig.AUDIO_SOURCE_MIC -> MediaRecorder.AudioSource.MIC
        YuurigramDebugConfig.AUDIO_SOURCE_REMOTE_SUBMIX -> MediaRecorder.AudioSource.REMOTE_SUBMIX
        YuurigramDebugConfig.AUDIO_SOURCE_UNPROCESSED -> MediaRecorder.AudioSource.UNPROCESSED // Api 24
        YuurigramDebugConfig.AUDIO_SOURCE_VOICE_CALL -> MediaRecorder.AudioSource.VOICE_CALL
        YuurigramDebugConfig.AUDIO_SOURCE_VOICE_COMMUNICATION -> MediaRecorder.AudioSource.VOICE_COMMUNICATION
        YuurigramDebugConfig.AUDIO_SOURCE_VOICE_DOWNLINK -> MediaRecorder.AudioSource.VOICE_DOWNLINK
        YuurigramDebugConfig.AUDIO_SOURCE_VOICE_PERFORMANCE -> MediaRecorder.AudioSource.VOICE_PERFORMANCE // Api 29
        YuurigramDebugConfig.AUDIO_SOURCE_VOICE_RECOGNITION -> MediaRecorder.AudioSource.VOICE_RECOGNITION
        YuurigramDebugConfig.AUDIO_SOURCE_VOICE_UPLINK -> MediaRecorder.AudioSource.VOICE_UPLINK
        else -> MediaRecorder.AudioSource.DEFAULT
    }

}