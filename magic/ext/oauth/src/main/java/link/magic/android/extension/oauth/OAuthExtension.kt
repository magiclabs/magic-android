package link.magic.android.extension.oauth

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import link.magic.android.Magic
import link.magic.android.core.provider.RpcProvider
import link.magic.android.extension.oauth.challenge.OAuthChallenge
import link.magic.android.extension.oauth.customTab.CustomTabMainActivity
import link.magic.android.extension.oauth.customTab.CustomTabMainActivity.Companion.EXTRA_URL
import link.magic.android.extension.oauth.customTab.CustomTabUtils
import link.magic.android.extension.oauth.requestConfiguration.OAuthConfiguration
import link.magic.android.extension.oauth.response.OAuthResponse
import link.magic.android.modules.BaseModule
import org.web3j.protocol.core.Request
import java.util.concurrent.CompletableFuture


class OAuthExtension(rpcProvider: RpcProvider): BaseModule(rpcProvider) {

    private var configuration: OAuthConfiguration? = null
    private var oauthChallenge: OAuthChallenge? = null

    companion object {

        // Singleton
        private var singleInstance: OAuthExtension? = null;

        fun getInstance(rpcProvider: RpcProvider): OAuthExtension {
            if (this.singleInstance == null)
                this.singleInstance = OAuthExtension(rpcProvider);

            return singleInstance as OAuthExtension;
        }
    }

    /**
     * OAuth extension interface
     */
    fun loginWithPopup(context: Context, configuration: OAuthConfiguration): Boolean {

        // configuration
        this.oauthChallenge = OAuthChallenge()
        this.configuration = configuration

        provider.context = context

        //Invoke Custom Tab
        startCustomTab(configuration, provider.urlBuilder.apiKey)

        return true
    }

    /* Actual call to retrieve id token */
    fun getResult(data: Intent?): CompletableFuture<OAuthResponse>{
        if (data != null) {
            val urlResult = data.getStringExtra(EXTRA_URL)
            if (urlResult != null) {
                val uri = Uri.parse(urlResult)

                val list = oauthChallenge?.let { listOf("?${uri.query.toString()}", it.verifier, it.state) }
                val request = Request(Method.MAGIC_OAUTH_PARSE_REDIRECT_RESULT.toString(), list, provider, OAuthResponse::class.java)
                return provider.sendAsync(request, OAuthResponse::class.java)
            }
        }
        throw Error("OAuth callback data is invalid")
    }

    /**
     * start CustomTab for the authentication flow
     */
    private fun startCustomTab(configuration: OAuthConfiguration, apiKey: String) {

        val activity = provider.context as Activity
        val packageName = provider.context.packageName

        val uri = oauthChallenge?.let { CustomTabUtils.buildOAuthUri(configuration, apiKey, packageName, it) }

        val intent = Intent(activity, CustomTabMainActivity::class.java)
        intent.putExtra(CustomTabMainActivity.EXTRA_CHROME_PACKAGE, getChromePackage())
        intent.putExtra(CustomTabMainActivity.URI, uri.toString())
        activity.startActivityForResult(intent, 0)
    }

    private fun getChromePackage(): String? {
        return CustomTabUtils.chromePackage(provider.context)
    }
}

/**
 * Extend OAuth
 */
val Magic.oauth: OAuthExtension
    get() = OAuthExtension.getInstance(this.rpcProvider)
