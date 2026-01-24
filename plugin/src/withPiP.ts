import {
  AndroidConfig,
  type ConfigPlugin,
  withAndroidManifest,
} from '@expo/config-plugins';

/**
 * Expo config plugin to enable Picture-in-Picture support for Android.
 * Modifies the AndroidManifest.xml to add required attributes to the main activity.
 */
export const withPiP: ConfigPlugin = (config) => {
  return withAndroidManifest(config, (cfg) => {
    const mainActivity = AndroidConfig.Manifest.getMainActivity(cfg.modResults);

    // If main activity not found, return config without throwing
    if (!mainActivity) {
      return cfg;
    }

    // Set supportsPictureInPicture attribute
    mainActivity.$['android:supportsPictureInPicture'] = 'true';

    // Merge configChanges flags
    const existingConfigChanges = mainActivity.$['android:configChanges'] || '';
    const requiredFlags = [
      'screenSize',
      'smallestScreenSize',
      'screenLayout',
      'orientation',
    ];

    // Split existing flags, filter out empty strings
    const existingFlags = existingConfigChanges
      .split('|')
      .filter((flag) => flag.trim() !== '');

    // Add missing flags while preserving existing order
    const missingFlags = requiredFlags.filter(
      (flag) => !existingFlags.includes(flag)
    );

    // Combine: existing flags first, then append missing flags
    const allFlags = [...existingFlags, ...missingFlags];

    // Set the merged configChanges
    mainActivity.$['android:configChanges'] = allFlags.join('|');

    return cfg;
  });
};
