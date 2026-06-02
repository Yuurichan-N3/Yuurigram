/**
 * This is the source code of Yuurigram for Android.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Please, be respectful and credit the original author if you use this code.
 *
 * Copyright github.com/arsLan4k1390, 2022-2026.
 */

package uz.unnarsx.cherrygram.misc

import uz.unnarsx.cherrygram.core.configs.YuurigramCoreConfig

object Constants {

    @JvmField
    var CG_AUTHOR = "Updates: @YuurigramAPKs"

    /** CG Links start**/
    @JvmField
    var CG_CHANNEL_USERNAME = "cherrygram"
    @JvmField
    var CG_CHANNEL_URL = "https://t.me/cherrygram"

    @JvmField
    var CG_APKS_CHANNEL_USERNAME = "YuurigramAPKs"
    @JvmField
    var CG_APKS_CHANNEL_URL = "https://t.me/YuurigramAPKs"

    @JvmField
    var CG_CHAT_USERNAME = "YuurigramSupport"
    @JvmField
    var CG_CHAT_URL = "https://t.me/YuurigramSupport"

    @JvmField
    var UPDATE_APP_URL = if (YuurigramCoreConfig.isPlayStoreBuild()) "https://play.google.com/store/apps/details?id=uz.unnarsx.cherrygram" else CG_CHANNEL_URL

    @JvmField
    var CG_PRIVACY_URL = "https://arslan4k1390.github.io/cherrygram/privacy"
    @JvmField
    var CG_DONATIONS_AND_TERMS_URL = "https://arslan4k1390.github.io/cherrygram/donation-terms"

    @JvmField
    var CG_CROWDIN_URL = "https://crowdin.com/project/cherrygram"
    @JvmField
    var CG_GITHUB_URL = "https://github.com/arsLan4k1390/Yuurigram"

    @JvmField
    var CG_SAFESTARS = "https://safestars.pro/?partner=cherrygram"
    @JvmField
    var CG_SAFESTARS_RU = "https://safestars.pro/ru/?partner=cherrygram"
    /** CG Links finish**/

    const val PACKAGE_NAME = "uz.unnarsx.cherrygram"

    /** CG Chats IDs start**/
    const val Yuurigram_Owner = 282287840L // Yuurigram Owner (Arslan)
    const val Yuurigram_Channel = 1776033848L // Yuurigram Channel
    const val Yuurigram_Support = 1554776538L // Yuurigram Support Group
    const val Yuurigram_APKs = 1557718915L // Yuurigram APKs
    const val Yuurigram_Beta = 1544768810L // Yuurigram Beta APKs
    const val Yuurigram_Archive = 1719103382L // Yuurigram Archive
    /** CG Chats IDs finish**/

    /** OWNer's friends start */
    const val Yuki = 706402791L
    const val Alina = 553511970L
    const val Samir = 5710829964L
    /** OWNer's friends finish */

    /** Misc start**/
    const val CHERRY_EMOJI_ID = 5220045200780458122L // Yuurigram logo
    const val CHERRY_EMOJI_ID_BRA = 5222458839256825177L // Yuurigram logo (bra)
    const val CHERRY_EMOJI_ID_VERIFIED = 5449476181864779205L // Yuurigram Verified adaptive logo
    const val CHERRY_EMOJI_ID_VERIFIED_BRA = 5451850156318181341L // Yuurigram Verified Bra adaptive logo
    const val CHERRY_EMOJI_ID_DONATE = 5411229175971322671L // Cherry emoji with eyeglasses
    const val CHERRY_EMOJI_ID_PREMIUM = 5393391313502609448L // Cherry emoji with stars
    const val CHERRY_EMOJI_ID_PREMIUM_MOON = 5370777017904011118L // Evil moon emoji
    const val PROFILE_BACKGROUND_COLOR_ID_GREEN_BLUE = 12 // Blue-Green gradient
    const val PROFILE_BACKGROUND_COLOR_ID_RED = 14 // Red-Pink gradient
    const val REPLY_BACKGROUND_COLOR_ID = 13 // Red-Pink gradient
    /** Misc finish**/

    /** Firebase remote Config start */
    const val Videomessages_Resolution = "videomessages_resolution"
    const val Is_Donate_Screen_Available = "is_donate_screen_available"
    const val Re_Tg_Check = "re_tg_check"
    const val is_new_updates_ui_available = "is_new_updates_ui_available"
    const val is_new_updates_ui_available_v2 = "is_new_updates_ui_available_v2"
    const val allow_use_safestars = "allow_use_safestars"
    /** Firebase remote Config finish */

}