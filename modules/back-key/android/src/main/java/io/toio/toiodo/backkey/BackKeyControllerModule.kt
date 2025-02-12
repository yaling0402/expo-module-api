package io.toio.toiodo.backkey

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition

class BackKeyControllerModule : Module() {
    private val manager = BackKeyManager.instance

    // Each module class must implement the definition function. The definition consists of components
    // that describes the module's functionality and behavior.
    // See https://docs.expo.dev/modules/module-api for more details about available components.
    override fun definition() = ModuleDefinition {
        // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
        // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
        // The module will be accessible from `requireNativeModule('BackKeyControllerModule')` in JavaScript.
        Name("BackKeyControllerModule")

        // Defines event names that the module can send to JavaScript.
        Events("onChange")

        // Defines a JavaScript synchronous function that runs the native code on the JavaScript thread.
        Function("hello") {
            "Hello BackKey! 👋"
        }

        Function("setEnabled") { enabled: Boolean ->
            manager.isEnabled = enabled
        }

        Function("getEnabled") {
            return@Function manager.isEnabled
        }

    }
}
