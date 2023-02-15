package link.magic.android.modules.user

enum class Method {
    MAGIC_AUTH_GET_ID_TOKEN,
    MAGIC_AUTH_GENERATE_ID_TOKEN,
    MAGIC_AUTH_GET_METADATA,
    MAGIC_AUTH_IS_LOGGED_IN,
    MAGIC_AUTH_SETTINGS,
    MAGIC_AUTH_LOGOUT,
    MAGIC_AUTH_UPDATE_EMAIL,
    MAGIC_AUTH_UPDATE_PHONE_NUMBER,
    MAGIC_AUTH_RECOVER_ACCOUNT;

    override fun toString(): String {
        return name.lowercase()
    }
}
