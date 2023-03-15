package link.magic.android.core.relayer.urlBuilder
import android.util.Base64
import androidx.annotation.Keep
import com.google.gson.Gson
import link.magic.android.EthNetwork
import link.magic.android.Magic
import link.magic.android.core.relayer.urlBuilder.network.CustomNodeBaseOptions
import link.magic.android.core.relayer.urlBuilder.network.CustomNodeConfiguration
import link.magic.android.core.relayer.urlBuilder.network.NetworkBaseOptions

/**
 * Here's an internal Uri builder class designed for Magic-SDK use case
 *
 * Uri from Android encodes every component of the Url, which is not ideal
 */
@Keep
class URLBuilder private constructor(var options: BaseOptions, val apiKey: String){

    private val gson = Gson()
    val url: String
        get() {
            return "$mgboxHost/send/?params=$encodedParams"
        }

    val encodedParams: String
        get() {
            val paramsInBytes = gson.toJson(options).toByteArray()
            return Base64.encodeToString(paramsInBytes, Base64.DEFAULT)
        }

    companion object {
        private const val mgboxHost = "https://box.magic.link"
    }

    internal constructor(apiKey: String, customNode: CustomNodeConfiguration, locale: String, bundleId: String)
            : this(CustomNodeBaseOptions(apiKey, customNode, mgboxHost, locale, bundleId, Magic.debugEnabled, Magic.isCH), apiKey)
    internal constructor(apiKey: String, network: EthNetwork, locale: String, bundleId: String)
            : this(NetworkBaseOptions(apiKey, network.toString().lowercase(), mgboxHost, locale, bundleId, Magic.debugEnabled, Magic.isCH), apiKey)
}
