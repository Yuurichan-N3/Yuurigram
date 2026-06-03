/**
 * This is the source code of Yuurigram for Android.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Please, be respectful and credit the original author if you use this code.
 *
 * Copyright github.com/arsLan4k1390, 2022-2026.
 */

package id.idn.yuurigram.preferences;

import static org.telegram.messenger.LocaleController.getString;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.View;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.ApplicationLoader;
import org.telegram.messenger.NotificationsService;
import org.telegram.messenger.R;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;
import org.telegram.ui.LaunchActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import id.idn.yuurigram.core.configs.YuurigramCoreConfig;
import id.idn.yuurigram.core.crashlytics.FirebaseAnalyticsHelper;
import id.idn.yuurigram.core.ui.CGBulletinCreator;
import id.idn.yuurigram.helpers.ui.PopupHelper;
import id.idn.yuurigram.preferences.helpers.SettingsHelper;

public class GeneralPreferencesEntry extends UniversalFragment {

    private final int springAnimationRow = 1;
    private final int actionbarCrossfadeRow = 2;
    private final int predictiveBackRow = 3;

    private final int silenceNonContactsRow = 4;
    private final int defaultNotificationIconRow = 5;
    private final int residentNotificationRow = 6;

    private final int hideStoriesRow = 7;
    private final int archiveStoriesRow = 8;

    private final int useSystemEmojiRow = 9;
    private final int useSystemFontsRow = 10;
    private final int tabledModeRow = 11;

    private final int downloadSpeedBoostRow = 12;
    private final int uploadSpeedBoostRow = 13;
    private final int slowNetworkMode = 14;

    @Override
    protected CharSequence getTitle() {
        FirebaseAnalyticsHelper.INSTANCE.trackEventWithEmptyBundle("general_preferences_screen");
        return getString(R.string.AP_Header_General);
    }

