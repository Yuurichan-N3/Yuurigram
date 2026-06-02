/**
 * This is the source code of Yuurigram for Android.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Please, be respectful and credit the original author if you use this code.
 *
 * Copyright github.com/arsLan4k1390, 2022-2026.
 */

package uz.unnarsx.yuurigram.preferences;

import static org.telegram.messenger.LocaleController.getString;

import android.content.Context;
import android.view.View;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.ActionBar.Theme;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;
import org.telegram.ui.LaunchActivity;

import java.util.ArrayList;

import uz.unnarsx.yuurigram.core.configs.YuurigramAppearanceConfig;
import uz.unnarsx.yuurigram.core.crashlytics.FirebaseAnalyticsHelper;
import uz.unnarsx.yuurigram.core.helpers.DeeplinkHelper;
import uz.unnarsx.yuurigram.core.ui.CGBulletinCreator;
import uz.unnarsx.yuurigram.helpers.ui.PopupHelper;
import uz.unnarsx.yuurigram.preferences.helpers.SettingsHelper;

public class AppearancePreferencesEntry extends UniversalFragment {

    private final int centerTitleRow = 1;
    private final int hideSearchBar = 2;
    private final int snowflakesRow = 3;

    private final int iconPackRow = 4;
    private final int oneUISwitchesRow = 5;
    private final int disableDividersRow = 6;

    private final int foldersRow = 7;
    private final int bottomTabsRow = 8;
    private final int messagesAndProfilesRow = 9;

    @Override
    protected CharSequence getTitle() {
        FirebaseAnalyticsHelper.INSTANCE.trackEventWithEmptyBundle("appearance_preferences_screen");
        return getString(R.string.AP_Header_Appearance);
    }

