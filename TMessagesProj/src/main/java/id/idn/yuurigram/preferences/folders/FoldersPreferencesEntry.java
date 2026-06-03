/**
 * This is the source code of Yuurigram for Android.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Please, be respectful and credit the original author if you use this code.
 *
 * Copyright github.com/arsLan4k1390, 2022-2026.
 */

package id.idn.yuurigram.preferences.folders;

import static org.telegram.messenger.LocaleController.getString;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.BotWebViewVibrationEffect;
import org.telegram.messenger.NotificationCenter;
import org.telegram.messenger.R;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;

import java.util.ArrayList;

import id.idn.yuurigram.core.configs.YuurigramAppearanceConfig;
import id.idn.yuurigram.core.crashlytics.FirebaseAnalyticsHelper;
import id.idn.yuurigram.core.ui.CGBulletinCreator;
import id.idn.yuurigram.donates.DonatesManager;
import id.idn.yuurigram.helpers.ui.PopupHelper;
import id.idn.yuurigram.preferences.folders.cells.FoldersPreviewCell;
import id.idn.yuurigram.preferences.helpers.SettingsHelper;

public class FoldersPreferencesEntry extends UniversalFragment {

    protected FoldersPreviewCell foldersPreviewCell;

    private final int hideAllChatsTabRow = 1;

    private final int hideCounterRow = 2;
    private final int tabIconTypeRow = 3;
    private final int addStrokeRow = 4;

    private final int folderNameAppHeaderRow = 5;
    private final int foldersAtBottomRow = 6;

    @Override
    public View createView(Context context) {
        FirebaseAnalyticsHelper.INSTANCE.trackEventWithEmptyBundle("folders_preferences_screen");
        setMD3(true);
        return super.createView(context);
    }

