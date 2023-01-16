package link.magic.android

import android.content.Context
import link.magic.android.core.provider.RpcProvider
import link.magic.android.core.relayer.urlBuilder.URLBuilder
import link.magic.android.modules.web3j.signTypedData.SignTypedDataExtension
import java.util.*

abstract class MagicCore constructor(context: Context, urlBuilder: URLBuilder) {

    /**
     * Contains a Web3-compliant provider and a webview overlay. Pass this module to your Web3/Ethers
     * instance for automatic compatibility with Ethereum methods.
     */
    val rpcProvider = RpcProvider(context, urlBuilder)

    /**
     * Web3J sign TypedData Extension
     */
    val web3jSigExt = SignTypedDataExtension(rpcProvider)

    companion object {
        var debugEnabled = false
        var defaultLocale = "${Locale.getDefault().language}_${Locale.getDefault().country}"
    }
}

enum class EthNetwork {
    Mainnet, Goerli
}

internal enum class ProductType {
    MA, MC
}