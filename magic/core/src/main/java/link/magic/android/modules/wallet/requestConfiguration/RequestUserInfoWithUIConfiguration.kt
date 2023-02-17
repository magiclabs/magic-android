package link.magic.android.modules.wallet.requestConfiguration

import androidx.annotation.Keep
import org.web3j.protocol.core.Response

@Keep
data class RequestUserInfoWithUIConfiguration(var scope: WalletUserInfoScope)

@Keep
data class WalletUserInfoScope(var email: WalletUserInfoEmailOptions)

@Keep
enum class WalletUserInfoEmailOptions {
    required, optional
}

