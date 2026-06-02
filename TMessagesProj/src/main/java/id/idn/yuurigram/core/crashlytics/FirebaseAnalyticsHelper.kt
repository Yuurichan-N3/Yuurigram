/**
 * This is the source code of Yuurigram for Android.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Please, be respectful and credit the original author if you use this code.
 *
 * Copyright github.com/arsLan4k1390, 2022-2026.
 */

package uz.unnarsx.yuurigram.core.crashlytics

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.analytics.FirebaseAnalytics
import org.telegram.messenger.AndroidUtilities
import org.telegram.messenger.ApplicationLoader
import org.telegram.messenger.FileLog
import org.telegram.tgnet.TLRPC
import uz.unnarsx.yuurigram.chats.helpers.ChatsHelper2
import uz.unnarsx.yuurigram.core.configs.YuurigramCoreConfig
import uz.unnarsx.yuurigram.core.configs.YuurigramDebugConfig
import uz.unnarsx.yuurigram.core.configs.YuurigramPrivacyConfig
import uz.unnarsx.yuurigram.core.helpers.CGResourcesHelper

object FirebaseAnalyticsHelper {

    private var firebaseAnalytics: FirebaseAnalytics? = null

    fun init(context: Context) {
        if (!ApplicationLoader.checkPlayServices()) return

        firebaseAnalytics = FirebaseAnalytics.getInstance(context).apply {
            val bundle = Bundle().apply {
                putString("flavor", CGResourcesHelper.getBuildType())
            }
            setDefaultEventParameters(bundle)

            setAnalyticsCollectionEnabled(YuurigramPrivacyConfig.googleAnalytics)
        }
    }

    fun onPrivacyConfigChanged(isEnabled: Boolean) {
        firebaseAnalytics?.setAnalyticsCollectionEnabled(isEnabled)

        if (YuurigramCoreConfig.isDevBuild()) {
            FileLog.e("Firebase Analytics collection: $isEnabled")
        }
    }

    fun trackEventWithEmptyBundle(eventName: String) {
        trackEvent(eventName, Bundle.EMPTY)
    }

    fun trackEvent(eventName: String, bundle: Bundle) {
        if (!YuurigramPrivacyConfig.googleAnalytics) return

        firebaseAnalytics?.let { analytics ->
            analytics.logEvent(eventName, bundle)

            if (YuurigramCoreConfig.isDevBuild()) {
                FileLog.e("отслежен ивент: $eventName $bundle")

                if (YuurigramDebugConfig.showRPCErrors) {
                    AndroidUtilities.runOnUIThread({
                        Toast.makeText(ApplicationLoader.applicationContext, eventName, Toast.LENGTH_SHORT).show()
                    }, 3000)
                }
            }
        }
    }

}