import { Image, StyleSheet, Platform, Button } from "react-native";

import { HelloWave } from "@/components/HelloWave";
import ParallaxScrollView from "@/components/ParallaxScrollView";
import { ThemedText } from "@/components/ThemedText";
import { ThemedView } from "@/components/ThemedView";
import LowMemoryObserverModule, {
  addLowMemoryListener,
} from "@/modules/low-memory/src/LowMemoryObserverModule";
import BackKeyControllerModule from "@/modules/back-key/src/BackKeyControllerModule";
import ShareFileObserverModule, {
  addFileReceivedListener,
} from "@/modules/share-file/src/ShareFileObserverModule";
import { useEffect } from "react";
import * as Sharing from "expo-sharing";
import dayjs from "dayjs";
import * as FileSystem from "expo-file-system";

export default function HomeScreen() {
  const handleLowMemory = (event: { level: number }) => {
    console.log("Memory Trim Level:", event.level);
  };

  const handleFileReceived = (event: { path: string; error: string }) => {
    console.log("File Received path:", event.path);
    console.log("File Received error:", event.error);
  };

  useEffect(() => {
    const lowMemorySubscription = addLowMemoryListener(handleLowMemory);
    const fileReceivedSubscription =
      addFileReceivedListener(handleFileReceived);
    LowMemoryObserverModule.start();
    ShareFileObserverModule.start();
    return () => {
      LowMemoryObserverModule.stop();
      lowMemorySubscription.remove();
      fileReceivedSubscription.remove();
    };
  }, []);

  const shareFile = async () => {
    // 检查分享功能是否可用
    const isAvailable = await Sharing.isAvailableAsync();
    if (!isAvailable) {
      alert("分享功能不可用");
      return;
    }
    const fileUri = FileSystem.documentDirectory + "example.jpg";
    console.log("file path: ", FileSystem.documentDirectory);
    try {
      // 调用 shareAsync 方法分享文件
      await Sharing.shareAsync(fileUri);
      alert("文件分享成功");
    } catch (error) {
      alert("文件分享失败：" + error);
    }
  };
  const handlePress = () => {
    console.log("Button pressed");
    // 点击事件处理逻辑
    const enable = BackKeyControllerModule.getEnabled();
    console.log("enable", enable);
  };
  return (
    <ParallaxScrollView
      headerBackgroundColor={{ light: "#A1CEDC", dark: "#1D3D47" }}
      headerImage={
        <Image
          source={require("@/assets/images/partial-react-logo.png")}
          style={styles.reactLogo}
        />
      }
    >
      <ThemedView style={styles.titleContainer}>
        <ThemedText type="title">{LowMemoryObserverModule.hello()}</ThemedText>
        <HelloWave />
      </ThemedView>
      <ThemedView style={styles.stepContainer}>
        <ThemedText type="subtitle">
          {BackKeyControllerModule.hello()}
        </ThemedText>
        <ThemedText>
          Edit{" "}
          <ThemedText type="defaultSemiBold">app/(tabs)/index.tsx</ThemedText>{" "}
          to see changes. Press{" "}
          <ThemedText type="defaultSemiBold">
            {Platform.select({
              ios: "cmd + d",
              android: "cmd + m",
              web: "F12",
            })}
          </ThemedText>{" "}
          to open developer tools.
        </ThemedText>
      </ThemedView>
      <ThemedView style={styles.stepContainer}>
        <ThemedText type="subtitle">
          {BackKeyControllerModule.setEnabled(true)}
        </ThemedText>
        <Button title="pressMe" onPress={() => handlePress()} />
        <Button title="分享文件" onPress={shareFile} />
        <ThemedText>
          Tap the Explore tab to learn more about what's included in this
          starter app.
        </ThemedText>
      </ThemedView>
      <ThemedView style={styles.stepContainer}>
        <ThemedText type="subtitle">
          {ShareFileObserverModule.hello()}
        </ThemedText>
        <ThemedText>
          When you're ready, run{" "}
          <ThemedText type="defaultSemiBold">npm run reset-project</ThemedText>{" "}
          to get a fresh <ThemedText type="defaultSemiBold">app</ThemedText>{" "}
          directory. This will move the current{" "}
          <ThemedText type="defaultSemiBold">app</ThemedText> to{" "}
          <ThemedText type="defaultSemiBold">app-example</ThemedText>.
        </ThemedText>
      </ThemedView>
    </ParallaxScrollView>
  );
}

const styles = StyleSheet.create({
  titleContainer: {
    flexDirection: "row",
    alignItems: "center",
    gap: 8,
  },
  stepContainer: {
    gap: 8,
    marginBottom: 8,
  },
  reactLogo: {
    height: 178,
    width: 290,
    bottom: 0,
    left: 0,
    position: "absolute",
  },
});
