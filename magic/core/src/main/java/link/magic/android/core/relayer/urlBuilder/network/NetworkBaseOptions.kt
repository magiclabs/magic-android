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
    remoteDebugEnabled: Boolean? = false
) : BaseOptions(apiKey, mgboxHost, locale, bundleId, remoteDebugEnabled)
