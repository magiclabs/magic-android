package link.magic.android.modules.user

import android.content.Context
import java.util.concurrent.CompletableFuture
import link.magic.android.core.provider.RpcProvider
import link.magic.android.modules.BaseModule
import link.magic.android.modules.user.requestConfiguration.GenerateIdTokenConfiguration
import link.magic.android.modules.user.requestConfiguration.GetIdTokenConfiguration
import link.magic.android.modules.user.requestConfiguration.UpdateEmailConfiguration
import link.magic.android.modules.user.response.*
import org.web3j.protocol.core.Request

class UserModule(rpcProvider: RpcProvider) : BaseModule(rpcProvider) {
        fun getIdToken(configuration: GetIdTokenConfiguration?): CompletableFuture<GetIdTokenResponse> {
                val request = Request(Method.MAGIC_AUTH_GET_ID_TOKEN.toString(), listOf(configuration), provider, GetIdTokenResponse::class.java)
                return provider.sendAsync(request, GetIdTokenResponse::class.java)
        }
        fun generateIdToken(configuration: GenerateIdTokenConfiguration?): CompletableFuture<GenerateIdTokenResponse>{
                val request = Request(Method.MAGIC_AUTH_GENERATE_ID_TOKEN.toString(), listOf(configuration), provider, GenerateIdTokenResponse::class.java)
                return provider.sendAsync(request, GenerateIdTokenResponse::class.java)
        }
        fun getMetadata(): CompletableFuture<GetMetadataResponse>{
                val request = Request(Method.MAGIC_AUTH_GET_METADATA.toString(), emptyList<String>(), provider, GetMetadataResponse::class.java)
                return provider.sendAsync(request, GetMetadataResponse::class.java)
        }
        fun isLoggedIn(): CompletableFuture<IsLoggedInResponse> {
                val request = Request(Method.MAGIC_AUTH_IS_LOGGED_IN.toString(), emptyList<String>(), provider, IsLoggedInResponse::class.java)
                return provider.sendAsync(request, IsLoggedInResponse::class.java)
        }
        fun updateEmail(context: Context, configuration: UpdateEmailConfiguration): CompletableFuture<UpdateEmailResponse> {
                provider.overlay.setContext(context)
                val request = Request(Method.MAGIC_AUTH_UPDATE_EMAIL.toString(), listOf(configuration), provider, UpdateEmailResponse::class.java)
                return provider.sendAsync(request, UpdateEmailResponse::class.java)
        }
        fun logout(): CompletableFuture<LogoutResponse> {
                val request = Request(Method.MAGIC_AUTH_LOGOUT.toString(), emptyList<String>(), provider, LogoutResponse::class.java)
                return provider.sendAsync(request, LogoutResponse::class.java)
        }
}
