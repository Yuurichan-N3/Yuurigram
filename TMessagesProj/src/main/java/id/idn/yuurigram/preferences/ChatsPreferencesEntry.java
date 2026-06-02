/**
 * This is the source code of Yuurigram for Android.
 * It is licensed under GNU GPL v. 2 or later.
 * You should have received a copy of the license in this archive (see LICENSE).
 * Please, be respectful and credit the original author if you use this code.
 *
 * Copyright github.com/arsLan4k1390, 2022-2026.
 */

package uz.unnarsx.cherrygram.preferences;

import static org.telegram.messenger.LocaleController.getString;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.HapticFeedbackConstants;
import android.view.View;

import org.telegram.messenger.AndroidUtilities;
import org.telegram.messenger.MessagesController;
import org.telegram.messenger.R;
import org.telegram.messenger.UserObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.BaseFragment;
import org.telegram.ui.Cells.UserCell;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;
import org.telegram.ui.DialogsActivity;
import org.telegram.ui.LaunchActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import uz.unnarsx.cherrygram.chats.helpers.ChatsHelper2;
import uz.unnarsx.cherrygram.core.VibrateUtil;
import uz.unnarsx.cherrygram.core.configs.YuurigramChatsConfig;
import uz.unnarsx.cherrygram.core.crashlytics.FirebaseAnalyticsHelper;
import uz.unnarsx.cherrygram.core.helpers.DeeplinkHelper;
import uz.unnarsx.cherrygram.core.ui.CGBulletinCreator;
import uz.unnarsx.cherrygram.helpers.ui.PopupHelper;
import uz.unnarsx.cherrygram.preferences.helpers.AlertDialogSwitchers;
import uz.unnarsx.cherrygram.preferences.helpers.SettingsHelper;

public class ChatsPreferencesEntry extends UniversalFragment {

    private final int centerTitleRow = 1, unreadBadgeRow = 2, chatMenuShortcutsRow = 3;

    private final int customBackgroundInChatsRow = 4, snowflakesRow = 5;

    private final int hideBottomBarRow = 6, sendAsChannelButtonRow = 7, recentEmojisStickersRow = 8;

    private final int messagesPreferencesRow = 9;

    private final int customChatRow = 10;

    private final int autoQuoteRow = 11, disableSwipeToNextRow = 12, disableVibrationRow = 13;

    private final int hideKbdSliderRow = 14;

    private final int largePhotosRow = 15, playVideoOnVolumeBtnRow = 16, autoPauseVideoRow = 17;

    private final int videoSeekSliderRow = 18;

    private final int notificationSoundRow = 19, vibrateInChatsRow = 20;

    @Override
    protected CharSequence getTitle() {
        FirebaseAnalyticsHelper.INSTANCE.trackEventWithEmptyBundle("chats_preferences_screen");
        return getString(R.string.FilterChats);
    }

