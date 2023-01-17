package link.magic.android.extension.oauth

import java.util.*

internal enum class Method {
    MAGIC_OAUTH_PARSE_REDIRECT_RESULT;

    override fun toString(): String {
        return name.lowercase(Locale.ROOT)
    }
}
