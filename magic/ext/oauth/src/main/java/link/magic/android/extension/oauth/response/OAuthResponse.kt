package link.magic.android.extension.oauth.response

import androidx.annotation.Keep
import link.magic.android.modules.user.response.UserInfoResponse
import org.web3j.protocol.core.Response

@Keep
class OAuthResponse: Response<OAuthResponseClass>()

@Keep
class OAuthResponseClass {
    lateinit var magic: MagicPartialResult
    lateinit var oauth: OAuthPartialResult
}

@Keep
class OAuthPartialResult {
    var provider: String? = null
    var scope: List<String>? = null
    var accessToken: String? = null
    var userHandle: String? = null
    lateinit var userInfo: OpenIDConnectProfile
}

@Keep
class MagicPartialResult {
    var idToken: String? = null
    lateinit var userInfo: UserInfoResponse;
}
