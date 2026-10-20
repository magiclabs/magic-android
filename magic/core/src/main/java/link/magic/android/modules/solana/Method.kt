package link.magic.android.modules.solana

enum class Method {
    SOL_SIGN_TRANSACTION,
    SOL_SIGN_MESSAGE,
    SOL_PARTIAL_SIGN_TRANSACTION;

    override fun toString(): String {
        return when (this) {
            SOL_SIGN_TRANSACTION -> "sol_signTransaction"
            SOL_SIGN_MESSAGE -> "sol_signMessage"
            SOL_PARTIAL_SIGN_TRANSACTION -> "sol_partialSignTransaction"
        }
    }
}
