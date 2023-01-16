package link.magic.android.core.relayer.urlBuilder.network

data class CustomNodeConfiguration(private val rpcUrl: String, private val chainId: String? = null) {
    constructor(rpcUrl: String) : this(rpcUrl, null)
}
