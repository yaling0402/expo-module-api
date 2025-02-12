package io.toio.toiodo.backkey

import expo.modules.core.interfaces.ReactActivityLifecycleListener

class BackKeyReactActivityLifecycleListener : ReactActivityLifecycleListener {
    override fun onBackPressed(): Boolean {
        return if (BackKeyManager.instance.isEnabled) {
            super.onBackPressed()
        } else {
            false
        }
    }
}
