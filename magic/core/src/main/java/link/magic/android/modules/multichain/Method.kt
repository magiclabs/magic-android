package link.magic.android.modules.multichain

enum class Method {
    MAGIC_GET_MULTICHAIN_PUBLIC_ADDRESS,
    MAGIC_REVEAL_KEY;

    override fun toString(): String {
        return name.lowercase()
    }
}
