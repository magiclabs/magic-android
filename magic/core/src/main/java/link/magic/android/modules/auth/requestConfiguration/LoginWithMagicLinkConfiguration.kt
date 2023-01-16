package link.magic.android.modules.auth.requestConfiguration

data class LoginWithMagicLinkConfiguration(var showUI: Boolean? = true,var email: String) {
    constructor(email: String) : this(true, email)
}
