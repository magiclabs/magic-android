package link.magic.android.core.relayer.urlBuilder.network
import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import link.magic.android.Magic
import link.magic.android.core.relayer.urlBuilder.BaseOptions

@Keep
internal class CustomNodeBaseOptions internal constructor(
    apiKey: String,
    @SerializedName("ETH_NETWORK") @Expose val network: CustomNodeConfiguration,
    mgboxHost: String,
    locale: String,
    bundleId: String,
    debugEnabled: Boolean? = false,
    isCH: Boolean? = false
) : BaseOptions(apiKey, mgboxHost, locale, bundleId, debugEnabled, isCH)
