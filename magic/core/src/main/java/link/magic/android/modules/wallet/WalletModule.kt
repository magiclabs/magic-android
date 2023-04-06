package link.magic.android.modules.wallet

import android.content.Context
import android.util.Log
import link.magic.android.Magic
import link.magic.android.core.provider.RpcProvider
import link.magic.android.modules.BaseModule
import link.magic.android.modules.user.UserModule
import link.magic.android.modules.wallet.requestConfiguration.RequestUserInfoWithUIConfiguration
import link.magic.android.modules.wallet.response.*
import org.web3j.protocol.core.Request
import java.util.concurrent.CompletableFuture
/**
 * WalletModule and it's methods only work with Magic Connect API Keys
 */
class WalletModule(rpcProvider: RpcProvider) : BaseModule(rpcProvider) {

    private val TAG: String? = "Magic SDK ${WalletModule::class.java.getName()}"
    private val LOG_WARN_MSG: String = "This method only work with Magic Connect API Keys"

    fun connectWithUI(context: Context): CompletableFuture<ConnectWithUIResponse> {
        if(Magic.debugEnabled) {
            Log.w(TAG, LOG_WARN_MSG)
        }
        provider.context = context
        val request = Request(Method.MC_LOGIN.toString(), emptyList<String>(), provider, ConnectWithUIResponse::class.java)
        return provider.sendAsync(request, ConnectWithUIResponse::class.java)
    }
    fun getInfo(context: Context): CompletableFuture<WalletInfoResponse> {
        if (Magic.debugEnabled) {
            Log.w(TAG, LOG_WARN_MSG)
        }
        provider.context = context
        val request = Request(Method.MC_GET_WALLET_INFO.toString(), emptyList<String>(), provider, WalletInfoResponse::class.java)
        return provider.sendAsync(request, WalletInfoResponse::class.java)
    }
    fun showUI(context: Context): CompletableFuture<ShowWalletResponse> {
        if(Magic.debugEnabled) {
            Log.w(TAG, LOG_WARN_MSG)
        }
        provider.context = context
        val request = Request(Method.MC_WALLET.toString(), emptyList<String>(), provider, ShowWalletResponse::class.java)
        return provider.sendAsync(request, ShowWalletResponse::class.java)
    }
    fun requestUserInfoWithUI(context: Context, configuration: RequestUserInfoWithUIConfiguration?): CompletableFuture<RequestUserInfoWithUIResponse> {
        if(Magic.debugEnabled) {
            Log.w(TAG, LOG_WARN_MSG)
        }
        provider.context = context
        val request = Request(Method.MC_REQUEST_USER_INFO.toString(), ( if (configuration != null) listOf(configuration) else emptyList<String>()), provider, RequestUserInfoWithUIResponse::class.java)
        return provider.sendAsync(request, RequestUserInfoWithUIResponse::class.java)
    }
    fun disconnect(context: Context): CompletableFuture<DisconnectResponse> {
        if (Magic.debugEnabled) {
            Log.w(TAG, LOG_WARN_MSG)
        }
        provider.context = context
        val request = Request(Method.MC_DISCONNECT.toString(), emptyList<String>(), provider, DisconnectResponse::class.java)
        return provider.sendAsync(request, DisconnectResponse::class.java)
    }
}
