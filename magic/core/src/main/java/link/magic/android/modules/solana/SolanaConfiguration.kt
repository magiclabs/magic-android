package link.magic.android.modules.solana

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import link.magic.android.modules.extension.ExtensionConfiguration
import link.magic.android.modules.multichain.TypedArray
import org.web3j.protocol.core.Response

/**
 * Enables the Solana extension. Mirrors `SolanaConfig` in `@magic-ext/solana`.
 *
 * @param rpcUrl The Solana RPC node URL.
 */
@Keep
class SolanaConfiguration(val rpcUrl: String) : ExtensionConfiguration {
    override val name: String
        get() = "solana"

    val chainType: String = SolanaExtension.CHAIN_TYPE
}

/**
 * The format of a serialized Solana transaction.
 */
enum class SolanaTransactionType {
    /** A legacy `Transaction`. */
    Legacy,

    /** A version 0 `VersionedTransaction`. */
    V0
}

/**
 * Options for serializing a legacy transaction.
 * Mirrors `SerializeConfig` in `@solana/web3.js`.
 */
@Keep
class SolanaSerializeConfig(
    val requireAllSignatures: Boolean? = null,
    val verifySignatures: Boolean? = null
)

/**
 * The signed transaction returned by `signTransaction` and `partialSignTransaction`.
 */
@Keep
class SolanaSignedTransaction(
    @SerializedName("rawTransaction") private val typedRawTransaction: TypedArray
) {
    /** The serialized signed transaction. */
    val rawTransaction: ByteArray
        get() = typedRawTransaction.bytes
}

@Keep
class SolanaSignTransactionResponse : Response<SolanaSignedTransaction>()

@Keep
class SolanaSignMessageResponse : Response<TypedArray>() {
    /** The 64-byte ed25519 signature, or `null` if the request failed. */
    val signature: ByteArray?
        get() = result?.bytes
}

/**
 * Params for `sol_signTransaction` and `sol_partialSignTransaction`.
 * magic-js sends `type` as the string `legacy` or the number `0`.
 */
@Keep
internal class SolanaTransactionParams(
    transactionType: SolanaTransactionType,
    val serialized: TypedArray,
    val serializeConfig: SolanaSerializeConfig?
) {
    val type: Any = when (transactionType) {
        SolanaTransactionType.Legacy -> "legacy"
        SolanaTransactionType.V0 -> 0
    }
}

/**
 * Params for `sol_signMessage`. `message` is a `String` or a [TypedArray].
 */
@Keep
internal class SolanaSignMessageParams(val message: Any)
