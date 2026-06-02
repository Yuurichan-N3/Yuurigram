/**
 * This is the source code of Yuurigram for Android.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Please, be respectful and credit the original author if you use this code.
 *
 * Copyright github.com/arsLan4k1390, 2022-2026.
 */

package id.idn.yuurigram.core.configs

import android.app.Activity
import android.content.SharedPreferences
import org.telegram.messenger.ApplicationLoader

object YuurigramExperimentalConfig {

    private val sharedPreferences: SharedPreferences = ApplicationLoader.applicationContext.getSharedPreferences("mainconfig", Activity.MODE_PRIVATE)

}