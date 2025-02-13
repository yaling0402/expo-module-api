import { ConfigPlugin, withAndroidManifest } from 'expo/config-plugins';

const withCustomUsesFeature: ConfigPlugin = (config) => {
  config = withAndroidManifest(config, (config) => {
    const usesFeature = [
      {
        '$': {
          'android:name': 'android.hardware.camera.any',
          'android:required': 'true'
        }
      },
      {
        '$': {
          'android:name': 'android.hardware.location.gps',
          'android:required': 'false'
        }
      }
    ];

    // config.modResults.manifest['uses-feature']?.push(usesFeature)

    return config;
  });

  return config;
};

export default withCustomUsesFeature;