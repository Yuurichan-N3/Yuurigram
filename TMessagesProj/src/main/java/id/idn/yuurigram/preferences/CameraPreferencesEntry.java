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
import android.os.Build;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Size;
import android.view.View;

import androidx.camera.video.Quality;

import org.telegram.messenger.R;
import org.telegram.ui.Components.UItem;
import org.telegram.ui.Components.UniversalAdapter;
import org.telegram.ui.Components.UniversalFragment;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import uz.unnarsx.cherrygram.camera.CameraTypeSelector;
import uz.unnarsx.cherrygram.camera.CameraXUtils;
import uz.unnarsx.cherrygram.core.configs.YuurigramCameraConfig;
import uz.unnarsx.cherrygram.core.configs.YuurigramCoreConfig;
import uz.unnarsx.cherrygram.core.crashlytics.FirebaseAnalyticsHelper;
import uz.unnarsx.cherrygram.core.helpers.CGResourcesHelper;
import uz.unnarsx.cherrygram.core.ui.CGBulletinCreator;
import uz.unnarsx.cherrygram.donates.DonatesManager;
import uz.unnarsx.cherrygram.helpers.ui.PopupHelper;
import uz.unnarsx.cherrygram.preferences.helpers.SettingsHelper;

public class CameraPreferencesEntry extends UniversalFragment {

    private final int cameraTypeSelectorRow = 0;

    private final int disableAttachCameraRow = 1;
    private final int cameraAspectRatioRow = 2;

    private final int cameraUseDualCameraRow = 3;
    private final int rearCamRow = 4;
    private final int startFromUltraWideRow = 5;
    private final int cameraStabilisationRow = 6;
    private final int cameraXQualityRow = 7;
    private final int cameraXFpsRangeRow = 8;

    private final int exposureSliderRow = 9;
    private final int cameraControlButtonsRow = 10;

    @Override
    protected CharSequence getTitle() {
        FirebaseAnalyticsHelper.INSTANCE.trackEventWithEmptyBundle("camera_preferences_screen");
        return getString(R.string.CP_Category_Camera);
    }

    @Override
    public View createView(Context context) {
        setMD3(true);
        return super.createView(context);
    }

    @Override
    protected void fillItems(ArrayList<UItem> items, UniversalAdapter adapter) {
        if (CameraXUtils.isCameraXSupported()) {
            items.add(UItem.asHeader(getString(R.string.CP_CameraType)));
            items.add(SettingsHelper.asCustomWithBackground(new CameraTypeSelector(getContext()) {
                @Override
                protected void onSelectedCamera(int cameraSelected) {
                    super.onSelectedCamera(cameraSelected);

                    YuurigramCameraConfig.INSTANCE.setCameraType(cameraSelected);

                    listView.adapter.update(true);
                }
            }));
            items.add(UItem.asShadow(getCameraAdvise()));
        }

        items.add(UItem.asHeader(getString(R.string.CP_Category_Camera)));
        if (YuurigramCoreConfig.isDevBuild()) {
            items.add(SettingsHelper.asSwitchCG(disableAttachCameraRow, getString(R.string.CP_DisableCam), getString(R.string.CP_DisableCam_Desc))
                    .setChecked(YuurigramCameraConfig.INSTANCE.getDisableAttachCamera())
            );
        }
        if (YuurigramCameraConfig.INSTANCE.getCameraType() != YuurigramCameraConfig.CAMERA_2) {
            items.add(UItem.asButton(cameraAspectRatioRow, getString(R.string.CP_CameraAspectRatio), getCameraAspectRatio()));
        }
        items.add(UItem.asShadow(null));

        items.add(UItem.asHeader(getString(R.string.CP_Header_Videomessages)));
        if (YuurigramCameraConfig.INSTANCE.getCameraType() == YuurigramCameraConfig.CAMERA_2 || CameraXUtils.isCurrentCameraCameraX()) {
            items.add(SettingsHelper.asSwitchCG(cameraUseDualCameraRow, getString(R.string.CP_CameraDualCamera), getString(R.string.CP_CameraDualCamera_Desc))
                    .setChecked(YuurigramCameraConfig.INSTANCE.getUseDualCamera())
            );
        }
        if (!(YuurigramCameraConfig.INSTANCE.getCameraType() == YuurigramCameraConfig.CAMERA_2 && YuurigramCameraConfig.INSTANCE.getUseDualCamera())) {
            items.add(SettingsHelper.asSwitchCG(rearCamRow, getString(R.string.CP_RearCam), getString(R.string.CP_RearCam_Desc))
                    .setChecked(YuurigramCameraConfig.INSTANCE.getRearCam())
            );
        }
        if (CameraXUtils.isCurrentCameraCameraX()) {

            items.add(SettingsHelper.asSwitchCG(startFromUltraWideRow, getString(R.string.CP_CameraUW), getString(R.string.CP_CameraUW_Desc))
                    .setChecked(YuurigramCameraConfig.INSTANCE.getStartFromUltraWideCam())
            );

            items.add(SettingsHelper.asSwitchCG(cameraStabilisationRow, getString(R.string.CP_CameraStabilisation))
                    .setChecked(YuurigramCameraConfig.INSTANCE.getCameraStabilisation())
            );
            items.add(UItem.asButton(cameraXQualityRow, getString(R.string.CP_CameraQuality), YuurigramCameraConfig.INSTANCE.getCameraResolution() + "p"));
            items.add(UItem.asButton(cameraXFpsRangeRow, "FPS", getCameraXFpsRange()));
        }
        items.add(UItem.asShadow(null));
        if (CameraXUtils.isCurrentCameraCameraX()) {
            items.add(UItem.asButton(exposureSliderRow, getString(R.string.CP_ExposureSliderPosition), getExposureSliderPosition()));
        }
        items.add(SettingsHelper.asSwitchCG(cameraControlButtonsRow, getString(R.string.CP_CenterCameraControlButtons), getString(R.string.CP_CenterCameraControlButtons_Desc))
                .setChecked(YuurigramCameraConfig.INSTANCE.getCenterCameraControlButtons())
        );
        items.add(UItem.asShadow(null));
    }

