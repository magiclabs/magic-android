package link.magic.android.modules.connect.response

import org.web3j.protocol.core.Response

class UserInfoResponse: Response<UserInfo>()

class UserInfo {
    var email: String? = null
}
