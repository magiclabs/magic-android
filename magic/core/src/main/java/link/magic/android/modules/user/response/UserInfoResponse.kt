package link.magic.android.modules.user.response

import androidx.annotation.Keep

@Keep
class UserInfoResponse {
    var issuer: String? = null
    var publicAddress: String? = null
    var email: String? = null
    var isMfaEnabled: Boolean = false;
    var recoveryFactors: Array<RecoveryFactor> = emptyArray();

    override fun toString(): String {
        return "email: $email\nissuer: $issuer\npublic address: $publicAddress\nis MFA Enabled: ${isMfaEnabled}\nrecovery factors: ${recoveryFactors.contentToString()}" ;
    }
}
