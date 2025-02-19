import { ConfigPlugin, withAndroidManifest } from 'expo/config-plugins';

const withCustomApplicationAttributes: ConfigPlugin = (config) => {
  config = withAndroidManifest(config, (config) => {
    // 获取 AndroidManifest.xml 的 JSON 结构
    const androidManifest = config.modResults;

    // 遍历找到 <application> 标签
    const application = androidManifest.manifest.application?.[0];
    if (!application) return config;

    // 覆盖或添加属性（此处以修改 android:allowBackup 和 android:largeHeap 为例）
    application.$ = {
        ...application.$,
        'android:icon': '@mipmap/custom_icon', // 指向自定义图标资源
        'android:roundIcon': '@mipmap/custom_round_icon',
    };

    return config;
  });

  return config;
};

export default withCustomApplicationAttributes;