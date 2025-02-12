package io.toio.toiodo.backkey

class BackKeyManager private constructor() {
    var isEnabled: Boolean = false

    companion object {
        @JvmField
        val instance: BackKeyManager = BackKeyManager()
    }
}
