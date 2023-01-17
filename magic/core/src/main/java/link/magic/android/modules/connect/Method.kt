package link.magic.android.modules.connect

enum class Method {
    MC_WALLET,
    MC_REQUEST_USER_INFO,
    MC_DISCONNECT,
    MC_GET_WALLET_INFO;

    override fun toString(): String {
        return name.lowercase()
    }
}
