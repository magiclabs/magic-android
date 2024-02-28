package link.magic.android.core.relayer.message

data class AnnouncementMessage(
    val msgType: String,
    val response: Response
)

data class Response(
    val jsonrpc: String,
    val id: Int?, // Assuming id can be null based on your example
    val result: Result
)

data class Result(
    val product_announcement: String
)
