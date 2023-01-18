package link.magic.android.core.relayer.message

import androidx.annotation.Keep

@Keep
open class ResponseData<R> internal constructor(val response: R, val msgType: String)
