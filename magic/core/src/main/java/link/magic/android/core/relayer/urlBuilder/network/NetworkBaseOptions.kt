package link.magic.android.core.relayer.urlBuilder.network

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import link.magic.android.core.relayer.urlBuilder.BaseOptions

@Keep
internal class NetworkBaseOptions(
    apiKey: String,
    @SerializedName("ETH_NETWORK") @Expose val network: String,
    mgboxHost: String,
    locale: String,
    bundleId: String,
    meta: Map<String, Any>,
    ext: Map<String, Any>? = null
) : BaseOptions(apiKey, mgboxHost, locale, bundleId, meta, ext)
