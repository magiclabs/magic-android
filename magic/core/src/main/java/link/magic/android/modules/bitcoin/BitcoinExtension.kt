package link.magic.android.modules.bitcoin

import android.content.Context
import link.magic.android.core.provider.RpcProvider
import link.magic.android.modules.multichain.MultichainExtension
import org.web3j.protocol.core.Request
import java.util.concurrent.CompletableFuture

/**
 * Bitcoin extension for signing transactions.
 * Mirrors the `@magic-ext/bitcoin` package in magic-js.
 *
 * Available as `magic.bitcoin` when `Magic` was created with a [BitcoinConfiguration].
 */
class BitcoinExtension internal constructor(rpcProvider: RpcProvider) : MultichainExtension(rpcProvider, CHAIN_TYPE) {

    companion object {
        internal const val CHAIN_TYPE = "BITCOIN"
    }

    /**
     * Signs a Bitcoin transaction in the TEE. The caller supplies fully-formed
     * inputs (the UTXOs to spend, each with its value) and outputs. Coin
     * selection, fee, and change are the caller's job. Resolves with the
     * broadcast-ready signed transaction.
     *
     * Input and output values are non-negative BTC amounts with at most eight
     * decimal places. Tiny floating-point errors (up to 0.001 sat) are
     * tolerated. Work out fees and change in integer satoshis, then convert to
     * BTC for this method. Total outputs must not exceed total inputs. The
     * difference is the fee. There is no minimum or maximum signing fee, and
     * no dust threshold is enforced. Check the fee rate (sats/vB) and output
     * amounts against the broadcasting node's policy. Signing does not
     * guarantee relay, confirmation, or UTXO availability. Invalid
     * transaction params fail with an RPC error that describes the problem.
     *
     * A value that is NaN, infinite or negative fails the returned future with an
     * [IllegalArgumentException] before anything is sent.
     */
    fun signTransaction(
        context: Context,
        inputs: List<BitcoinTransactionInput>,
        outputs: List<BitcoinTransactionOutput>
    ): CompletableFuture<BitcoinSignTransactionResponse> {
        val values = inputs.map { it.value } + outputs.map { it.value }
        if (values.any { !it.isFinite() || it < 0 }) {
            val failed = CompletableFuture<BitcoinSignTransactionResponse>()
            failed.completeExceptionally(IllegalArgumentException("Bitcoin amounts must be finite and not negative"))
            return failed
        }

        provider.context = context
        val request = Request(
            Method.BTC_SIGN_TRANSACTION.toString(),
            listOf(BitcoinTransactionParams(inputs, outputs)),
            provider,
            BitcoinSignTransactionResponse::class.java
        )
        return provider.sendAsync(request, BitcoinSignTransactionResponse::class.java)
    }
}
