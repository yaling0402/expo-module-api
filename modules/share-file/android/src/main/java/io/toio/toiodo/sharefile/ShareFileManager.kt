package io.toio.toiodo.sharefile

import org.json.JSONObject

class ShareFileManager private constructor() {
    private var event: ((JSONObject) -> Unit)? = null

    val eventParams: ArrayList<JSONObject> = ArrayList()

    fun setHandleEvent(handleEvent: ((JSONObject) -> Unit)?) {
        this.event = handleEvent;
    }

    private fun addEventParam(param: JSONObject) {
        eventParams.add(param)
    }

    fun clearEventParams() {
        eventParams.clear()
    }

    fun sendEvent(param: JSONObject) {
        if (event != null) {
            event!!.invoke(param)
        } else {
            addEventParam(param)
        }
    }

    companion object {
        @JvmField
        val instance: ShareFileManager = ShareFileManager()
    }
}
