package link.magic.android.core.relayer.message
import androidx.annotation.Keep
import link.magic.android.core.provider.RequestForSerialization

@Keep
internal class RequestData(val payload: RequestForSerialization<*>, outboundMessageType: OutboundMessageType, encodedParams: String) {
    val msgType: String = "$outboundMessageType-$encodedParams"
}
