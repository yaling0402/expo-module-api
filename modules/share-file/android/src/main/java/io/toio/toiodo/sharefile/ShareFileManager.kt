package io.toio.toiodo.sharefile

import org.json.JSONObject

class ShareFileManager private constructor() {
    interface Event {
        fun send(param: JSONObject?)
    }

    private var event: Event? = null
    val eventParams: ArrayList<JSONObject> = ArrayList()

    fun setEvent(event: Event?) {
        this.event = event
    }

    private fun addEventParam(param: JSONObject) {
        eventParams.add(param)
    }

    fun clearEventParams() {
        eventParams.clear()
    }

    fun sendEvent(param: JSONObject) {
        if (event != null) {
            event!!.send(param)
        } else {
            addEventParam(param)
        }
    }

    companion object {
        @JvmField
        val instance: ShareFileManager = ShareFileManager()
    }
}
