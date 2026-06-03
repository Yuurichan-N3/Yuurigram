/**
 * This is the source code of Yuurigram for Android.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Please, be respectful and credit the original author if you use this code.
 *
 * Copyright github.com/arsLan4k1390, 2022-2026.
 */

package id.idn.yuurigram.core

import id.idn.yuurigram.core.configs.YuurigramChatsConfig

// I've created this so CG features can be injected in a source file with 1 line only (maybe)
// Because manual editing of drklo's sources harms your mental health.
object CGFeatureHooks {

    fun switchNoAuthor(b: Boolean) {
        YuurigramChatsConfig.noAuthorship = b
    }

    fun switchNoCaptions(b: Boolean) {
        YuurigramChatsConfig.noCaptions = b
    }

}