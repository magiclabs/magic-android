package link.magic.android.modules.events

import android.content.Context
import link.magic.android.core.provider.RpcProvider
import link.magic.android.modules.BaseModule
import link.magic.android.modules.wallet.response.DisconnectResponse
import org.web3j.protocol.core.Request
import java.util.concurrent.CompletableFuture

class EventModule(rpcProvider: RpcProvider) : BaseModule(rpcProvider) {
    val CLOSE_MAGIC_WINDOW_EVENT = "close-magic-window";
    val CLOSED_BY_USER_EVENT = "closed-by-user-on-received";

    fun emit(eventType: String, context: Context): CompletableFuture<DisconnectResponse> {
        provider.context = context
        val request = Request(Method.MAGIC_INTERMEDIARY_EVENT.toString(), listOf(mapOf("eventType" to eventType)), provider, DisconnectResponse::class.java)
        return provider.sendAsync(request, DisconnectResponse::class.java)
    }
}
