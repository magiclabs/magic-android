package link.magic.android.core.relayer

interface MagicEventListener {
    fun onMagicEvent(eventType: String, data: String)
}