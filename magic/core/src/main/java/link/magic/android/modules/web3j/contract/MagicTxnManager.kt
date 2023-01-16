package link.magic.android.modules.web3j.contract

import org.web3j.protocol.Web3j
import org.web3j.protocol.core.DefaultBlockParameter
import org.web3j.protocol.core.DefaultBlockParameterName
import org.web3j.protocol.core.methods.request.Transaction
import org.web3j.protocol.core.methods.response.EthGetCode
import org.web3j.protocol.core.methods.response.EthSendTransaction
import org.web3j.tx.TransactionManager
import java.io.IOException
import java.math.BigInteger

/**
 * TransactionManager implementation using Magic auth relayer to sign and dispatch to chain.
 */
open class MagicTxnManager constructor(private val web3j: Web3j, fromAddress: String) : TransactionManager(web3j, fromAddress) {

    @get:Throws(IOException::class)
    protected open val nonce: BigInteger
        get() {
            val ethGetTransactionCount = web3j.ethGetTransactionCount(fromAddress, DefaultBlockParameterName.PENDING)
                    .send()
            return ethGetTransactionCount.transactionCount
        }

    @Throws(IOException::class)
    override fun sendTransaction(
            gasPrice: BigInteger,
            gasLimit: BigInteger,
            to: String?,
            data: String,
            value: BigInteger,
            constructor: Boolean): EthSendTransaction {
        val nonce = nonce
        val transaction = Transaction.createFunctionCallTransaction(fromAddress, nonce, gasPrice, gasLimit, to, data)
        return web3j.ethSendTransaction(transaction).send()
    }

    override fun sendEIP1559Transaction(
        chainId: Long,
        maxPriorityFeePerGas: BigInteger?,
        maxFeePerGas: BigInteger?,
        gasLimit: BigInteger?,
        to: String?,
        data: String?,
        value: BigInteger?,
        constructor: Boolean
    ): EthSendTransaction {
        val transaction = Transaction(
            fromAddress,
            nonce,
            null,
            gasLimit,
            to,
            value,
            data,
        )

        return web3j.ethSendTransaction(transaction).send()
    }

    @Throws(IOException::class)
    override fun sendCall(to: String, data: String, defaultBlockParameter: DefaultBlockParameter): String {
        val ethCall = web3j.ethCall(
                Transaction.createEthCallTransaction(null, to, data),
                defaultBlockParameter)
                .send()
        return ethCall.result
    }

    @Throws(IOException::class)
    override fun getCode(
            contractAddress: String, defaultBlockParameter: DefaultBlockParameter): EthGetCode {
        return web3j.ethGetCode(contractAddress, defaultBlockParameter).send()
    }
}
