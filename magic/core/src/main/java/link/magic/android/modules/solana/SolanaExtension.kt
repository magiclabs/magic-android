package link.magic.android.modules.solana

import android.content.Context
import link.magic.android.core.provider.RpcProvider
import link.magic.android.modules.multichain.MultichainExtension
import link.magic.android.modules.multichain.TypedArray
import java.util.concurrent.CompletableFuture

/**
 * Solana extension for signing transactions and messages.
 * Mirrors the `@magic-ext/solana` package in magic-js.
 *
 * magic-js takes `Transaction` and `VersionedTransaction` objects from
 * `@solana/web3.js`. This SDK has no Solana dependency, so it takes the
 * serialized transaction bytes and their [SolanaTransactionType] instead.
 * Serialize a legacy transaction the same way you would pass it to magic-js,
 * usually with `requireAllSignatures = false`.
 *
 * Available as `magic.solana` when `Magic` was created with a [SolanaConfiguration].
 */
class SolanaExtension internal constructor(rpcProvider: RpcProvider) : MultichainExtension(rpcProvider, CHAIN_TYPE) {

    companion object {
        internal const val CHAIN_TYPE = "SOLANA"
    }

    /**
     * Signs a Solana transaction.
     *
     * @param serialized The serialized transaction.
     * @param type [SolanaTransactionType.Legacy] for a `Transaction`, [SolanaTransactionType.V0] for a `VersionedTransaction`.
     * @param serializeConfig The config used to serialize the signed legacy transaction.
     */
    fun signTransaction(
        context: Context,
        serialized: ByteArray,
        type: SolanaTransactionType,
        serializeConfig: SolanaSerializeConfig? = null
    ): CompletableFuture<SolanaSignTransactionResponse> {
        provider.context = context
        return provider.sendAsync(
            Method.SOL_SIGN_TRANSACTION.toString(),
            SolanaTransactionParams(type, TypedArray(serialized), serializeConfig),
            SolanaSignTransactionResponse::class.java
        )
    }

    /**
     * Adds the user's signature to a transaction that other parties also sign.
     *
     * @param serialized The serialized transaction.
     * @param type [SolanaTransactionType.Legacy] for a `Transaction`, [SolanaTransactionType.V0] for a `VersionedTransaction`.
     * @param serializeConfig The config used to serialize the signed legacy transaction.
     */
    fun partialSignTransaction(
        context: Context,
        serialized: ByteArray,
        type: SolanaTransactionType,
        serializeConfig: SolanaSerializeConfig? = null
    ): CompletableFuture<SolanaSignTransactionResponse> {
        provider.context = context
        return provider.sendAsync(
            Method.SOL_PARTIAL_SIGN_TRANSACTION.toString(),
            SolanaTransactionParams(type, TypedArray(serialized), serializeConfig),
            SolanaSignTransactionResponse::class.java
        )
    }

    /**
     * Signs a UTF-8 message. The response holds the 64-byte ed25519 signature.
     */
    fun signMessage(context: Context, message: String): CompletableFuture<SolanaSignMessageResponse> {
        return signMessage(context, SolanaSignMessageParams(message))
    }

    /**
     * Signs raw bytes. The response holds the 64-byte ed25519 signature.
     */
    fun signMessage(context: Context, message: ByteArray): CompletableFuture<SolanaSignMessageResponse> {
        return signMessage(context, SolanaSignMessageParams(TypedArray(message)))
    }

    private fun signMessage(context: Context, params: SolanaSignMessageParams): CompletableFuture<SolanaSignMessageResponse> {
        provider.context = context
        return provider.sendAsync(
            Method.SOL_SIGN_MESSAGE.toString(),
            params,
            SolanaSignMessageResponse::class.java
        )
    }
}
