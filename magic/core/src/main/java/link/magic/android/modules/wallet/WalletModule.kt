package link.magic.android.modules.wallet

import android.content.Context
import link.magic.android.core.provider.RpcProvider
import link.magic.android.modules.BaseModule
import link.magic.android.modules.wallet.requestConfiguration.RequestUserInfoWithUIConfiguration
import link.magic.android.modules.wallet.response.*
import org.web3j.protocol.core.Request
import java.util.concurrent.CompletableFuture

interface ShowUiConfig {
    val onramperParams: Map<String, String>?
}

// Default implementation of ShowUiConfig
object DefaultShowUiConfig : ShowUiConfig {
    override val onramperParams: Map<String, String> = emptyMap()
}

class CustomShowUiConfig(override val onramperParams: Map<String, String>) : ShowUiConfig

/**
 * WalletModule and it's methods only work with Magic Connect API Keys
 */
class WalletModule(rpcProvider: RpcProvider) : BaseModule(rpcProvider) {
    fun connectWithUI(context: Context): CompletableFuture<ConnectWithUIResponse> {
        provider.context = context
        val request = Request(Method.MC_LOGIN.toString(), emptyList<String>(), provider, ConnectWithUIResponse::class.java)
        return provider.sendAsync(request, ConnectWithUIResponse::class.java)
    }
    fun getInfo(context: Context): CompletableFuture<WalletInfoResponse> {
        provider.context = context
        val request = Request(Method.MC_GET_WALLET_INFO.toString(), emptyList<String>(), provider, WalletInfoResponse::class.java)
        return provider.sendAsync(request, WalletInfoResponse::class.java)
    }
    fun showUI(context: Context, config: ShowUiConfig = DefaultShowUiConfig): CompletableFuture<ShowWalletResponse> {
        provider.context = context
        val request = Request(Method.MC_WALLET.toString(), listOf(config), provider, ShowWalletResponse::class.java)
        return provider.sendAsync(request, ShowWalletResponse::class.java)
    }
    fun requestUserInfoWithUI(context: Context, configuration: RequestUserInfoWithUIConfiguration?): CompletableFuture<RequestUserInfoWithUIResponse> {
        provider.context = context
        val request = Request(Method.MC_REQUEST_USER_INFO.toString(), ( if (configuration != null) listOf(configuration) else emptyList<String>()), provider, RequestUserInfoWithUIResponse::class.java)
        return provider.sendAsync(request, RequestUserInfoWithUIResponse::class.java)
    }
    fun disconnect(context: Context): CompletableFuture<DisconnectResponse> {
        provider.context = context
        val request = Request(Method.MC_DISCONNECT.toString(), emptyList<String>(), provider, DisconnectResponse::class.java)
        return provider.sendAsync(request, DisconnectResponse::class.java)
    }
}
