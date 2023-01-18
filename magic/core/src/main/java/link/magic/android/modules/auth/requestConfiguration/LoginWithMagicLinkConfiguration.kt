package link.magic.android.modules.auth.requestConfiguration

import androidx.annotation.Keep

@Keep
data class LoginWithMagicLinkConfiguration(var showUI: Boolean? = true,var email: String) {
    constructor(email: String) : this(true, email)
}
