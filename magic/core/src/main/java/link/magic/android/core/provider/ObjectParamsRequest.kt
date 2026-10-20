package link.magic.android.core.provider

import androidx.annotation.Keep
import link.magic.android.core.relayer.message.DPop
import link.magic.android.core.relayer.message.OutboundMessageType

/**
 * A JSON-RPC request whose `params` is a single object, not a list. The Solana
 * methods are sent this way, as in magic-js. web3j's `Request` only holds a list,
 * so these requests skip it and have their own types.
 */
@Keep
internal class ObjectParamsRequest(
    private var method: String,
    private var params: Any,
    private var id: Long
) {

    private val jsonrpc = "2.0"
}

@Keep
internal class MagicObjectParamsRequestData(val payload: ObjectParamsRequest, outboundMessageType: OutboundMessageType, encodedParams: String) {
    val msgType: String = "$outboundMessageType-$encodedParams"
    val jwt: String = DPop.createJwt()
}
