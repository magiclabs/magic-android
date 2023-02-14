package link.magic.android.modules.connect

enum class Method {
    MC_WALLET,
    MC_REQUEST_USER_INFO,
    MC_DISCONNECT,
    MC_SHOW_UI,
    MC_GET_WALLET_INFO;

    override fun toString(): String {
        // Todo(Ari): Remove this special bypass when third party integrations is complete on web
        if (name == MC_SHOW_UI.toString()) {
            return "eth_requestAccounts"
        }
        return name.lowercase()
    }
}
