package link.magic.android.core.provider

enum class Method {
    MAGIC_BOX_HEART_BEAT;

    override fun toString(): String {
        return name.lowercase()
    }
}