package link.magic.android.modules.auth.requestConfiguration

import androidx.annotation.Keep

@Keep
class LoginWithEmailOTPConfiguration (var email: String, overrides: OverridesConfiguration? = null)

@Keep
class OverridesConfiguration(var variation: String)