    @Override
    public View createView(Context context) {
        setMD3(true);
        return super.createView(context);
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        items.add(UItem.asHeader(getString(R.string.LiteMode)));
        items.add(UItem.asButton(springAnimationRow, getString(R.string.EP_NavigationAnimation), getSpringValue()));
        if (YuurigramCoreConfig.INSTANCE.getSpringAnimation() == YuurigramCoreConfig.ANIMATION_SPRING) {
            items.add(SettingsHelper.asSwitchCG(actionbarCrossfadeRow, getString(R.string.EP_NavigationAnimationCrossfading))
                    .setChecked(YuurigramCoreConfig.INSTANCE.getActionbarCrossfade())
            );
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
            items.add(SettingsHelper.asSwitchCG(predictiveBackRow, getString(R.string.CG_PredictiveBackAnimation))
                    .setChecked(YuurigramCoreConfig.INSTANCE.getPredictiveBack())
            );
        }
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader(getString(R.string.SettingsNotifications)));
        items.add(SettingsHelper.asSwitchCG(silenceNonContactsRow, getString(R.string.CP_SilenceNonContacts), getString(R.string.CP_SilenceNonContacts_Desc))
                .setChecked(YuurigramCoreConfig.INSTANCE.getSilenceNonContacts())
        );
        items.add(SettingsHelper.asSwitchCG(defaultNotificationIconRow, getString(R.string.AP_Old_Notification_Icon))
                .setChecked(YuurigramCoreConfig.INSTANCE.getOldNotificationIcon())
        );
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && Build.VERSION.SDK_INT < Build.VERSION_CODES.VANILLA_ICE_CREAM) {
            items.add(SettingsHelper.asSwitchCG(residentNotificationRow, getString(R.string.CG_ResidentNotification), getString(R.string.NotificationsService))
                    .setChecked(YuurigramCoreConfig.INSTANCE.getResidentNotification())
            );
        }
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader(getString(R.string.FilterStories)));
        items.add(SettingsHelper.asSwitchCG(hideStoriesRow, getString(R.string.CP_HideStories), getString(R.string.CP_HideStories_Desc))
                .setChecked(YuurigramCoreConfig.INSTANCE.getHideStories())
        );
        items.add(SettingsHelper.asTextDetail(archiveStoriesRow, R.drawable.msg_archive, getString(R.string.CP_ArchiveStories), getString(R.string.CP_ArchiveStories_Desc)));
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader(getString(R.string.LocalMiscellaneousCache)));
        items.add(SettingsHelper.asSwitchCG(useSystemEmojiRow, getString(R.string.AP_SystemEmoji))
                .setChecked(YuurigramCoreConfig.INSTANCE.getSystemEmoji())
        );
        items.add(SettingsHelper.asSwitchCG(useSystemFontsRow, getString(R.string.AP_SystemFonts))
                .setChecked(YuurigramCoreConfig.INSTANCE.getSystemFonts())
        );
        items.add(UItem.asButton(tabledModeRow, getString(R.string.AP_Tablet_Mode), getTabletModeValue()));
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader(getString(R.string.EP_Network)));
        items.add(UItem.asButton(downloadSpeedBoostRow, getString(R.string.EP_DownloadSpeedBoost), getDownloadSpeedBoostText()));
        items.add(SettingsHelper.asSwitchCG(uploadSpeedBoostRow, getString(R.string.EP_UploadloadSpeedBoost))
                .setChecked(YuurigramCoreConfig.INSTANCE.getUploadSpeedBoost())
        );
        items.add(SettingsHelper.asSwitchCG(slowNetworkMode, getString(R.string.EP_SlowNetworkMode))
                .setChecked(YuurigramCoreConfig.INSTANCE.getSlowNetworkMode())
        );
        items.add(UItem.asShadow(null));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        if (item.id == springAnimationRow) {
            ArrayList<String> configStringKeys = new ArrayList<>();
            ArrayList<Integer> configValues = new ArrayList<>();

            configStringKeys.add(getString(R.string.EP_NavigationAnimationSpring));
            configValues.add(YuurigramCoreConfig.ANIMATION_SPRING);

            configStringKeys.add(getString(R.string.EP_NavigationAnimationBezier));
            configValues.add(YuurigramCoreConfig.ANIMATION_CLASSIC);

            PopupHelper.show(configStringKeys, getString(R.string.EP_NavigationAnimation), configValues.indexOf(YuurigramCoreConfig.INSTANCE.getSpringAnimation()), getContext(), i -> {
                YuurigramCoreConfig.INSTANCE.setSpringAnimation(configValues.get(i));
                SettingsHelper.updateButtonValue(view, getSpringValue());

                listView.adapter.update(true);

                CGBulletinCreator.INSTANCE.createRestartBulletin(this);
            });
        } else if (item.id == actionbarCrossfadeRow) {
            YuurigramCoreConfig.INSTANCE.setActionbarCrossfade(!YuurigramCoreConfig.INSTANCE.getActionbarCrossfade());
            SettingsHelper.updateCheckState(view, YuurigramCoreConfig.INSTANCE.getActionbarCrossfade());

            if (YuurigramCoreConfig.INSTANCE.getActionbarCrossfade() && YuurigramCoreConfig.INSTANCE.getPredictiveBack()) {
                YuurigramCoreConfig.INSTANCE.setPredictiveBack(false);
                listView.adapter.update(true);
            }

            CGBulletinCreator.INSTANCE.createRestartBulletin(this);
        } else if (item.id == predictiveBackRow) {
            YuurigramCoreConfig.INSTANCE.setPredictiveBack(!YuurigramCoreConfig.INSTANCE.getPredictiveBack());
            SettingsHelper.updateCheckState(view, YuurigramCoreConfig.INSTANCE.getPredictiveBack());

            if (YuurigramCoreConfig.INSTANCE.getPredictiveBack() && YuurigramCoreConfig.INSTANCE.getActionbarCrossfade()) {
                YuurigramCoreConfig.INSTANCE.setActionbarCrossfade(false);
                listView.adapter.update(true);
            }

            CGBulletinCreator.INSTANCE.createRestartBulletin(this);
        } else if (item.id == silenceNonContactsRow) {
            YuurigramCoreConfig.INSTANCE.setSilenceNonContacts(!YuurigramCoreConfig.INSTANCE.getSilenceNonContacts());
            SettingsHelper.updateCheckState(view, YuurigramCoreConfig.INSTANCE.getSilenceNonContacts());
        } else if (item.id == defaultNotificationIconRow) {
            YuurigramCoreConfig.INSTANCE.setOldNotificationIcon(!YuurigramCoreConfig.INSTANCE.getOldNotificationIcon());
            SettingsHelper.updateCheckState(view, YuurigramCoreConfig.INSTANCE.getOldNotificationIcon());

            CGBulletinCreator.INSTANCE.createRestartBulletin(this);
        } else if (item.id == residentNotificationRow) {
            YuurigramCoreConfig.INSTANCE.setResidentNotification(!YuurigramCoreConfig.INSTANCE.getResidentNotification());
            SettingsHelper.updateCheckState(view, YuurigramCoreConfig.INSTANCE.getResidentNotification());

            ApplicationLoader.applicationContext.stopService(new Intent(ApplicationLoader.applicationContext, NotificationsService.class));
            ApplicationLoader.startPushService();
            CGBulletinCreator.INSTANCE.createRestartBulletin(this);
        } else if (item.id == hideStoriesRow) {
            YuurigramCoreConfig.INSTANCE.setHideStories(!YuurigramCoreConfig.INSTANCE.getHideStories());
            SettingsHelper.updateCheckState(view, YuurigramCoreConfig.INSTANCE.getHideStories());

            CGBulletinCreator.INSTANCE.createRestartBulletin(this);
        } else if (item.id == archiveStoriesRow) {
            showStoriesArchiveConfigurator();
        } else if (item.id == useSystemEmojiRow) {
            YuurigramCoreConfig.INSTANCE.setSystemEmoji(!YuurigramCoreConfig.INSTANCE.getSystemEmoji());
            SettingsHelper.updateCheckState(view, YuurigramCoreConfig.INSTANCE.getSystemEmoji());
        } else if (item.id == useSystemFontsRow) {
            YuurigramCoreConfig.INSTANCE.setSystemFonts(!YuurigramCoreConfig.INSTANCE.getSystemFonts());
            SettingsHelper.updateCheckState(view, YuurigramCoreConfig.INSTANCE.getSystemFonts());

            CGBulletinCreator.INSTANCE.createRestartBulletin(this);
        } else if (item.id == tabledModeRow) {
            showTabletModeSelector(() -> {
                SettingsHelper.updateButtonValue(view, getTabletModeValue());

                AndroidUtilities.resetTabletFlag();
                if (getParentActivity() instanceof LaunchActivity launchActivity) {
                    launchActivity.invalidateTabletMode();
                }
            });
        } else if (item.id == downloadSpeedBoostRow) {
            ArrayList<String> configStringKeys = new ArrayList<>();
            ArrayList<Integer> configValues = new ArrayList<>();

            configStringKeys.add(getString(R.string.LiteBatteryDisabled));
            configValues.add(YuurigramCoreConfig.BOOST_NONE);

            configStringKeys.add(getString(R.string.LiteBatteryEnabled));
            configValues.add(YuurigramCoreConfig.BOOST_AVERAGE);

            configStringKeys.add(getString(R.string.EP_DownloadSpeedBoostExtreme));
            configValues.add(YuurigramCoreConfig.BOOST_EXTREME);

            PopupHelper.show(configStringKeys, getString(R.string.EP_DownloadSpeedBoost), configValues.indexOf(YuurigramCoreConfig.INSTANCE.getDownloadSpeedBoost()), getContext(), i -> {
                YuurigramCoreConfig.INSTANCE.setDownloadSpeedBoost(configValues.get(i));
                SettingsHelper.updateButtonValue(view, getDownloadSpeedBoostText());

                CGBulletinCreator.INSTANCE.createRestartBulletin(this);
            });
        } else if (item.id == uploadSpeedBoostRow) {
            YuurigramCoreConfig.INSTANCE.setUploadSpeedBoost(!YuurigramCoreConfig.INSTANCE.getUploadSpeedBoost());
            SettingsHelper.updateCheckState(view, YuurigramCoreConfig.INSTANCE.getUploadSpeedBoost());

            CGBulletinCreator.INSTANCE.createRestartBulletin(this);
        } else if (item.id == slowNetworkMode) {
            YuurigramCoreConfig.INSTANCE.setSlowNetworkMode(!YuurigramCoreConfig.INSTANCE.getSlowNetworkMode());
            SettingsHelper.updateCheckState(view, YuurigramCoreConfig.INSTANCE.getSlowNetworkMode());

            CGBulletinCreator.INSTANCE.createRestartBulletin(this);
        }
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }

    private String getSpringValue()  {
        return switch (YuurigramCoreConfig.INSTANCE.getSpringAnimation()) {
            case YuurigramCoreConfig.ANIMATION_CLASSIC -> getString(R.string.EP_NavigationAnimationBezier);
            default -> getString(R.string.EP_NavigationAnimationSpring);
        };
    }

    private String getDownloadSpeedBoostText()  {
        return switch (YuurigramCoreConfig.INSTANCE.getDownloadSpeedBoost()) {
            case YuurigramCoreConfig.BOOST_NONE -> getString(R.string.LiteBatteryDisabled);
            case YuurigramCoreConfig.BOOST_AVERAGE -> getString(R.string.LiteBatteryEnabled);
            default -> getString(R.string.EP_DownloadSpeedBoostExtreme);
        };
    }

    private void showTabletModeSelector(Runnable runnable) {
        ArrayList<String> configStringKeys = new ArrayList<>();
        ArrayList<Integer> configValues = new ArrayList<>();

        configStringKeys.add(getString(R.string.QualityAuto));
        configValues.add(YuurigramCoreConfig.TABLET_MODE_AUTO);

        configStringKeys.add(getString(R.string.LiteBatteryEnabled));
        configValues.add(YuurigramCoreConfig.TABLET_MODE_ENABLE);

        configStringKeys.add(getString(R.string.LiteBatteryDisabled));
        configValues.add(YuurigramCoreConfig.TABLET_MODE_DISABLE);

        PopupHelper.show(configStringKeys, getString(R.string.AP_Tablet_Mode), configValues.indexOf(YuurigramCoreConfig.INSTANCE.getTabletMode()), getContext(), i -> {
            YuurigramCoreConfig.INSTANCE.setTabletMode(configValues.get(i));
            if (runnable != null) runnable.run();
        });
    }

    private String getTabletModeValue()  {
        return switch (YuurigramCoreConfig.INSTANCE.getTabletMode()) {
            case YuurigramCoreConfig.TABLET_MODE_ENABLE -> getString(R.string.LiteBatteryEnabled);
            case YuurigramCoreConfig.TABLET_MODE_DISABLE -> getString(R.string.LiteBatteryDisabled);
            default -> getString(R.string.QualityAuto);
        };
    }

    private void showStoriesArchiveConfigurator() {
        List<MenuItemConfig> menuItems = Arrays.asList(
                new MenuItemConfig(
                        getString(R.string.FilterContacts),
                        R.drawable.msg_contacts,
                        YuurigramCoreConfig.INSTANCE::getArchiveStoriesFromUsers,
                        () -> YuurigramCoreConfig.INSTANCE.setArchiveStoriesFromUsers(!YuurigramCoreConfig.INSTANCE.getArchiveStoriesFromUsers()),
                        false
                ),
                new MenuItemConfig(
                        getString(R.string.FilterChannels),
                        R.drawable.msg_channel,
                        YuurigramCoreConfig.INSTANCE::getArchiveStoriesFromChannels,
                        () -> YuurigramCoreConfig.INSTANCE.setArchiveStoriesFromChannels(!YuurigramCoreConfig.INSTANCE.getArchiveStoriesFromChannels()),
                        false
                )
        );

        ArrayList<String> prefTitle = new ArrayList<>();
        ArrayList<Integer> prefIcon = new ArrayList<>();
        ArrayList<Boolean> prefCheck = new ArrayList<>();
        ArrayList<Boolean> prefDivider = new ArrayList<>();
        ArrayList<Runnable> clickListener = new ArrayList<>();

        for (MenuItemConfig item : menuItems) {
            prefTitle.add(item.titleRes());
            prefIcon.add(item.iconRes());
            prefCheck.add(item.isChecked().get());
            prefDivider.add(item.divider());
            clickListener.add(item.toggle());
        }

        PopupHelper.showSwitchAlert(
                getString(R.string.CP_ArchiveStories),
                GeneralPreferencesEntry.this,
                prefTitle,
                prefIcon,
                prefCheck,
                null,
                null,
                prefDivider,
                clickListener,
                null
        );
    }

    private record MenuItemConfig(
            String titleRes,
            int iconRes,
            Supplier<Boolean> isChecked,
            Runnable toggle,
            boolean divider
    ) {

    }

}
