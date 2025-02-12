package io.toio.toiodo.memory

import android.content.ComponentCallbacks2
import android.content.Context
import android.content.res.Configuration
import expo.modules.kotlin.exception.Exceptions
import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class LowMemoryObserverModule : Module(), ComponentCallbacks2 {
    private val context: Context
        get() = appContext.reactContext ?: throw Exceptions.ReactContextLost()

    // Each module class must implement the definition function. The definition consists of components
    // that describes the module's functionality and behavior.
    // See https://docs.expo.dev/modules/module-api for more details about available components.
    override fun definition() = ModuleDefinition {
        // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
        // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
        // The module will be accessible from `requireNativeModule('LowMemoryObserverModule')` in JavaScript.
        Name("LowMemoryObserverModule")

        // Sets constant properties on the module. Can take a dictionary or a closure that returns a dictionary.
        Constants(
            "TRIM_MEMORY_UI_HIDDEN" to ComponentCallbacks2.TRIM_MEMORY_UI_HIDDEN
        )

        // Defines event names that the module can send to JavaScript.
        Events("onLowMemory")

        // Defines a JavaScript synchronous function that runs the native code on the JavaScript thread.
        Function("hello") {
            "Hello LowMemory! 👋"
        }

        Function("start") {
            if (appContext.hasActiveReactInstance) {
                registerCallback()
            }
        }

        Function("stop") {
            if (appContext.hasActiveReactInstance) {
                unRegisterCallback()
            }
        }
    }

    private fun registerCallback() {
        context.unregisterComponentCallbacks(this) // unregister
        context.registerComponentCallbacks(this) // register
    }

    private fun unRegisterCallback() {
        context.unregisterComponentCallbacks(this) // unregister
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        // nothing to do.
    }

    @Deprecated("Deprecated in Java")
    override fun onLowMemory() {
        // nothing to do.
    }

    override fun onTrimMemory(level: Int) {
        val context = appContext
        if (context.hasActiveReactInstance) {
            // send event to react-native module
            this@LowMemoryObserverModule.sendEvent(
                "onLowMemory", mapOf(
                    "level" to level
                )
            )
        }
    }
}
