package link.magic.android.modules.user.response

import androidx.annotation.Keep

@Keep
enum class RecoveryMethodType {
    PHONE_NUMBER;

    override fun toString(): String {
        return name.lowercase()
    }
}
