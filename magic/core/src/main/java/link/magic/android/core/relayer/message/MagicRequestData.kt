package link.magic.android.core.relayer.message
import androidx.annotation.Keep
import link.magic.android.core.provider.RequestForSerialization

@Keep
internal class MagicRequestData(val payload: RequestForSerialization<*>, outboundMessageType: OutboundMessageType, encodedParams: String, val rt: String? = null) {
    val msgType: String = "$outboundMessageType-$encodedParams"
    val jwt: String = DPop.createJwt()
}
