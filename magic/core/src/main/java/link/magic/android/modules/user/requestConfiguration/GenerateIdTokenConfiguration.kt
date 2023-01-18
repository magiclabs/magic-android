package link.magic.android.modules.user.requestConfiguration
import androidx.annotation.Keep

@Keep
data class GenerateIdTokenConfiguration(var attachment: String? = "none", var lifespan: Long? = 900)
