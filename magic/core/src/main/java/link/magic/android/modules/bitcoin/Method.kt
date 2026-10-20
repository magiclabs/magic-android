package link.magic.android.modules.bitcoin

enum class Method {
    BTC_SIGN_TRANSACTION;

    override fun toString(): String {
        return "btc_signTransaction"
    }
}
