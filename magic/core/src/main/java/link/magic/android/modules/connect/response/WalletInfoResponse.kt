package link.magic.android.modules.connect.response

import org.web3j.protocol.core.Response
import androidx.annotation.Keep

@Keep
class WalletInfoResponse: Response<WalletInfo>()

@Keep
class WalletInfo {
    var walletType: String? = null
}
