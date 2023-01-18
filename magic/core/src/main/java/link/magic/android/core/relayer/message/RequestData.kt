package link.magic.android.core.relayer.message
import androidx.annotation.Keep
import link.magic.android.core.provider.RequestForSerialization
import org.web3j.protocol.core.Request

@Keep
internal class RequestData(val payload: RequestForSerialization<*>, outboundMessageType: OutboundMessageType, encodedParams: String) {
    val msgType: String = "$outboundMessageType-$encodedParams"
}
