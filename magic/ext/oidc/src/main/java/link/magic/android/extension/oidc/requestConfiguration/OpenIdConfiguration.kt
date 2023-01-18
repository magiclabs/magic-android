package link.magic.android.extension.oidc.requestConfiguration

import androidx.annotation.Keep

@Keep
data class OpenIdConfiguration constructor(val jwt: String, val providerId: String)