    @Override
    protected void onClick(UItem item, View view, int position, float x, float y) {
        if (item.id == disableAttachCameraRow) {
            YuurigramCameraConfig.INSTANCE.setDisableAttachCamera(!YuurigramCameraConfig.INSTANCE.getDisableAttachCamera());
            SettingsHelper.updateCheckState(view, YuurigramCameraConfig.INSTANCE.getDisableAttachCamera());

            CGBulletinCreator.INSTANCE.createRestartBulletin(this);
        } else if (item.id == cameraAspectRatioRow) {
            showAspectRatioSelector(getContext(), () -> SettingsHelper.updateButtonValue(view, getCameraAspectRatio()));
        } else if (item.id == cameraUseDualCameraRow) {
            YuurigramCameraConfig.INSTANCE.setUseDualCamera(!YuurigramCameraConfig.INSTANCE.getUseDualCamera());
            SettingsHelper.updateCheckState(view, YuurigramCameraConfig.INSTANCE.getUseDualCamera());

            if (CameraXUtils.isCurrentCameraCameraX() && YuurigramCameraConfig.INSTANCE.getUseDualCamera()) {
                YuurigramCameraConfig.INSTANCE.setCameraAspectRatio(YuurigramCameraConfig.Camera4to3);
                listView.adapter.update(true);
            }

            if (CameraXUtils.isCurrentCameraNotCameraX()) listView.adapter.update(true);
        } else if (item.id == rearCamRow) {
            YuurigramCameraConfig.INSTANCE.setRearCam(!YuurigramCameraConfig.INSTANCE.getRearCam());
            SettingsHelper.updateCheckState(view, YuurigramCameraConfig.INSTANCE.getRearCam());
        } else if (item.id == startFromUltraWideRow) {
            YuurigramCameraConfig.INSTANCE.setStartFromUltraWideCam(!YuurigramCameraConfig.INSTANCE.getStartFromUltraWideCam());
            SettingsHelper.updateCheckState(view, YuurigramCameraConfig.INSTANCE.getStartFromUltraWideCam());
        } else if (item.id == cameraStabilisationRow) {
            YuurigramCameraConfig.INSTANCE.setCameraStabilisation(!YuurigramCameraConfig.INSTANCE.getCameraStabilisation());
            SettingsHelper.updateCheckState(view, YuurigramCameraConfig.INSTANCE.getCameraStabilisation());
        } else if (item.id == cameraXFpsRangeRow) {
            ArrayList<String> configStringKeys = new ArrayList<>();
            ArrayList<Integer> configValues = new ArrayList<>();

            configStringKeys.add("25-30");
            configValues.add(YuurigramCameraConfig.CameraXFpsRange25to30);

            configStringKeys.add("30-30");
            configValues.add(YuurigramCameraConfig.CameraXFpsRange30to30);

            if (isExtendedFpsAvailable()) {
                configStringKeys.add("30-60");
                configValues.add(YuurigramCameraConfig.CameraXFpsRange30to60);

                configStringKeys.add("60-60");
                configValues.add(YuurigramCameraConfig.CameraXFpsRange60to60);
            }

            configStringKeys.add(getString(R.string.Default));
            configValues.add(YuurigramCameraConfig.CameraXFpsRangeDefault);

            PopupHelper.show(configStringKeys, "FPS", configValues.indexOf(YuurigramCameraConfig.INSTANCE.getCameraXFpsRange()), getContext(), i -> {
                YuurigramCameraConfig.INSTANCE.setCameraXFpsRange(configValues.get(i));
                SettingsHelper.updateButtonValue(view, getCameraXFpsRange());
            });
        } else if (item.id == cameraXQualityRow) {
            Map<Quality, Size> availableSizes = CameraXUtils.getAvailableVideoSizes();
            Stream<Integer> tmp = availableSizes.values().stream().sorted(Comparator.comparingInt(Size::getWidth).reversed()).map(Size::getHeight);
            ArrayList<Integer> types = tmp.collect(Collectors.toCollection(ArrayList::new));
            ArrayList<String> arrayList = types.stream().map(p -> p + "p").collect(Collectors.toCollection(ArrayList::new));

            PopupHelper.show(arrayList, getString(R.string.CP_CameraQuality), types.indexOf(YuurigramCameraConfig.INSTANCE.getCameraResolution()), getContext(), i -> {
                YuurigramCameraConfig.INSTANCE.setCameraResolution(types.get(i));
                SettingsHelper.updateButtonValue(view, YuurigramCameraConfig.INSTANCE.getCameraResolution() + "p");
            });
        } else if (item.id == exposureSliderRow) {
            ArrayList<String> configStringKeys = new ArrayList<>();
            ArrayList<Integer> configValues = new ArrayList<>();

            /*configStringKeys.add(getString(R.string.CP_ZoomSliderPosition_Bottom));
            configValues.add(YuurigramCameraConfig.EXPOSURE_SLIDER_BOTTOM);*/

            configStringKeys.add(getString(R.string.CP_ZoomSliderPosition_Right));
            configValues.add(YuurigramCameraConfig.EXPOSURE_SLIDER_RIGHT);

            /*configStringKeys.add(getString(R.string.CP_ZoomSliderPosition_Left));
            configValues.add(YuurigramCameraConfig.EXPOSURE_SLIDER_LEFT);*/

            configStringKeys.add(getString(R.string.Disable));
            configValues.add(YuurigramCameraConfig.EXPOSURE_SLIDER_NONE);

            PopupHelper.show(configStringKeys, getString(R.string.CP_ExposureSliderPosition), configValues.indexOf(YuurigramCameraConfig.INSTANCE.getExposureSlider()), getContext(), i -> {
                YuurigramCameraConfig.INSTANCE.setExposureSlider(configValues.get(i));
                SettingsHelper.updateButtonValue(view, getExposureSliderPosition());
            });
        } else if (item.id == cameraControlButtonsRow) {
            YuurigramCameraConfig.INSTANCE.setCenterCameraControlButtons(!YuurigramCameraConfig.INSTANCE.getCenterCameraControlButtons());
            SettingsHelper.updateCheckState(view, YuurigramCameraConfig.INSTANCE.getCenterCameraControlButtons());
        }
    }

