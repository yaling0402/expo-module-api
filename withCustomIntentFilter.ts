import {
  AndroidConfig,
  ConfigPlugin,
  withAndroidManifest,
} from "expo/config-plugins";

const withCustomIntentFilter: ConfigPlugin = (config) => {
  config = withAndroidManifest(config, (config) => {
    const mainActivity = AndroidConfig.Manifest.getMainActivityOrThrow(
      config.modResults
    );
    // 添加自定义 intent-filter
    const intentFilter1 = {
      action: [{ $: { "android:name": "android.intent.action.SEND" } }],
      category: [{ $: { "android:name": "android.intent.category.DEFAULT" } }],
      data: [
        { $: { "android:mimeType": "application/octet-stream" } },
        { $: { "android:mimeType": "application/x-zip" } },
      ],
    };
    const intentFilter2 = {
      action: [{ $: { "android:name": "android.intent.action.VIEW" } }],
      category: [{ $: { "android:name": "android.intent.category.DEFAULT" } }],
      data: [
        { $: { "android:mimeType": "application/octet-stream" } },
        { $: { "android:mimeType": "application/x-zip" } },
      ],
    };
    const intentFilter3 = {
      action: [{ $: { "android:name": "android.intent.action.VIEW" } }],
      category: [
        { $: { "android:name": "android.intent.category.DEFAULT" } },
        { $: { "android:name": "android.intent.category.BROWSABLE" } },
      ],
      data: [
        {
          $: {
            "android:scheme": "https",
            "android:host": "do.toio.io",
            "android:pathPrefix": "/r",
          },
        },
      ],
    };
    const intentFilter4 = {
      action: [{ $: { "android:name": "android.intent.action.VIEW" } }],
      category: [
        { $: { "android:name": "android.intent.category.DEFAULT" } },
        { $: { "android:name": "android.intent.category.BROWSABLE" } },
      ],
      data: [
        {
          $: {
            "android:scheme": "https",
            "android:host": "toio.page.link",
          },
        },
      ],
    };
    // 将 intent-filter 添加到 MainActivity
    if (mainActivity) {
      if (!mainActivity["intent-filter"]) {
        mainActivity["intent-filter"] = [];
      }
      mainActivity["intent-filter"].push(
        intentFilter1,
        intentFilter2,
        intentFilter3,
        intentFilter4
      );
    }

    return config;
  });

  return config;
};

export default withCustomIntentFilter;
