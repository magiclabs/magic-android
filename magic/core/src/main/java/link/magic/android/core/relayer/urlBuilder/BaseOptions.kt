package link.magic.android.core.relayer.urlBuilder

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

open class BaseOptions(
    @SerializedName("API_KEY") @Expose val apiKey: String,
    val host: String,
    val locale: String,
    val bundleId: String
): Serializable {
    val sdk = "magic-sdk-android"
}
