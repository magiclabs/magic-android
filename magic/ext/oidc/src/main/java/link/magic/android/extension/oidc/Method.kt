package link.magic.android.extension.oidc

import java.util.*

internal enum class Method {
    MAGIC_AUTH_LOGIN_WITH_OIDC;

    override fun toString(): String {
        return name.lowercase(Locale.ROOT)
    }
}
