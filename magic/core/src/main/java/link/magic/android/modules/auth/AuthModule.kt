package link.magic.android.modules.auth

import android.content.Context
import android.util.Log
import link.magic.android.core.provider.RpcProvider
import link.magic.android.modules.BaseModule
import link.magic.android.modules.auth.requestConfiguration.LoginWithEmailOTPConfiguration
import link.magic.android.modules.auth.requestConfiguration.LoginWithMagicLinkConfiguration
import link.magic.android.modules.auth.requestConfiguration.LoginWithSMSConfiguration
import link.magic.android.modules.auth.response.DIDToken
import link.magic.android.modules.user.UserModule
import org.web3j.protocol.core.Request
import java.util.concurrent.CompletableFuture
/**
 * AuthModule and it's methods only work with Magic Auth API Keys
 */
class AuthModule(rpcProvider: RpcProvider) : BaseModule(rpcProvider) {

    private val TAG: String? = "Magic SDK ${AuthModule::class.java.getName()}"
    private val LOG_WARN_MSG: String = "This method only work with Magic Auth API Keys"

    fun loginWithMagicLink(context: Context, configuration: LoginWithMagicLinkConfiguration): CompletableFuture<DIDToken> {
        val request = Request(Method.MAGIC_AUTH_LOGIN_WITH_MAGIC_LINK.toString(), listOf(configuration), provider, DIDToken::class.java)
        provider.context = context
        return provider.sendAsync(request, DIDToken::class.java)
    }

    fun loginWithSMS(context: Context, configuration: LoginWithSMSConfiguration): CompletableFuture<DIDToken> {
        Log.w(TAG, LOG_WARN_MSG)
        val request = Request(Method.MAGIC_AUTH_LOGIN_WITH_SMS.toString(), listOf(configuration), provider, DIDToken::class.java)
        provider.context = context
        return provider.sendAsync(request, DIDToken::class.java)
    }

    fun loginWithEmailOTP(context: Context, configuration: LoginWithEmailOTPConfiguration): CompletableFuture<DIDToken> {
        Log.w(TAG, LOG_WARN_MSG)
        val request = Request(Method.MAGIC_AUTH_LOGIN_WITH_EMAIL_OTP.toString(), listOf(configuration), provider, DIDToken::class.java)
        provider.context = context
        return provider.sendAsync(request, DIDToken::class.java)
    }
}
