package link.magic.android.core.relayer.message

data class AnnouncementResponse(
    val jsonrpc: String,
    val id: Int?, // Assuming id can be null based on your example
    val result: Result
)

data class Result(
    val product_announcement: String
)
