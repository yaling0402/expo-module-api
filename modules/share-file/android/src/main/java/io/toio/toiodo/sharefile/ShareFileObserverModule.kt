package io.toio.toiodo.sharefile

import expo.modules.kotlin.modules.Module
import expo.modules.kotlin.modules.ModuleDefinition
import org.json.JSONObject

class ShareFileObserverModule : Module() {
    private var manager = ShareFileManager.instance

    // Each module class must implement the definition function. The definition consists of components
    // that describes the module's functionality and behavior.
    // See https://docs.expo.dev/modules/module-api for more details about available components.
    override fun definition() = ModuleDefinition {
        // Sets the name of the module that JavaScript code will use to refer to the module. Takes a string as an argument.
        // Can be inferred from module's class name, but it's recommended to set it explicitly for clarity.
        // The module will be accessible from `requireNativeModule('ShareFileObserverModule')` in JavaScript.
        Name("ShareFileObserverModule")

        // Defines event names that the module can send to JavaScript.
        Events("onFileReceived")

        // Defines a JavaScript synchronous function that runs the native code on the JavaScript thread.
        Function("hello") {
            "Hello ShareFile! 👋"
        }

        Function("start") {
            for (param in manager.eventParams) {
                sendEvent(param)
            }
            manager.clearEventParams()
//            manager.setEvent{sendEvent}
        }

        Function("stop") {
            manager.setEvent(null)
        }
    }

    private fun sendEvent(param: JSONObject?) {
        if (param != null) {
            if (appContext.hasActiveReactInstance) {
                // send event to react-native module
                this@ShareFileObserverModule.sendEvent(
                    "onFileReceived", mapOf(
                        "param" to param
                    )
                )
            }
        }
    }
}
