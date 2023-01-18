package link.magic.android.core.provider

import androidx.annotation.Keep
import org.web3j.protocol.Web3jService

@Keep
class RequestForSerialization<S>(
    private var method: String,
    private var params: List<S>,
    private var id: Long
) {

    private val jsonrpc = "2.0"
}