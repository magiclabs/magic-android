package link.magic.android.modules.user.requestConfiguration
import androidx.annotation.Keep

@Keep
data class UpdateEmailConfiguration(var email: String, var showUI: Boolean? = true)
