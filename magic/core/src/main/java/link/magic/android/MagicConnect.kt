package link.magic.android

import android.content.Context
import link.magic.android.core.relayer.urlBuilder.network.CustomNodeConfiguration
import link.magic.android.modules.connect.ConnectModule
import link.magic.android.core.relayer.urlBuilder.URLBuilder
import java.util.*

class MagicConnect private constructor(context: Context, urlBuilder: URLBuilder): MagicCore(
    context, urlBuilder
) {

    /**
     * Contains methods for interacting with user data, checking login
     * status, generating cryptographically-secure ID tokens, and more.
     */
    val connect = ConnectModule(rpcProvider)

    // default initializer
    constructor(ctx: Context, apiKey: String)
            : this(ctx, URLBuilder(apiKey, EthNetwork.Mainnet, defaultLocale, ctx.packageName, ProductType.MC))

    // Eth Network initializer
    constructor(ctx: Context, apiKey: String, ethNetwork: EthNetwork)
            : this(ctx, URLBuilder(apiKey, ethNetwork, defaultLocale, ctx.packageName, ProductType.MC))

    // Custom Node Initializer
    constructor(ctx: Context, apiKey: String, customNodeConfiguration: CustomNodeConfiguration)
            : this(ctx, URLBuilder(apiKey, customNodeConfiguration, defaultLocale, ctx.packageName, ProductType.MC))
}
