package link.magic.android.modules.multichain

import android.content.Context
import androidx.annotation.Keep
import link.magic.android.core.provider.RpcProvider
import link.magic.android.modules.BaseModule
import link.magic.android.modules.user.response.RevealPrivateKeyResponse
import org.web3j.protocol.core.Request
import org.web3j.protocol.core.Response
import java.util.concurrent.CompletableFuture

@Keep
class MultichainPublicAddressResponse : Response<String>()

@Keep
internal class ChainParams(val chain: String)

/**
 * Base class for non-EVM chain extensions.
 * Mirrors `MultichainExtension` in `@magic-sdk/provider`.
 *
 * @param chain The chain type sent to the relayer, e.g. `SOLANA` or `BITCOIN`.
 */
open class MultichainExtension internal constructor(rpcProvider: RpcProvider, val chain: String) : BaseModule(rpcProvider) {

    /**
     * Gets the user's public address on this chain.
     */
    fun getPublicAddress(context: Context): CompletableFuture<MultichainPublicAddressResponse> {
        provider.context = context
        val request = Request(
            Method.MAGIC_GET_MULTICHAIN_PUBLIC_ADDRESS.toString(),
            listOf(ChainParams(chain)),
            provider,
            MultichainPublicAddressResponse::class.java
        )
        return provider.sendAsync(request, MultichainPublicAddressResponse::class.java)
    }

    /**
     * Shows the Magic UI that reveals the user's private key for this chain.
     */
    fun revealPrivateKey(context: Context): CompletableFuture<RevealPrivateKeyResponse> {
        provider.context = context
        val request = Request(
            Method.MAGIC_REVEAL_KEY.toString(),
            listOf(ChainParams(chain)),
            provider,
            RevealPrivateKeyResponse::class.java
        )
        return provider.sendAsync(request, RevealPrivateKeyResponse::class.java)
    }
}
