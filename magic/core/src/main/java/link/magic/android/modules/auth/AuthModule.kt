package link.magic.android.modules.auth

import android.content.Context
import link.magic.android.core.provider.RpcProvider
import link.magic.android.modules.BaseModule
import link.magic.android.modules.auth.requestConfiguration.*
import link.magic.android.modules.auth.response.DIDToken
import org.web3j.protocol.core.Request
import java.util.concurrent.CompletableFuture

class AuthModule(rpcProvider: RpcProvider) : BaseModule(rpcProvider) {
    fun loginWithMagicLink(context: Context, configuration: LoginWithMagicLinkConfiguration): CompletableFuture<DIDToken> {
        val request = Request(Method.MAGIC_AUTH_LOGIN_WITH_MAGIC_LINK.toString(), listOf(configuration), provider, DIDToken::class.java)
        provider.overlay.setContext(context)
        return provider.sendAsync(request, DIDToken::class.java)
    }

    fun loginWithSMS(context: Context, configuration: LoginWithSMSConfiguration): CompletableFuture<DIDToken> {
        val request = Request(Method.MAGIC_AUTH_LOGIN_WITH_SMS.toString(), listOf(configuration), provider, DIDToken::class.java)
        provider.overlay.setContext(context)
        return provider.sendAsync(request, DIDToken::class.java)
    }

    fun loginWithEmailOTP(context: Context, configuration: LoginWithEmailOTPConfiguration): CompletableFuture<DIDToken> {
        val request = Request(Method.MAGIC_AUTH_LOGIN_WITH_EMAIL_OTP.toString(), listOf(configuration), provider, DIDToken::class.java)
        provider.overlay.setContext(context)
        return provider.sendAsync(request, DIDToken::class.java)
    }
}