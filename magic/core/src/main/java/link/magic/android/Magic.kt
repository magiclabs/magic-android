package link.magic.android

import android.content.Context
import link.magic.android.core.relayer.urlBuilder.network.CustomNodeConfiguration
import link.magic.android.modules.auth.AuthModule
import link.magic.android.modules.user.UserModule
import link.magic.android.core.relayer.urlBuilder.URLBuilder

class Magic private constructor(context: Context, urlBuilder: URLBuilder): MagicCore(
    context, urlBuilder
) {
    /**
     * Contains methods for interacting with user data, checking login
     * status, generating cryptographically-secure ID tokens, and more.
     */
    val user = UserModule(rpcProvider)

    /**
     * Contains methods for starting a Magic SDK authentication flow.
     */
    val auth = AuthModule(rpcProvider)

    // default initializer
    constructor(ctx:Context, apiKey: String, locale: String = defaultLocale)
            : this(ctx, URLBuilder(apiKey, EthNetwork.Mainnet, locale, ctx.packageName, ProductType.MA))

    // Eth Network initializer
    constructor(ctx:Context, apiKey: String, ethNetwork: EthNetwork, locale: String = defaultLocale)
            : this(ctx, URLBuilder(apiKey, ethNetwork, locale, ctx.packageName, ProductType.MA))

    // Custom Node Initializer
    constructor(ctx:Context, apiKey: String, customNodeConfiguration: CustomNodeConfiguration, locale: String = defaultLocale)
            : this(ctx, URLBuilder(apiKey, customNodeConfiguration, locale, ctx.packageName, ProductType.MA))
}

