package link.magic.android.core.relayer

import link.magic.android.MagicEvent

interface MagicEventListener {
    fun onMagicEvent(eventType: MagicEvent, data: String)
}