    @Override
    protected boolean onLongClick(UItem item, View view, int position, float x, float y) {
        return false;
    }

    public static void showAspectRatioSelector(Context context, Runnable runnable) {
        ArrayList<String> configStringKeys = new ArrayList<>();
        ArrayList<Integer> configValues = new ArrayList<>();

        configStringKeys.add("1:1");
        configValues.add(YuurigramCameraConfig.Camera1to1);

        configStringKeys.add("4:3");
        configValues.add(YuurigramCameraConfig.Camera4to3);

        configStringKeys.add("16:9");
        configValues.add(YuurigramCameraConfig.Camera16to9);

        if (!YuurigramCameraConfig.INSTANCE.getUseDualCamera() && CameraXUtils.isCurrentCameraCameraX()) {
            configStringKeys.add(getString(R.string.Default));
            configValues.add(YuurigramCameraConfig.CameraAspectDefault);
        }

        PopupHelper.show(configStringKeys, getString(R.string.CP_CameraAspectRatio), configValues.indexOf(YuurigramCameraConfig.INSTANCE.getCameraAspectRatio()), context, i -> {
            YuurigramCameraConfig.INSTANCE.setCameraAspectRatio(configValues.get(i));
            if (runnable != null) runnable.run();
        });
    }

    private boolean isExtendedFpsAvailable() {
        return YuurigramCoreConfig.isDevBuild() || YuurigramCoreConfig.isStandalonePremiumBuild()
                || DonatesManager.INSTANCE.checkAllDonatedAccounts() || DonatesManager.INSTANCE.checkAllDonatedAccountsForMarketplace();
    }

