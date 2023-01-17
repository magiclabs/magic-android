package link.magic.android.core.relayer.urlBuilder.network

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import link.magic.android.core.relayer.urlBuilder.BaseOptions

internal class NetworkBaseOptions(
    apiKey: String,
    @SerializedName("ETH_NETWORK") @Expose val network: String,
    mgboxHost: String,
    locale: String,
    bundleId: String
) :
        BaseOptions(apiKey, mgboxHost, locale, bundleId)
