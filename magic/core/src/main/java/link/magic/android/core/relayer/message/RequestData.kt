package link.magic.android.core.relayer.message
import org.web3j.protocol.core.Request

internal class RequestData(val payload: Request<*, *>, outboundMessageType: OutboundMessageType, encodedParams: String) {
    val msgType: String = "$outboundMessageType-$encodedParams"
}
