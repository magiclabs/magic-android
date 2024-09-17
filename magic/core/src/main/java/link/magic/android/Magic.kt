package link.magic.android

import android.content.Context
import link.magic.android.core.provider.RpcProvider
import link.magic.android.core.relayer.urlBuilder.URLBuilder
import link.magic.android.core.relayer.urlBuilder.network.CustomNodeConfiguration
import link.magic.android.modules.auth.AuthModule
import link.magic.android.modules.events.EventModule
import link.magic.android.modules.user.UserModule
import link.magic.android.modules.wallet.WalletModule
import link.magic.android.modules.web3j.signTypedData.SignTypedDataExtension
import java.util.*

class Magic private constructor(applicationContext: Context, urlBuilder: URLBuilder) {

    companion object {
        var debugEnabled = false
        var defaultLocale = "${Locale.getDefault().language}_${Locale.getDefault().country}"
        val TAG: String? = Magic::class.java.name
        val LOG_WARN_MSG: String = "This extension only works with Magic Auth API Keys"
    }

    /**
     * Contains a Web3-compliant provider and a webview overlay. Pass this module to your Web3/Ethers
     * instance for automatic compatibility with Ethereum methods.
     */
    val rpcProvider = RpcProvider(applicationContext, urlBuilder)

    /**
     * Web3J sign TypedData Extension
     */
    val web3jSigExt = SignTypedDataExtension(rpcProvider)

    /**
     * Contains methods for interacting with user data, checking login
     * status, generating cryptographically-secure ID tokens, and more.
     */
    val user = UserModule(rpcProvider)

    /**
     * Contains methods for starting a Magic SDK authentication flow.
     */
    val auth = AuthModule(rpcProvider)

    /**
     * Contains methods for starting a Magic Connect flow.
     */
    val wallet = WalletModule(rpcProvider)

    /**
     * Contains methods for listening/emitting events from/to Magic.
     */
    val events = EventModule(rpcProvider)

    // Default initializer
    constructor(ctx:Context, apiKey: String, locale: String = defaultLocale)
            : this(ctx, URLBuilder(apiKey, EthNetwork.Mainnet, locale, ctx.packageName, emptyMap()))

    // Meta initializer
    constructor(ctx:Context, apiKey: String, meta: Map<String, Any>, locale: String = defaultLocale)
            : this(ctx, URLBuilder(apiKey, EthNetwork.Mainnet, locale, ctx.packageName, meta))

    // Eth Network initializer
    constructor(ctx:Context, apiKey: String, ethNetwork: EthNetwork, locale: String = defaultLocale)
            : this(ctx, URLBuilder(apiKey, ethNetwork, locale, ctx.packageName, emptyMap()))

    // Custom Node Initializer
    constructor(ctx:Context, apiKey: String, customNodeConfiguration: CustomNodeConfiguration, meta: Map<String, String> = emptyMap(), locale: String = defaultLocale)
            : this(ctx, URLBuilder(apiKey, customNodeConfiguration, locale, ctx.packageName, meta))

    // Method to change the activity context when needed
    fun setContext(newContext: Context) {
        rpcProvider.context = newContext
    }

}

enum class EthNetwork {
    Mainnet, Goerli
}