    @Override
    public View createView(Context context) {
        setMD3(true);
        return super.createView(context);
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        items.add(UItem.asHeader(getString(R.string.AP_Header_Appearance)));
        items.add(SettingsHelper.asSwitchCG(centerTitleRow, getString(R.string.AP_CenterTitle))
                .setChecked(YuurigramChatsConfig.INSTANCE.getCenterChatTitle())
        );
        items.add(SettingsHelper.asSwitchCG(unreadBadgeRow, getString(R.string.CP_UnreadBadgeOnBackButton), getString(R.string.CP_UnreadBadgeOnBackButton_Desc))
                .setChecked(YuurigramChatsConfig.INSTANCE.getUnreadBadgeOnBackButton())
        );
        items.add(UItem.asButton(chatMenuShortcutsRow, R.drawable.msg_list, getString(R.string.CP_ChatMenuShortcuts)));
        items.add(UItem.asShadow(null));

        items.add(SettingsHelper.asSwitchCG(customBackgroundInChatsRow, getString(R.string.CP_CustomWallpapers), getString(R.string.CP_CustomWallpapers_Desc))
                .setChecked(YuurigramChatsConfig.INSTANCE.getCustomWallpapers())
        );
        items.add(SettingsHelper.asSwitchCG(snowflakesRow, getString(R.string.CP_Snowflakes_Header))
                .setChecked(YuurigramChatsConfig.INSTANCE.getDrawSnowInChat())
        );
        items.add(UItem.asShadow(null));

        items.add(SettingsHelper.asSwitchCG(hideBottomBarRow, getString(R.string.CP_HideMuteUnmuteButton))
                .setChecked(YuurigramChatsConfig.INSTANCE.getHideMuteUnmuteButton())
        );
        items.add(SettingsHelper.asSwitchCG(sendAsChannelButtonRow, getString(R.string.CP_HideSendAsChannel), getString(R.string.CP_HideSendAsChannelDesc))
                .setChecked(YuurigramChatsConfig.INSTANCE.getHideSendAsChannel())
        );
        items.add(UItem.asButton(recentEmojisStickersRow, 0, getString(R.string.CP_Slider_RecentEmojisAndStickers)));
        items.add(UItem.asShadow(null));

        items.add(UItem.asButton(messagesPreferencesRow, R.drawable.msg_discussion, getString(R.string.MessagesSettings)));
        items.add(UItem.asShadow(null));

        items.add(SettingsHelper.asSwitchCG(customChatRow, getString(R.string.EP_CustomChat), getString(R.string.EP_CustomChat_Desc))
                .setChecked(YuurigramChatsConfig.INSTANCE.getCustomChatForSavedMessages())
        );
        if (YuurigramChatsConfig.INSTANCE.getCustomChatForSavedMessages()) {
            items.add(SettingsHelper.asCustomWithBackground(createUserCell()));
        }
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader(getString(R.string.ActionsChartTitle)));
        items.add(SettingsHelper.asSwitchCG(autoQuoteRow, getString(R.string.CP_AutoQuoteReplies), getString(R.string.CP_AutoQuoteReplies_Desc))
                .setChecked(YuurigramChatsConfig.INSTANCE.getAutoQuoteReplies())
        );
        items.add(SettingsHelper.asSwitchCG(disableSwipeToNextRow, getString(R.string.CP_DisableSwipeToNext), getString(R.string.CP_DisableSwipeToNext_Desc))
                .setChecked(YuurigramChatsConfig.INSTANCE.getDisableSwipeToNext())
        );
        items.add(SettingsHelper.asSwitchCG(disableVibrationRow, getString(R.string.CP_DisableVibration))
                .setChecked(YuurigramChatsConfig.INSTANCE.getDisableVibration())
        );
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader(getString(R.string.CP_HideKbdOnScroll)));
        items.add(
                UItem.asIntSlideView(
                        1,
                        0,
                        YuurigramChatsConfig.INSTANCE.getHideKeyboardOnScrollIntensity(),
                        10,
                        val -> val == 0 ? getString(R.string.VibrationDisabled) : String.valueOf(val),
                        YuurigramChatsConfig.INSTANCE::setHideKeyboardOnScrollIntensity
                )
        );
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader(getString(R.string.CP_Header_Record)));
        items.add(SettingsHelper.asSwitchCG(largePhotosRow, getString(R.string.EP_PhotosSize))
                .setChecked(YuurigramChatsConfig.INSTANCE.getLargePhotos())
        );
        items.add(SettingsHelper.asSwitchCG(playVideoOnVolumeBtnRow, getString(R.string.CP_PlayVideo), getString(R.string.CP_PlayVideo_Desc))
                .setChecked(YuurigramChatsConfig.INSTANCE.getPlayVideoOnVolume())
        );
        items.add(SettingsHelper.asSwitchCG(autoPauseVideoRow, getString(R.string.CP_AutoPauseVideo), getString(R.string.CP_AutoPauseVideo_Desc))
                .setChecked(YuurigramChatsConfig.INSTANCE.getAutoPauseVideo())
        );
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader(getString(R.string.CP_VideoSeekDuration)));
        items.add(
                UItem.asIntSlideView(
                        1,
                        0,
                        YuurigramChatsConfig.INSTANCE.getVideoSeekDuration(),
                        25,
                        val -> val == 0 ? getString(R.string.VibrationDisabled) : String.valueOf(val),
                        YuurigramChatsConfig.INSTANCE::setVideoSeekDuration
                )
        );
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader(getString(R.string.SettingsNotifications)));
        items.add(UItem.asButton(notificationSoundRow, getString(R.string.NotificationsSound), getNotificationSoundValue()));
        items.add(UItem.asButton(vibrateInChatsRow, getString(R.string.CP_VibrateInChats), getVibrationValue()));
        items.add(UItem.asShadow(getString(R.string.CP_VibrateInChats_Desc)));
        items.add(UItem.asShadow(null));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        if (item.id == centerTitleRow) {
            YuurigramChatsConfig.INSTANCE.setCenterChatTitle(!YuurigramChatsConfig.INSTANCE.getCenterChatTitle());
            SettingsHelper.updateCheckState(view, YuurigramChatsConfig.INSTANCE.getCenterChatTitle());

            getParentLayout().rebuildAllFragmentViews(false, false);
        } else if (item.id == unreadBadgeRow) {
            YuurigramChatsConfig.INSTANCE.setUnreadBadgeOnBackButton(!YuurigramChatsConfig.INSTANCE.getUnreadBadgeOnBackButton());
            SettingsHelper.updateCheckState(view, YuurigramChatsConfig.INSTANCE.getUnreadBadgeOnBackButton());
        } else if (item.id == chatMenuShortcutsRow) {
            showChatMenuItemsConfigurator(this);
        } else if (item.id == customBackgroundInChatsRow) {
            YuurigramChatsConfig.INSTANCE.setCustomWallpapers(!YuurigramChatsConfig.INSTANCE.getCustomWallpapers());
            SettingsHelper.updateCheckState(view, YuurigramChatsConfig.INSTANCE.getCustomWallpapers());
        } else if (item.id == snowflakesRow) {
            YuurigramChatsConfig.INSTANCE.setDrawSnowInChat(!YuurigramChatsConfig.INSTANCE.getDrawSnowInChat());
            SettingsHelper.updateCheckState(view, YuurigramChatsConfig.INSTANCE.getDrawSnowInChat());

            getParentLayout().rebuildAllFragmentViews(false, false);
        } else if (item.id == hideBottomBarRow) {
            YuurigramChatsConfig.INSTANCE.setHideMuteUnmuteButton(!YuurigramChatsConfig.INSTANCE.getHideMuteUnmuteButton());
            SettingsHelper.updateCheckState(view, YuurigramChatsConfig.INSTANCE.getHideMuteUnmuteButton());
        } else if (item.id == sendAsChannelButtonRow) {
            YuurigramChatsConfig.INSTANCE.setHideSendAsChannel(!YuurigramChatsConfig.INSTANCE.getHideSendAsChannel());
            SettingsHelper.updateCheckState(view, YuurigramChatsConfig.INSTANCE.getHideSendAsChannel());
        } else if (item.id == recentEmojisStickersRow) {
            AlertDialogSwitchers.showRecentEmojisAndStickers(this);
        } else if (item.id == messagesPreferencesRow) {
            YuurigramPreferencesNavigator.INSTANCE.createMessages(this);
        } else if (item.id == customChatRow) {
            YuurigramChatsConfig.INSTANCE.setCustomChatForSavedMessages(!YuurigramChatsConfig.INSTANCE.getCustomChatForSavedMessages());
            SettingsHelper.updateCheckState(view, YuurigramChatsConfig.INSTANCE.getCustomChatForSavedMessages());

            listView.adapter.update(true);
        } else if (item.id == autoQuoteRow) {
            YuurigramChatsConfig.INSTANCE.setAutoQuoteReplies(!YuurigramChatsConfig.INSTANCE.getAutoQuoteReplies());
            SettingsHelper.updateCheckState(view, YuurigramChatsConfig.INSTANCE.getAutoQuoteReplies());
        } else if (item.id == disableSwipeToNextRow) {
            YuurigramChatsConfig.INSTANCE.setDisableSwipeToNext(!YuurigramChatsConfig.INSTANCE.getDisableSwipeToNext());
            SettingsHelper.updateCheckState(view, YuurigramChatsConfig.INSTANCE.getDisableSwipeToNext());
        } else if (item.id == disableVibrationRow) {
            YuurigramChatsConfig.INSTANCE.setDisableVibration(!YuurigramChatsConfig.INSTANCE.getDisableVibration());
            SettingsHelper.updateCheckState(view, YuurigramChatsConfig.INSTANCE.getDisableVibration());

            CGBulletinCreator.INSTANCE.createRestartBulletin(this);
        } else if (item.id == largePhotosRow) {
            YuurigramChatsConfig.INSTANCE.setLargePhotos(!YuurigramChatsConfig.INSTANCE.getLargePhotos());
            SettingsHelper.updateCheckState(view, YuurigramChatsConfig.INSTANCE.getLargePhotos());

            CGBulletinCreator.INSTANCE.createRestartBulletin(this);
        } else if (item.id == playVideoOnVolumeBtnRow) {
            YuurigramChatsConfig.INSTANCE.setPlayVideoOnVolume(!YuurigramChatsConfig.INSTANCE.getPlayVideoOnVolume());
            SettingsHelper.updateCheckState(view, YuurigramChatsConfig.INSTANCE.getPlayVideoOnVolume());
        } else if (item.id == autoPauseVideoRow) {
            YuurigramChatsConfig.INSTANCE.setAutoPauseVideo(!YuurigramChatsConfig.INSTANCE.getAutoPauseVideo());
            SettingsHelper.updateCheckState(view, YuurigramChatsConfig.INSTANCE.getAutoPauseVideo());
        } else if (item.id == notificationSoundRow) {
            showNotificationSoundSelector(() -> {
                SettingsHelper.updateButtonValue(view, getNotificationSoundValue());

                int tone = (YuurigramChatsConfig.INSTANCE.getNotificationSound() == YuurigramChatsConfig.NOTIF_SOUND_DEFAULT) ? R.raw.sound_in : R.raw.sound_in_ios;
                try {
                    MediaPlayer mp = MediaPlayer.create(getContext(), tone);
                    mp.start();
                } catch (Exception ignored) {}

                CGBulletinCreator.INSTANCE.createRestartBulletin(this);
            });
        } else if (item.id == vibrateInChatsRow) {
            showVibrationSelector(() -> {
                try {
                    switch (YuurigramChatsConfig.INSTANCE.getVibrateInChats()) {
                        case YuurigramChatsConfig.VIBRATION_CLICK ->
                                VibrateUtil.INSTANCE.makeClickVibration();
                        case YuurigramChatsConfig.VIBRATION_WAVE_FORM ->
                                VibrateUtil.INSTANCE.makeWaveVibration();
                        case YuurigramChatsConfig.VIBRATION_KEYBOARD_TAP ->
                                VibrateUtil.INSTANCE.vibrate(HapticFeedbackConstants.KEYBOARD_TAP);
                        case YuurigramChatsConfig.VIBRATION_LONG ->
                                VibrateUtil.INSTANCE.vibrate();
                    }
                } catch (Exception ignored) { }

                SettingsHelper.updateButtonValue(view, getVibrationValue());
            });
        }
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        if (item.id == messagesPreferencesRow) {
            AndroidUtilities.addToClipboard("tg://" + DeeplinkHelper.DeepLinksRepo.CG_Messages);
            return true;
        }
        return false;
    }

    private UserCell createUserCell() {
        UserCell userCell = new UserCell(getContext(), 14, 0, false, true, getResourceProvider(), false, false);

        userCell.addButton.setText(getString(R.string.Edit));
        userCell.addButton.setOnClickListener(view1 -> {
            if (getUserConfig().getCurrentUser() == null) {
                return;
            }
            Bundle args = new Bundle();
            args.putBoolean("onlySelect", true);
            args.putBoolean("cgPrefs", true);
            args.putBoolean("allowGlobalSearch", false);
            args.putInt("dialogsType", DialogsActivity.DIALOGS_TYPE_FORWARD);
            args.putBoolean("resetDelegate", false);
            args.putBoolean("closeFragment", true);
            DialogsActivity fragment = new DialogsActivity(args);
            fragment.setDelegate((fragment1, dids, message, param, notify, scheduleDate, scheduleRepeatPeriod, topicsFragment) -> {
                long did = dids.get(0).dialogId;

                String selectedChatId = String.valueOf(did);

                SharedPreferences.Editor editor = MessagesController.getMainSettings(currentAccount).edit();
                editor.putString("CP_CustomChatIDSM", selectedChatId).apply();

                fragment.finishFragment(true);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    View avatar = userCell.avatarImageView;

                    int[] loc = new int[2];
                    avatar.getLocationOnScreen(loc);

                    float cx = loc[0] + avatar.getWidth() / 2f;
                    float cy = loc[1] + avatar.getHeight() / 2f;

                    LaunchActivity.makeRipple(cx, cy, 5f);
                }

                listView.adapter.update(false);
                return true;
            });
            presentFragment(fragment);
        });

        long chatId = ChatsHelper2.INSTANCE.getCustomChatID();

        TLRPC.Chat chat = MessagesController.getInstance(currentAccount).getChat(-chatId);
        TLRPC.User user = MessagesController.getInstance(currentAccount).getUser(chatId);

        StringBuilder status = new StringBuilder();
        status.append(getString(R.string.EP_CustomChat_Selected_Title));
        status.append(' ');
        status.append("\"");
        status.append(getString(R.string.SavedMessages));
        status.append("\".");

        if (chatId == getUserConfig().clientUserId) {
            userCell.setData("saved_cg", getString(R.string.SavedMessages), "", 0);
        } else if (chat != null) {
            userCell.setData(chat, chat.title, status, 0);
        } else {
            userCell.setData(user, UserObject.getUserName(user), status, 0);
        }

        return userCell;
    }

    public static void showChatMenuItemsConfigurator(BaseFragment fragment) {
        List<MenuItemConfig> menuItems = Arrays.asList(
                new MenuItemConfig(
                        getString(R.string.CG_JumpToBeginning),
                        R.drawable.ic_upward,
                        YuurigramChatsConfig.INSTANCE::getShortcut_JumpToBegin,
                        () -> YuurigramChatsConfig.INSTANCE.setShortcut_JumpToBegin(!YuurigramChatsConfig.INSTANCE.getShortcut_JumpToBegin()),
                        false,
                        false
                ),
                new MenuItemConfig(
                        getString(R.string.CG_DeleteAllFromSelf),
                        R.drawable.msg_delete,
                        YuurigramChatsConfig.INSTANCE::getShortcut_DeleteAll,
                        () -> YuurigramChatsConfig.INSTANCE.setShortcut_DeleteAll(!YuurigramChatsConfig.INSTANCE.getShortcut_DeleteAll()),
                        false,
                        false
                ),
                new MenuItemConfig(
                        getString(R.string.SavedMessages),
                        R.drawable.msg_saved,
                        YuurigramChatsConfig.INSTANCE::getShortcut_SavedMessages,
                        () -> YuurigramChatsConfig.INSTANCE.setShortcut_SavedMessages(!YuurigramChatsConfig.INSTANCE.getShortcut_SavedMessages()),
                        false,
                        false
                ),
                new MenuItemConfig(
                        "Telegram Browser",
                        R.drawable.msg_language,
                        YuurigramChatsConfig.INSTANCE::getShortcut_Browser,
                        () -> YuurigramChatsConfig.INSTANCE.setShortcut_Browser(!YuurigramChatsConfig.INSTANCE.getShortcut_Browser()),
                        true,
                        false
                ),
                new MenuItemConfig(
                        getString(R.string.CP_AdminActions),
                        R.drawable.msg_admins,
                        () -> false,
                        () -> showChatAdminItemsConfigurator(fragment),
                        false,
                        true
                )
        );

        handleMenuAlert(getString(R.string.CP_ChatMenuShortcuts), menuItems, fragment);
    }

    private static void showChatAdminItemsConfigurator(BaseFragment fragment) {
        List<MenuItemConfig> menuItems = Arrays.asList(
                new MenuItemConfig(
                        getString(R.string.Reactions),
                        R.drawable.msg_reactions2,
                        YuurigramChatsConfig.INSTANCE::getAdmins_Reactions,
                        () -> YuurigramChatsConfig.INSTANCE.setAdmins_Reactions(!YuurigramChatsConfig.INSTANCE.getAdmins_Reactions()),
                        false,
                        false
                ),
                new MenuItemConfig(
                        getString(R.string.ChannelPermissions),
                        R.drawable.msg_permissions,
                        YuurigramChatsConfig.INSTANCE::getAdmins_Permissions,
                        () -> YuurigramChatsConfig.INSTANCE.setAdmins_Permissions(!YuurigramChatsConfig.INSTANCE.getAdmins_Permissions()),
                        false,
                        false
                ),
                new MenuItemConfig(
                        getString(R.string.ChannelAdministrators),
                        R.drawable.msg_admins,
                        YuurigramChatsConfig.INSTANCE::getAdmins_Administrators,
                        () -> YuurigramChatsConfig.INSTANCE.setAdmins_Administrators(!YuurigramChatsConfig.INSTANCE.getAdmins_Administrators()),
                        false,
                        false
                ),
                new MenuItemConfig(
                        getString(R.string.ChannelMembers),
                        R.drawable.msg_groups,
                        YuurigramChatsConfig.INSTANCE::getAdmins_Members,
                        () -> YuurigramChatsConfig.INSTANCE.setAdmins_Members(!YuurigramChatsConfig.INSTANCE.getAdmins_Members()),
                        false,
                        false
                ),
                new MenuItemConfig(
                        getString(R.string.StatisticsAndBoosts),
                        R.drawable.msg_stats,
                        YuurigramChatsConfig.INSTANCE::getAdmins_Statistics,
                        () -> YuurigramChatsConfig.INSTANCE.setAdmins_Statistics(!YuurigramChatsConfig.INSTANCE.getAdmins_Statistics()),
                        false,
                        false
                ),
                new MenuItemConfig(
                        getString(R.string.EventLog),
                        R.drawable.msg_log,
                        YuurigramChatsConfig.INSTANCE::getAdmins_RecentActions,
                        () -> YuurigramChatsConfig.INSTANCE.setAdmins_RecentActions(!YuurigramChatsConfig.INSTANCE.getAdmins_RecentActions()),
                        false,
                        false
                )
        );

        handleMenuAlert(getString(R.string.CP_AdminActions), menuItems, fragment);
    }

    private String getNotificationSoundValue() {
        return switch (YuurigramChatsConfig.INSTANCE.getNotificationSound()) {
            case YuurigramChatsConfig.NOTIF_SOUND_DEFAULT -> getString(R.string.Default);
            case YuurigramChatsConfig.NOTIF_SOUND_IOS -> "iOS";
            default -> getString(R.string.PopupDisabled);
        };
    }

    private void showNotificationSoundSelector(Runnable runnable) {
        ArrayList<String> configStringKeys = new ArrayList<>();
        ArrayList<Integer> configValues = new ArrayList<>();

        configStringKeys.add(getString(R.string.PopupDisabled));
        configValues.add(YuurigramChatsConfig.NOTIF_SOUND_DISABLE);

        configStringKeys.add(getString(R.string.Default));
        configValues.add(YuurigramChatsConfig.NOTIF_SOUND_DEFAULT);

        configStringKeys.add("iOS");
        configValues.add(YuurigramChatsConfig.NOTIF_SOUND_IOS);

        PopupHelper.show(configStringKeys, getString(R.string.NotificationsSound), configValues.indexOf(YuurigramChatsConfig.INSTANCE.getNotificationSound()), getContext(), i -> {
            YuurigramChatsConfig.INSTANCE.setNotificationSound(configValues.get(i));
            if (runnable != null) runnable.run();
        });
    }

    private String getVibrationValue() {
        return switch (YuurigramChatsConfig.INSTANCE.getVibrateInChats()) {
            case YuurigramChatsConfig.VIBRATION_CLICK -> "1";
            case YuurigramChatsConfig.VIBRATION_WAVE_FORM -> "2";
            case YuurigramChatsConfig.VIBRATION_KEYBOARD_TAP -> "3";
            case YuurigramChatsConfig.VIBRATION_LONG -> "4";
            default -> getString(R.string.AutoLockDisabled);
        };
    }

    private void showVibrationSelector(Runnable runnable) {
        ArrayList<String> configStringKeys = new ArrayList<>();
        ArrayList<Integer> configValues = new ArrayList<>();

        configStringKeys.add(getString(R.string.AutoLockDisabled));
        configValues.add(YuurigramChatsConfig.VIBRATION_DISABLE);

        configStringKeys.add("1");
        configValues.add(YuurigramChatsConfig.VIBRATION_CLICK);

        configStringKeys.add("2");
        configValues.add(YuurigramChatsConfig.VIBRATION_WAVE_FORM);

        configStringKeys.add("3");
        configValues.add(YuurigramChatsConfig.VIBRATION_KEYBOARD_TAP);

        configStringKeys.add("4");
        configValues.add(YuurigramChatsConfig.VIBRATION_LONG);

        PopupHelper.show(configStringKeys, getString(R.string.CP_VibrateInChats), configValues.indexOf(YuurigramChatsConfig.INSTANCE.getVibrateInChats()), getContext(), i -> {
            YuurigramChatsConfig.INSTANCE.setVibrateInChats(configValues.get(i));
            if (runnable != null) runnable.run();
        });
    }

    private static void handleMenuAlert(String title, List<MenuItemConfig> items, BaseFragment fragment) {
        ArrayList<String> prefTitle = new ArrayList<>();
        ArrayList<Integer> prefIcon = new ArrayList<>();
        ArrayList<Boolean> prefCheck = new ArrayList<>();
        ArrayList<Boolean> prefCheckInvisible = new ArrayList<>();
        ArrayList<Boolean> prefDivider = new ArrayList<>();
        ArrayList<Runnable> clickListener = new ArrayList<>();

        for (MenuItemConfig item : items) {
            prefTitle.add(item.title);
            prefIcon.add(item.iconRes);
            prefCheck.add(item.isChecked.get());
            prefCheckInvisible.add(item.isCheckInvisible);
            prefDivider.add(item.divider);
            clickListener.add(item.toggle);
        }

        PopupHelper.showSwitchAlert(
                title,
                fragment,
                prefTitle,
                prefIcon,
                prefCheck,
                prefCheckInvisible,
                null,
                prefDivider,
                clickListener,
                null
        );
    }

    public static class MenuItemConfig {
        String title;
        int iconRes;
        Supplier<Boolean> isChecked;
        Runnable toggle;
        boolean divider;
        boolean isCheckInvisible;

        MenuItemConfig(String title, int iconRes, Supplier<Boolean> isChecked, Runnable toggle, boolean divider, boolean isCheckInvisible) {
            this.title = title;
            this.iconRes = iconRes;
            this.isChecked = isChecked;
            this.toggle = toggle;
            this.divider = divider;
            this.isCheckInvisible = isCheckInvisible;
        }
    }

}
