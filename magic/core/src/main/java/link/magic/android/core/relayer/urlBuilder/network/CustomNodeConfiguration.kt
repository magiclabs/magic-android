package link.magic.android.core.relayer.urlBuilder.network

import androidx.annotation.Keep

@Keep
data class CustomNodeConfiguration(private val rpcUrl: String, private val chainId: String? = null) {
    constructor(rpcUrl: String) : this(rpcUrl, null)
}
