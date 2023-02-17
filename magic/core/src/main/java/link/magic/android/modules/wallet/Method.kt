package link.magic.android.modules.wallet

enum class Method {
    MC_LOGIN,
    MC_WALLET,
    MC_REQUEST_USER_INFO,
    MC_DISCONNECT,
    MC_GET_WALLET_INFO;

    override fun toString(): String {
        if (name.equals(MC_LOGIN)) {
            return "eth_requestAccounts"
        }
        return name.lowercase()
    }
}
