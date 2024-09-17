package link.magic.android.modules.events

enum class Method {
    MAGIC_INTERMEDIARY_EVENT;

    override fun toString(): String {
        return name.lowercase()
    }
}
