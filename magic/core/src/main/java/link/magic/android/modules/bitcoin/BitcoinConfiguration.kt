package link.magic.android.modules.bitcoin

import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import link.magic.android.modules.extension.ExtensionConfiguration
import org.web3j.protocol.core.Response

/**
 * Enables the Bitcoin extension. Mirrors `BitcoinConfig` in `@magic-ext/bitcoin`.
 *
 * @param rpcUrl The Bitcoin node or indexer URL.
 * @param network The network name, e.g. `mainnet` or `testnet`.
 */
@Keep
class BitcoinConfiguration(val rpcUrl: String, network: String) : ExtensionConfiguration {
    override val name: String
        get() = "bitcoin"

    val chainType: String = BitcoinExtension.CHAIN_TYPE

    val options = Options(network)

    @Keep
    class Options(val network: String)
}

/**
 * A UTXO to spend. [value] is in BTC (not satoshis), with at most eight decimal
 * places. The enclave needs this value to compute its segwit (BIP143) sighash.
 * [txNum] is the output index (vout) of the UTXO within its source transaction.
 */
@Keep
class BitcoinTransactionInput(
    val txid: String,
    @SerializedName("tx_num") val txNum: Int,
    val value: Double,
    val address: String? = null
)

/**
 * A transaction output. [value] is in BTC with at most eight decimal places.
 */
@Keep
class BitcoinTransactionOutput(
    val address: String,
    val value: Double
)

/**
 * The result of `signTransaction`.
 */
@Keep
class BitcoinSignedTransaction(
    /** The broadcast-ready, fully-signed transaction as hex. */
    val signedTransaction: String,
    /** The transaction hash (txid). */
    val transactionHash: String
)

@Keep
class BitcoinSignTransactionResponse : Response<BitcoinSignedTransaction>()

/**
 * Params for `btc_signTransaction`.
 */
@Keep
internal class BitcoinTransactionParams(
    val inputs: List<BitcoinTransactionInput>,
    val outputs: List<BitcoinTransactionOutput>
)