    @Override
    public View createView(Context context) {
        setMD3(true);
        return super.createView(context);
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        items.add(UItem.asHeader(getString(R.string.AP_Header)));
        items.add(SettingsHelper.asSwitchCG(centerTitleRow, getString(R.string.AP_CenterTitle))
                .setChecked(YuurigramAppearanceConfig.INSTANCE.getCenterTitle())
        );
        items.add(SettingsHelper.asSwitchCG(hideSearchBar, getString(R.string.AP_HideSearchBar))
                .setChecked(YuurigramAppearanceConfig.INSTANCE.getHideSearchFiled())
        );
        items.add(SettingsHelper.asSwitchCG(snowflakesRow, getString(R.string.CP_Snowflakes_Header))
                .setChecked(YuurigramAppearanceConfig.INSTANCE.getDrawSnowInActionBar())
        );
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader(getString(R.string.AP_Header_Appearance)));
        items.add(UItem.asButton(iconPackRow, getString(R.string.AP_IconReplacements), getIconPackValueText()));
        items.add(SettingsHelper.asSwitchCG(oneUISwitchesRow, getString(R.string.AP_OneUI_Switch_Style))
                .setChecked(YuurigramAppearanceConfig.INSTANCE.getOneUI_SwitchStyle())
        );
        items.add(SettingsHelper.asSwitchCG(disableDividersRow, getString(R.string.AP_DisableDividers))
                .setChecked(YuurigramAppearanceConfig.INSTANCE.getDisableDividers())
        );
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader(getString(R.string.LocalMiscellaneousCache)));
        items.add(UItem.asButton(foldersRow, R.drawable.msg_folders, getString(R.string.CP_Filters_Header)));
        items.add(UItem.asButton(bottomTabsRow, R.drawable.tabs_reorder, getString(R.string.CP_MainTabs_Header)));
        items.add(UItem.asButton(messagesAndProfilesRow, R.drawable.msg_customize, getString(R.string.CP_ProfileReplyBackground)));
        items.add(UItem.asShadow(null));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        if (item.id == centerTitleRow) {
            YuurigramAppearanceConfig.INSTANCE.setCenterTitle(!YuurigramAppearanceConfig.INSTANCE.getCenterTitle());
            SettingsHelper.updateCheckState(view, YuurigramAppearanceConfig.INSTANCE.getCenterTitle());

            getParentLayout().rebuildAllFragmentViews(true, true);
        } else  if (item.id == hideSearchBar) {
            YuurigramAppearanceConfig.INSTANCE.setHideSearchFiled(!YuurigramAppearanceConfig.INSTANCE.getHideSearchFiled());
            SettingsHelper.updateCheckState(view, YuurigramAppearanceConfig.INSTANCE.getHideSearchFiled());

            getNotificationCenter().postNotificationName(NotificationCenter.cgUpdateSearchFiledVisibility);
        } else if (item.id == snowflakesRow) {
            YuurigramAppearanceConfig.INSTANCE.setDrawSnowInActionBar(!YuurigramAppearanceConfig.INSTANCE.getDrawSnowInActionBar());
            SettingsHelper.updateCheckState(view, YuurigramAppearanceConfig.INSTANCE.getDrawSnowInActionBar());

            CGBulletinCreator.INSTANCE.createRestartBulletin(this);
        } else if (item.id == iconPackRow) {
            ArrayList<String> configStringKeys = new ArrayList<>();
            ArrayList<Integer> configValues = new ArrayList<>();

            configStringKeys.add(getString(R.string.Default));
            configValues.add(YuurigramAppearanceConfig.ICON_REPLACE_NONE);

            configStringKeys.add(getString(R.string.AP_IconReplacement_Solar));
            configValues.add(YuurigramAppearanceConfig.ICON_REPLACE_SOLAR);

            PopupHelper.show(configStringKeys, getString(R.string.AP_IconReplacements), configValues.indexOf(YuurigramAppearanceConfig.INSTANCE.getIconReplacement()), getContext(), i -> {
                YuurigramAppearanceConfig.INSTANCE.setIconReplacement(configValues.get(i));
                SettingsHelper.updateButtonValue(view, getIconPackValueText());

                if (getParentActivity() instanceof LaunchActivity) {
                    ((LaunchActivity) getParentActivity()).reloadResources();
                }
                Theme.reloadAllResources(getParentActivity());

                getParentLayout().rebuildAllFragmentViews(false, false);
            });
        } else if (item.id == oneUISwitchesRow) {
            YuurigramAppearanceConfig.INSTANCE.setOneUI_SwitchStyle(!YuurigramAppearanceConfig.INSTANCE.getOneUI_SwitchStyle());
            SettingsHelper.updateCheckState(view, YuurigramAppearanceConfig.INSTANCE.getOneUI_SwitchStyle());

            listView.adapter.update(true);
        } else if (item.id == disableDividersRow) {
            YuurigramAppearanceConfig.INSTANCE.setDisableDividers(!YuurigramAppearanceConfig.INSTANCE.getDisableDividers());
            SettingsHelper.updateCheckState(view, YuurigramAppearanceConfig.INSTANCE.getDisableDividers());

            Theme.applyCommonTheme();
            listView.adapter.update(true);
        }  else if (item.id == foldersRow) {
            YuurigramPreferencesNavigator.INSTANCE.createFoldersPrefs(this);
        } else if (item.id == bottomTabsRow) {
            YuurigramPreferencesNavigator.INSTANCE.createTabs(this);
        } else if (item.id == messagesAndProfilesRow) {
            YuurigramPreferencesNavigator.INSTANCE.createMessagesAndProfiles(this);
        }
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        if (item.id == foldersRow) {
            AndroidUtilities.addToClipboard("tg://" + DeeplinkHelper.DeepLinksRepo.CG_Folders);
            return true;
        } else if (item.id == bottomTabsRow) {
            AndroidUtilities.addToClipboard("tg://" + DeeplinkHelper.DeepLinksRepo.CG_Tabs);
            return true;
        } else if (item.id == messagesAndProfilesRow) {
            AndroidUtilities.addToClipboard("tg://" + DeeplinkHelper.DeepLinksRepo.CG_Messages_And_Profiles);
            return true;
        }
        return false;
    }

    private String getIconPackValueText()  {
        return switch (YuurigramAppearanceConfig.INSTANCE.getIconReplacement()) {
            case YuurigramAppearanceConfig.ICON_REPLACE_SOLAR -> getString(R.string.AP_IconReplacement_Solar);
            default -> getString(R.string.Default);
        };
    }

}
