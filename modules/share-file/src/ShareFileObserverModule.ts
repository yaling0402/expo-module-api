import { NativeModule, requireNativeModule } from "expo";
type FileReceivedEvent = {
  path: string;
  error: string;
};

declare class MyFileReceivedModule extends NativeModule<{
  onFileReceived: (event: FileReceivedEvent) => void;
}> {}

const ShareFileObserverModule = requireNativeModule<MyFileReceivedModule>(
  "ShareFileObserverModule"
);
// This call loads the native module object from the JSI.
export default ShareFileObserverModule;

export function addFileReceivedListener(
  listener: (event: FileReceivedEvent) => void
) {
  return ShareFileObserverModule.addListener("onFileReceived", listener);
}
