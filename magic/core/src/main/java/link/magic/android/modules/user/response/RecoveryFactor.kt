package link.magic.android.modules.user.response

import androidx.annotation.Keep

@Keep
class RecoveryFactor {
    var value: String = "";
    var type: RecoveryMethodType = RecoveryMethodType.PHONE_NUMBER;

    override fun toString(): String {
        return "value: ${value}\ntype: ${type}\n"
    }
}