    @Override
    protected CharSequence getTitle() {
        return getString(R.string.CP_Filters_Header);
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        foldersPreviewCell = new FoldersPreviewCell(getContext());
        foldersPreviewCell.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT));
        items.add(SettingsHelper.asCustomWithBackground(foldersPreviewCell));
        items.add(UItem.asShadow(null));

        items.add(SettingsHelper.asSwitchCG(hideAllChatsTabRow, getString(R.string.CP_NewTabs_RemoveAllChats))
                .setChecked(YuurigramAppearanceConfig.INSTANCE.getTabsHideAllChats())
        );
        items.add(SettingsHelper.asSwitchCG(hideCounterRow, getString(R.string.CP_NewTabs_NoCounter))
                .setChecked(YuurigramAppearanceConfig.INSTANCE.getTabsNoUnread())
        );
        items.add(UItem.asButton(tabIconTypeRow, getString(R.string.AP_Tab_Style), getTabModeValue()));
        items.add(SettingsHelper.asSwitchCG(addStrokeRow, getString(R.string.AP_Tab_Style_Stroke))
                .setChecked(YuurigramAppearanceConfig.INSTANCE.getTabStyleStroke())
        );
        items.add(UItem.asShadow(null));

        items.add(SettingsHelper.asSwitchCG(folderNameAppHeaderRow, getString(R.string.AP_FolderNameInHeader), getString(R.string.AP_FolderNameInHeader_Desc))
                .setChecked(YuurigramAppearanceConfig.INSTANCE.getFolderNameInHeader())
        );
        items.add(SettingsHelper.asSwitchCG(foldersAtBottomRow, getString(R.string.AP_FoldersAtBottom))
                .setChecked(YuurigramAppearanceConfig.INSTANCE.getFoldersAtBottom()).setLocked(!DonatesManager.INSTANCE.didUserDonateForFeature())
        );
        items.add(UItem.asShadow(null));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        if (item.id == hideAllChatsTabRow) {
            YuurigramAppearanceConfig.INSTANCE.setTabsHideAllChats(!YuurigramAppearanceConfig.INSTANCE.getTabsHideAllChats());
            SettingsHelper.updateCheckState(view, YuurigramAppearanceConfig.INSTANCE.getTabsHideAllChats());

            foldersPreviewCell.updateAllChatsTabName(true);

            parentLayout.rebuildAllFragmentViews(false, false);

            getNotificationCenter().postNotificationName(NotificationCenter.dialogFiltersUpdated);
            getNotificationCenter().postNotificationName(NotificationCenter.mainUserInfoChanged);
        } else if (item.id == hideCounterRow) {
            YuurigramAppearanceConfig.INSTANCE.setTabsNoUnread(!YuurigramAppearanceConfig.INSTANCE.getTabsNoUnread());
            SettingsHelper.updateCheckState(view, YuurigramAppearanceConfig.INSTANCE.getTabsNoUnread());

            foldersPreviewCell.updateTabCounter(true);

            parentLayout.rebuildAllFragmentViews(false, false);

            getNotificationCenter().postNotificationName(NotificationCenter.dialogFiltersUpdated);
        } else if (item.id == tabIconTypeRow) {
            ArrayList<String> configStringKeys = new ArrayList<>();
            ArrayList<Integer> configValues = new ArrayList<>();

            configStringKeys.add(getString(R.string.CG_FoldersTypeIconsTitles));
            configValues.add(YuurigramAppearanceConfig.TAB_TYPE_MIX);

            configStringKeys.add(getString(R.string.CG_FoldersTypeTitles));
            configValues.add(YuurigramAppearanceConfig.TAB_TYPE_TEXT);

            configStringKeys.add(getString(R.string.CG_FoldersTypeIcons));
            configValues.add(YuurigramAppearanceConfig.TAB_TYPE_ICON);

            PopupHelper.show(configStringKeys, getString(R.string.AP_Tab_Style), configValues.indexOf(YuurigramAppearanceConfig.INSTANCE.getTabMode()), getContext(), i -> {
                YuurigramAppearanceConfig.INSTANCE.setTabMode(configValues.get(i));
                SettingsHelper.updateButtonValue(view, getTabModeValue());

                foldersPreviewCell.updateTabIcons(true);
                foldersPreviewCell.updateTabTitle(true);

                parentLayout.rebuildAllFragmentViews(false, false);

                getNotificationCenter().postNotificationName(NotificationCenter.dialogFiltersUpdated);
            });
        } else if (item.id == addStrokeRow) {
            YuurigramAppearanceConfig.INSTANCE.setTabStyleStroke(!YuurigramAppearanceConfig.INSTANCE.getTabStyleStroke());
            SettingsHelper.updateCheckState(view, YuurigramAppearanceConfig.INSTANCE.getTabStyleStroke());

            foldersPreviewCell.invalidate();
            parentLayout.rebuildAllFragmentViews(false, false);
        } else if (item.id == folderNameAppHeaderRow) {
            YuurigramAppearanceConfig.INSTANCE.setFolderNameInHeader(!YuurigramAppearanceConfig.INSTANCE.getFolderNameInHeader());
            SettingsHelper.updateCheckState(view, YuurigramAppearanceConfig.INSTANCE.getFolderNameInHeader());

            parentLayout.rebuildAllFragmentViews(false, false);

            getNotificationCenter().postNotificationName(NotificationCenter.dialogFiltersUpdated);
        } else if (item.id == foldersAtBottomRow) {
            if (!DonatesManager.INSTANCE.didUserDonateForFeature()) {
                AndroidUtilities.shakeViewSpring(view);
                BotWebViewVibrationEffect.APP_ERROR.vibrate();
                CGBulletinCreator.INSTANCE.createRequireDonateBulletin(this);
                return;
            }

            YuurigramAppearanceConfig.INSTANCE.setFoldersAtBottom(!YuurigramAppearanceConfig.INSTANCE.getFoldersAtBottom());
            SettingsHelper.updateCheckState(view, YuurigramAppearanceConfig.INSTANCE.getFoldersAtBottom());

            CGBulletinCreator.INSTANCE.createRestartBulletin(this);
        }
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }

    private String getTabModeValue() {
        return switch (YuurigramAppearanceConfig.INSTANCE.getTabMode()) {
            case YuurigramAppearanceConfig.TAB_TYPE_MIX -> getString(R.string.CG_FoldersTypeIconsTitles);
            case YuurigramAppearanceConfig.TAB_TYPE_ICON -> getString(R.string.CG_FoldersTypeIcons);
            default -> getString(R.string.CG_FoldersTypeTitles);
        };
    }

}
