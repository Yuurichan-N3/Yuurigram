/**
 * This is the source code of Yuurigram for Android.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Please, be respectful and credit the original author if you use this code.
 *
 * Copyright github.com/arsLan4k1390, 2022-2026.
 */

package id.idn.yuurigram.core.helpers

import android.widget.Toast
import com.google.firebase.remoteconfig.FirebaseRemoteConfig
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.telegram.messenger.AndroidUtilities
import org.telegram.messenger.ApplicationLoader
import org.telegram.messenger.BuildVars
import org.telegram.messenger.FileLog
import id.idn.yuurigram.core.configs.YuurigramCoreConfig
import id.idn.yuurigram.core.configs.YuurigramCameraConfig
import id.idn.yuurigram.core.configs.YuurigramDebugConfig
import id.idn.yuurigram.core.configs.YuurigramPrivacyConfig
import id.idn.yuurigram.misc.Constants
import kotlin.coroutines.resume

object FirebaseRemoteConfigHelper {

    private suspend fun activate(
        fetchInterval: Long
    ): Result<FirebaseRemoteConfig> = suspendCancellableCoroutine { continuation ->
        FirebaseRemoteConfig.getInstance().fetch(fetchInterval)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    FirebaseRemoteConfig
                        .getInstance()
                        .activate()
                        .addOnCompleteListener {
                            continuation.resume(Result.success(FirebaseRemoteConfig.getInstance()))
                        }
                } else {
                    continuation.resume(Result.failure(task.exception ?: Exception()))
                }
            }
            /*.addOnFailureListener { continuation.resume(Result.failure(it.cause ?: Exception())) }
            .addOnCanceledListener { continuation.resume(Result.failure(Exception())) }*/
    }

    suspend fun initRemoteConfig() = withContext(Dispatchers.IO) {
        val fetchInterval = if (YuurigramCoreConfig.isDevBuild()) 10800 else 21600 // 6 hours

        activate(fetchInterval.toLong())
            .onSuccess {
                if (it.getLong(Constants.Videomessages_Resolution) != 0L) {
                    setRoundVideoResolution(it.getLong(Constants.Videomessages_Resolution))
                }
                toggleReTgCheck(it.getBoolean(Constants.Re_Tg_Check))
                toggleNewUpdatesUI(it.getBoolean(Constants.is_new_updates_ui_available_v2))
                toggleSafeStars(it.getBoolean(Constants.allow_use_safestars))

                if (YuurigramCoreConfig.isDevBuild() || YuurigramDebugConfig.showRPCErrors) {
                    AndroidUtilities.runOnUIThread {
                        Toast.makeText(ApplicationLoader.applicationContext, "Fetch and activate succeeded", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            .onFailure {
                if (YuurigramCoreConfig.isDevBuild() || YuurigramDebugConfig.showRPCErrors) {
                    AndroidUtilities.runOnUIThread {
                        Toast.makeText(ApplicationLoader.applicationContext, "Fetch failed", Toast.LENGTH_SHORT).show()
                    }
                }
                FileLog.e(it, false)
            }
    }

    fun isFeatureEnabled(featureFlag: String?): Boolean {
        return if (ApplicationLoader.checkPlayServices()) {
            FirebaseRemoteConfig.getInstance().getBoolean(featureFlag!!)
        } else {
            false
        }
    }

    private fun setRoundVideoResolution(resolution: Long) {
        if (YuurigramCoreConfig.isDevBuild() || BuildVars.LOGS_ENABLED) {
            FileLog.d("Old videomessages resolution:" + YuurigramCameraConfig.videoMessagesResolution)
        }

        YuurigramCameraConfig.videoMessagesResolution = resolution.toInt()

        if (YuurigramCoreConfig.isDevBuild() || BuildVars.LOGS_ENABLED) {
            FileLog.d("New videomessages resolution:" + YuurigramCameraConfig.videoMessagesResolution)
        }
    }

    private fun toggleReTgCheck(enable: Boolean) {
        if (YuurigramCoreConfig.isDevBuild() || BuildVars.LOGS_ENABLED) {
            FileLog.d("Old reTg value:" + YuurigramPrivacyConfig.reTgCheck)
        }

        YuurigramPrivacyConfig.reTgCheck = enable

        if (YuurigramCoreConfig.isDevBuild() || BuildVars.LOGS_ENABLED) {
            FileLog.d("New reTg value:" + YuurigramPrivacyConfig.reTgCheck)
        }
    }

    private fun toggleNewUpdatesUI(enable: Boolean) {
        if (YuurigramCoreConfig.isDevBuild() || BuildVars.LOGS_ENABLED) {
            FileLog.d("Old updates value:" + YuurigramCoreConfig.updatesNewUI)
        }

        if (YuurigramCoreConfig.isDevBuild()) {
            YuurigramCoreConfig.updatesNewUI = true
        } else {
            YuurigramCoreConfig.updatesNewUI = enable
        }

        if (YuurigramCoreConfig.isDevBuild() || BuildVars.LOGS_ENABLED) {
            FileLog.d("New updates value:" + YuurigramCoreConfig.updatesNewUI)
        }
    }

    private fun toggleSafeStars(enable: Boolean) {
        /*if (YuurigramCoreConfig.isDevBuild() || BuildVars.LOGS_ENABLED) {
            FileLog.d("Old safeStars value:" + YuurigramCoreConfig.allowSafeStars)
        }

        YuurigramCoreConfig.allowSafeStars = enable

        if (YuurigramCoreConfig.isDevBuild() || BuildVars.LOGS_ENABLED) {
            FileLog.d("New safeStars value:" + YuurigramCoreConfig.allowSafeStars)
        }*/
    }

    /*fun getVideoMessageResolution(): Int {
        val res = if (FirebaseRemoteConfig.getInstance().getLong(Constants.Videomessages_Resolution) != 0L) {
            FirebaseRemoteConfig.getInstance().getLong(Constants.Videomessages_Resolution).toInt()
        } else {
            512
        }

        if (YuurigramConfig.isDevBuild()) {
            FileLog.d("VideoMessages resolution: $res")
        }

        return res
    }*/
}