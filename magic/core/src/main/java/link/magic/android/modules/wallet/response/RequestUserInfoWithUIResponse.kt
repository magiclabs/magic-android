package link.magic.android.modules.wallet.response

import androidx.annotation.Keep
import org.web3j.protocol.core.Response

@Keep
class RequestUserInfoWithUIResponse: Response<UserInfo>()

@Keep
class UserInfo {
    var email: String? = null
}
