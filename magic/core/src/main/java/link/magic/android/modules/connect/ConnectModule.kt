package link.magic.android.modules.connect

import link.magic.android.core.provider.RpcProvider
import link.magic.android.modules.BaseModule
import link.magic.android.modules.connect.requestConfiguration.RequestUserInfoConfiguration
import link.magic.android.modules.connect.response.*
import link.magic.android.modules.user.requestConfiguration.*
import org.web3j.protocol.core.Request
import java.util.concurrent.CompletableFuture

class ConnectModule(rpcProvider: RpcProvider) : BaseModule(rpcProvider) {
    fun getWalletInfo(): CompletableFuture<WalletInfoResponse> {
        val request = Request(Method.MC_GET_WALLET_INFO.toString(), emptyList<String>(), provider, WalletInfoResponse::class.java)
        return provider.sendAsync(request, WalletInfoResponse::class.java)
    }
    fun showWallet(): CompletableFuture<ShowWalletResponse> {
        val request = Request(Method.MC_WALLET.toString(), emptyList<String>(), provider, ShowWalletResponse::class.java)
        return provider.sendAsync(request, ShowWalletResponse::class.java)
    }
    fun requestUserInfo(configuration: RequestUserInfoConfiguration = RequestUserInfoConfiguration()): CompletableFuture<UserInfoResponse> {
        val request = Request(Method.MC_REQUEST_USER_INFO.toString(), listOf(configuration), provider, UserInfoResponse::class.java)
        return provider.sendAsync(request, UserInfoResponse::class.java)
    }
    fun disconnect(): CompletableFuture<DisconnectResponse> {
        val request = Request(Method.MC_DISCONNECT.toString(), emptyList<String>(), provider, DisconnectResponse::class.java)
        return provider.sendAsync(request, DisconnectResponse::class.java)
    }
}
