package link.magic.android.core.provider

import androidx.annotation.Keep

@Keep
class RequestForSerialization<S>(
    private var method: String,
    private var params: List<S>,
    var id: Long
) {

    private val jsonrpc = "2.0"
}
