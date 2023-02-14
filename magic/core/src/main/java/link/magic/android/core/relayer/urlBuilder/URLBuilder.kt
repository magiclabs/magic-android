package link.magic.android.core.relayer.urlBuilder
import android.util.Base64
import androidx.annotation.Keep
import com.google.gson.Gson
import com.google.gson.JsonObject
import link.magic.android.EthNetwork
import link.magic.android.ProductType
import link.magic.android.core.relayer.urlBuilder.network.CustomNodeBaseOptions
import link.magic.android.core.relayer.urlBuilder.network.CustomNodeConfiguration
import link.magic.android.core.relayer.urlBuilder.network.NetworkBaseOptions

/**
 * Here's an internal Uri builder class designed for Magic-SDK use case
 *
 * Uri from Android encodes every component of the Url, which is not ideal
 */
@Keep
class URLBuilder private constructor(var options: BaseOptions, val apiKey: String, private val productType: ProductType){

    private val gson = Gson()
    val url: String
        get() {
            return "$mgboxHost/send/?params=$encodedParams"
        }

    val encodedParams: String
        get() {

            // core options
            val jsonObject = gson.toJsonTree(options).asJsonObject
            val paramsInBytes = gson.toJson(jsonObject).replace("\\", "")

            return Base64.encodeToString(paramsInBytes.toByteArray(), Base64.DEFAULT)
        }

    companion object {
        private const val mgboxHost = "https://box.magic.link"
    }

    internal constructor(apiKey: String, customNode: CustomNodeConfiguration, locale: String, bundleId: String, productType: ProductType)
            : this(CustomNodeBaseOptions(apiKey, customNode, mgboxHost, locale, bundleId), apiKey, productType)
    internal constructor(apiKey: String, network: EthNetwork, locale: String, bundleId: String, productType: ProductType)
            : this(NetworkBaseOptions(apiKey, network.toString().lowercase(), mgboxHost, locale, bundleId), apiKey, productType)
}
