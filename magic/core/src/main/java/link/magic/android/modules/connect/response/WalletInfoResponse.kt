package link.magic.android.modules.connect.response

import org.web3j.protocol.core.Response

class WalletInfoResponse: Response<WalletInfo>()

class WalletInfo {
    var walletType: String? = null
}
