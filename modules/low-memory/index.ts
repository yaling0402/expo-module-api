// Reexport the native module. On web, it will be resolved to LowMemoryObserverModule.web.ts
// and on native platforms to LowMemoryObserverModule.ts
export { default } from './src/LowMemoryObserverModule';

