/**
 * This is the source code of Yuurigram for Android.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Please, be respectful and credit the original author if you use this code.
 *
 * Copyright github.com/arsLan4k1390, 2022-2026.
 */

package id.idn.yuurigram.core.helpers;

import java.util.ArrayList;

import id.idn.yuurigram.misc.Constants;

public class LocalVerificationsHelper {
    private static final ArrayList<Long> DEFAULT_VERIFY_LIST = new ArrayList<>();

    static {
        DEFAULT_VERIFY_LIST.add(Constants.Yuurigram_Channel);
        DEFAULT_VERIFY_LIST.add(Constants.Yuurigram_Support);
        DEFAULT_VERIFY_LIST.add(Constants.Yuurigram_APKs);
        DEFAULT_VERIFY_LIST.add(Constants.Yuurigram_Beta);
        DEFAULT_VERIFY_LIST.add(Constants.Yuurigram_Archive);
    }

    public static ArrayList<Long> getVerify() {
        return DEFAULT_VERIFY_LIST;
    }

}
