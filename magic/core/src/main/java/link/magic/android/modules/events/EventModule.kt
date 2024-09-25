package link.magic.android.modules.events

import android.content.Context
import link.magic.android.MagicEvent
import link.magic.android.core.provider.RpcProvider
import link.magic.android.modules.BaseModule
import link.magic.android.modules.wallet.response.DisconnectResponse
import org.web3j.protocol.core.Request
import java.util.concurrent.CompletableFuture

class EventModule(rpcProvider: RpcProvider) : BaseModule(rpcProvider) {
    fun emit(eventType: MagicEvent, context: Context): CompletableFuture<DisconnectResponse> {
        provider.context = context
        val request = Request(Method.MAGIC_INTERMEDIARY_EVENT.toString(), listOf(mapOf("eventType" to eventType.toString())), provider, DisconnectResponse::class.java)
        return provider.sendAsync(request, DisconnectResponse::class.java)
    }
}
