package link.magic.android.extension.oauth.response

import link.magic.android.modules.user.response.UserMetadataResponse
import org.web3j.protocol.core.Response
import androidx.annotation.Keep

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
    lateinit var userMetadata: UserMetadataResponse;
}
