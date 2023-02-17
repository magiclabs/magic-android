package link.magic.android.modules.wallet.response

import org.web3j.protocol.core.Response
import androidx.annotation.Keep

@Keep
class UserInfoResponse: Response<UserInfo>()

@Keep
class UserInfo {
    var email: String? = null
}
