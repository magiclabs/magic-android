package link.magic.android.modules.wallet.response

import androidx.annotation.Keep
import org.web3j.protocol.core.Response

@Keep
class WalletInfoResponse: Response<WalletInfo>()

@Keep
class WalletInfo {
    var walletType: String? = null
}
