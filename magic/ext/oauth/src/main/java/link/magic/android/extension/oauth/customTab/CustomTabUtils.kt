package link.magic.android.extension.oauth.customTab

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.browser.customtabs.CustomTabsService
import link.magic.android.extension.oauth.challenge.OAuthChallenge
import link.magic.android.extension.oauth.requestConfiguration.OAuthConfiguration
import java.util.*
import kotlin.reflect.full.memberProperties

object CustomTabUtils {
    private val CHROME_PACKAGES = arrayOf(
            "com.android.chrome", "com.chrome.beta", "com.chrome.dev")

    internal fun chromePackage(context: Context): String? {

            val serviceIntent = Intent(CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION)
            val resolveInfos = context.packageManager.queryIntentServices(serviceIntent, 0)
            val chromePackages: Set<String> = HashSet(listOf(*CHROME_PACKAGES))
            for (resolveInfo in resolveInfos) {
                val serviceInfo = resolveInfo.serviceInfo
                if (serviceInfo != null && chromePackages.contains(serviceInfo.packageName)) {
                    return serviceInfo.packageName
                }
            }
            return null
    }

    fun buildOAuthUri(configuration: OAuthConfiguration, apiKey: String, packageName: String, oauthChallenge: OAuthChallenge): Uri.Builder {

        /**
         * Query class to avoid appendQueryParameter double encoding
         */
        data class Query(val magic_api_key: String, val magic_challenge: String, val state: String, val redirect_uri: String) {
            val platform: String = "rn"
            val bundleId = packageName
        }

        /**
         *  Construct challenge
         */
        val query = Query(apiKey, oauthChallenge.challenge, oauthChallenge.state, configuration.redirectURI)
        val sb = StringBuilder()

        for (prop in Query::class.memberProperties) {
            println("${prop.name} = ${prop.get(query)}")
            sb.append(prop.name)
            sb.append("=")
            sb.append(prop.get(query))
            sb.append("&")
        }

        val uri = Uri.Builder()
                .scheme("https")
                .authority("auth.magic.link")
                .appendPath("v1")
                .appendPath("oauth2")
                .appendPath(configuration.provider.toString().lowercase(Locale.ROOT))
                .appendPath("start")
                .encodedQuery(sb.toString())

        val scope = configuration.scope
        if (scope != null && scope.isNotEmpty()) {
            uri.appendQueryParameter("scope", scope.joinToString { " " })
        }

        if (configuration.loginHint != null) {
            uri.appendQueryParameter("login_hint", configuration.loginHint)
        }

        return uri
    }
}
