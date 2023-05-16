package link.magic.android.modules.user.response

import androidx.annotation.Keep

@Keep
class UserMetadataResponse {
    var issuer: String? = null
    var publicAddress: String? = null
    var email: String? = null
    var phoneNumber: String? = null
}
