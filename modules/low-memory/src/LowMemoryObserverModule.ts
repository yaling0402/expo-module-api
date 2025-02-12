import { NativeModule, requireNativeModule } from "expo";

type MemoryTrimEvent = {
  level: number;
};

declare class MyMemoryTrimModule extends NativeModule<{
  onLowMemory: (event: MemoryTrimEvent) => void;
}> {}
// This call loads the native module object from the JSI.
const LowMemoryObserverModule = requireNativeModule<MyMemoryTrimModule>(
  "LowMemoryObserverModule"
);
export default LowMemoryObserverModule;

export function addLowMemoryListener(
  listener: (event: MemoryTrimEvent) => void
) {
  return LowMemoryObserverModule.addListener("onLowMemory", listener);
}