    public static String getCameraName() {
        return switch (YuurigramCameraConfig.INSTANCE.getCameraType()) {
            case YuurigramCameraConfig.TELEGRAM_CAMERA -> "Telegram";
            case YuurigramCameraConfig.CAMERA_X -> "CameraX";
            case YuurigramCameraConfig.CAMERA_2 -> "Camera 2 (Telegram)";
            default -> getString(R.string.CP_CameraTypeSystem);
        };
    }

    private CharSequence getCameraAdvise() {
        String advise = switch (YuurigramCameraConfig.INSTANCE.getCameraType()) {
            case YuurigramCameraConfig.TELEGRAM_CAMERA -> getString(R.string.CP_DefaultCameraDesc);
            case YuurigramCameraConfig.CAMERA_X -> getString(R.string.CP_CameraXDesc);
            case YuurigramCameraConfig.CAMERA_2 -> getString(R.string.CP_Camera2Desc);
            default -> getString(R.string.CP_SystemCameraDesc);
        };

        Spannable htmlParsed;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            htmlParsed = new SpannableString(Html.fromHtml(advise, Html.FROM_HTML_MODE_LEGACY));
        } else {
            htmlParsed = new SpannableString(Html.fromHtml(advise));
        }

        return CGResourcesHelper.INSTANCE.getUrlNoUnderlineText(htmlParsed);
    }

    private static String getCameraAspectRatio()  {
        return switch (YuurigramCameraConfig.INSTANCE.getCameraAspectRatio()) {
            case YuurigramCameraConfig.Camera1to1 -> "1:1";
            case YuurigramCameraConfig.Camera4to3 -> "4:3";
            case YuurigramCameraConfig.Camera16to9 -> "16:9";
            default -> getString(R.string.Default);
        };
    }

    private String getCameraXFpsRange() {
        return switch (YuurigramCameraConfig.INSTANCE.getCameraXFpsRange()) {
            case YuurigramCameraConfig.CameraXFpsRange25to30 -> "25-30";
            case YuurigramCameraConfig.CameraXFpsRange30to30 -> "30-30";
            case YuurigramCameraConfig.CameraXFpsRange30to60 -> "30-60";
            case YuurigramCameraConfig.CameraXFpsRange60to60 -> "60-60";
            default -> getString(R.string.Default);
        };
    }

    private String getExposureSliderPosition() {
        return switch (YuurigramCameraConfig.INSTANCE.getExposureSlider()) {
            case YuurigramCameraConfig.EXPOSURE_SLIDER_BOTTOM -> getString(R.string.CP_ZoomSliderPosition_Bottom);
            case YuurigramCameraConfig.EXPOSURE_SLIDER_RIGHT -> getString(R.string.CP_ZoomSliderPosition_Right);
            case YuurigramCameraConfig.EXPOSURE_SLIDER_LEFT -> getString(R.string.CP_ZoomSliderPosition_Left);
            default -> getString(R.string.Disable);
        };
    }

}
