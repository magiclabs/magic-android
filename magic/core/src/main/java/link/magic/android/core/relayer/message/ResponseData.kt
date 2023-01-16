package link.magic.android.core.relayer.message

open class ResponseData<R> internal constructor(val response: R, val msgType: String)
