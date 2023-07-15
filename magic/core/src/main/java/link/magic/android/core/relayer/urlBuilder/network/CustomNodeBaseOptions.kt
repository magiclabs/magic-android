package link.magic.android.core.relayer.urlBuilder.network
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import link.magic.android.core.relayer.urlBuilder.BaseOptions

@Keep
internal class CustomNodeBaseOptions internal constructor(
    apiKey: String,
    @SerializedName("ETH_NETWORK") @Expose val network: CustomNodeConfiguration,
    mgboxHost: String,
    locale: String,
    bundleId: String
) : BaseOptions(apiKey, mgboxHost, locale, bundleId)
