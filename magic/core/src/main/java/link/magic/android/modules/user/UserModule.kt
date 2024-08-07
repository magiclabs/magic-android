package link.magic.android.modules.user

import android.content.Context
import link.magic.android.core.provider.RpcProvider
import link.magic.android.modules.BaseModule
import link.magic.android.modules.user.requestConfiguration.GenerateIdTokenConfiguration
import link.magic.android.modules.user.requestConfiguration.GetIdTokenConfiguration
import link.magic.android.modules.user.requestConfiguration.RecoverAccountConfiguration
import link.magic.android.modules.user.requestConfiguration.UpdateEmailConfiguration
import link.magic.android.modules.user.response.*
import org.web3j.protocol.core.Request
import java.util.concurrent.CompletableFuture
/**
 * UserModule and it's methods only work with Magic Auth API Keys
 */
class UserModule(rpcProvider: RpcProvider) : BaseModule(rpcProvider) {
        fun getIdToken(context: Context, configuration: GetIdTokenConfiguration?): CompletableFuture<GetIdTokenResponse> {
                provider.context = context
                val request = Request(Method.MAGIC_AUTH_GET_ID_TOKEN.toString(), listOf(configuration), provider, GetIdTokenResponse::class.java)
                return provider.sendAsync(request, GetIdTokenResponse::class.java)
        }
        fun generateIdToken(context: Context, configuration: GenerateIdTokenConfiguration?): CompletableFuture<GenerateIdTokenResponse>{
                provider.context = context
                val request = Request(Method.MAGIC_AUTH_GENERATE_ID_TOKEN.toString(), listOf(configuration), provider, GenerateIdTokenResponse::class.java)
                return provider.sendAsync(request, GenerateIdTokenResponse::class.java)
        }
        fun getInfo(context: Context): CompletableFuture<GetInfoResponse>{
                provider.context = context
                val request = Request(Method.MAGIC_GET_INFO.toString(), emptyList<String>(), provider, GetInfoResponse::class.java)
                return provider.sendAsync(request, GetInfoResponse::class.java)
        }
        fun isLoggedIn(context: Context): CompletableFuture<IsLoggedInResponse> {
                provider.context = context
                val request = Request(Method.MAGIC_AUTH_IS_LOGGED_IN.toString(), emptyList<String>(), provider, IsLoggedInResponse::class.java)
                return provider.sendAsync(request, IsLoggedInResponse::class.java)
        }
        fun updateEmail(context: Context, configuration: UpdateEmailConfiguration): CompletableFuture<UpdateEmailResponse> {
                provider.context = context
                val request = Request(Method.MAGIC_AUTH_UPDATE_EMAIL.toString(), listOf(configuration), provider, UpdateEmailResponse::class.java)
                return provider.sendAsync(request, UpdateEmailResponse::class.java)
        }
        
        fun showSettings(context: Context): CompletableFuture<GetInfoResponse> {
                provider.context = context
                val request = Request(Method.MAGIC_AUTH_SETTINGS.toString(), emptyList<String>(), provider, GetInfoResponse::class.java)
                return provider.sendAsync(request, GetInfoResponse::class.java)
        }

        fun logout(context: Context): CompletableFuture<LogoutResponse> {
                provider.context = context
                val request = Request(Method.MAGIC_AUTH_LOGOUT.toString(), emptyList<String>(), provider, LogoutResponse::class.java)
                return provider.sendAsync(request, LogoutResponse::class.java).thenApply { response ->
                        if (response.result === true) {
                                provider.clearWebViewStorage()
                        }

                        response
                }
        }
        fun updatePhoneNumber(context: Context): CompletableFuture<UpdatePhoneNumberResponse> {
                provider.context = context
                val request = Request(Method.MAGIC_AUTH_UPDATE_PHONE_NUMBER.toString(), emptyList<String>(), provider, UpdatePhoneNumberResponse::class.java)
                return provider.sendAsync(request, UpdatePhoneNumberResponse::class.java)
        }

        fun recoverAccount(context: Context, configuration: RecoverAccountConfiguration): CompletableFuture<RecoverAccountResponse> {
                provider.context = context
                val request = Request(Method.MAGIC_AUTH_RECOVER_ACCOUNT.toString(), listOf(configuration), provider, RecoverAccountResponse::class.java)
                return provider.sendAsync(request, RecoverAccountResponse::class.java)
        }

        fun revealPrivateKey(context: Context): CompletableFuture<RevealPrivateKeyResponse> {
                provider.context = context
                val request = Request(Method.MAGIC_REVEAL_KEY.toString(), emptyList<String>(), provider, RevealPrivateKeyResponse::class.java)
                return provider.sendAsync(request, RevealPrivateKeyResponse::class.java)
        }
}
