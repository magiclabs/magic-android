package link.magic.android.modules.auth

internal enum class Method {
    MAGIC_AUTH_LOGIN_WITH_MAGIC_LINK,
    MAGIC_AUTH_LOGIN_WITH_SMS,
    MAGIC_AUTH_LOGIN_WITH_EMAIL_OTP;

    override fun toString(): String {
        return name.lowercase()
    }
}
