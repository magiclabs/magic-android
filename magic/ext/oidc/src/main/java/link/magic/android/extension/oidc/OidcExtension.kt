package link.magic.android.extension.oidc

import android.content.Context
import link.magic.android.Magic
import link.magic.android.core.provider.RpcProvider
import link.magic.android.extension.oidc.requestConfiguration.OpenIdConfiguration
import link.magic.android.modules.BaseModule
import link.magic.android.modules.auth.response.DIDToken
import org.web3j.protocol.core.Request
import java.util.concurrent.CompletableFuture

class OidcExtension(rpcProvider: RpcProvider): BaseModule(rpcProvider) {

    companion object {

        // Singleton
        private var singleInstance: OidcExtension? = null;

        fun getInstance(rpcProvider: RpcProvider): OidcExtension {
            if (singleInstance == null)
                singleInstance = OidcExtension(rpcProvider);

            return singleInstance as OidcExtension;
        }
    }

    fun loginWithOIDC(context: Context, configuration: OpenIdConfiguration): CompletableFuture<DIDToken> {
        provider.context = context
        val request = Request(Method.MAGIC_AUTH_LOGIN_WITH_OIDC.toString(), listOf(configuration), provider, DIDToken::class.java)
        return provider.sendAsync(request, DIDToken::class.java)
    }
}

/**
 * Extend OAuth
 */
val Magic.openid: OidcExtension
    get() = OidcExtension.getInstance(this.rpcProvider)
