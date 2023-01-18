package link.magic.android.modules.auth.requestConfiguration

import androidx.annotation.Keep

@Keep
data class LoginWithSMSConfiguration(var phoneNumber: String) {
    private val showUI: Boolean = true
}
