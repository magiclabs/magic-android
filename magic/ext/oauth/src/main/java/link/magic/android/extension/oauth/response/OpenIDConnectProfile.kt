package link.magic.android.extension.oauth.response

import androidx.annotation.Keep

@Keep
class OpenIDConnectProfile {
    var name: String? = null
    var familyName: String? = null
    var givenName: String? = null
    var middleName: String? = null
    var nickname: String? = null
    var preferredUsername: String? = null
    var profile: String? = null
    var picture: String? = null
    var website: String? = null
    var gender: String? = null
    var birthdate: String? = null
    var zoneinfo: String? = null
    var locale: String? = null
    var updatedAt: Int? = null

    // OpenIDConnectEmail
    var email: String? = null
    var emailVerified: Boolean? = null

    // OpenIDConnectPhone
    var phoneNumber: String? = null
    var phoneNumberVerified: Boolean? = null

    // OpenIDConnectAddress
    var address: OIDAddress? = null

    // OIDAddress
    class OIDAddress {
        lateinit var formatted: String;
        lateinit var streetAddress: String;
        lateinit var locality: String;
        lateinit var region: String;
        lateinit var postalCode: String;
        lateinit var country: String;
    }
}
