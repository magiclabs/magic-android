package link.magic.android.core.relayer.message

data class ReceivedMessage(
    val msgType: String,
    val response: ProductResponseType
)

data class ProductResponseType(
    val product_type: String
)
