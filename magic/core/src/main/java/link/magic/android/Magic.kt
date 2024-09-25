package link.magic.android

import android.content.Context
import link.magic.android.core.provider.RpcProvider
import link.magic.android.core.relayer.urlBuilder.URLBuilder
import link.magic.android.core.relayer.urlBuilder.network.CustomNodeConfiguration
import link.magic.android.modules.auth.AuthModule
import link.magic.android.modules.events.EventModule
import link.magic.android.modules.user.UserModule
import link.magic.android.modules.wallet.WalletModule
import link.magic.android.modules.web3j.signTypedData.SignTypedDataExtension
import java.util.*

class Magic private constructor(applicationContext: Context, urlBuilder: URLBuilder) {

    companion object {
        var debugEnabled = false
        var defaultLocale = "${Locale.getDefault().language}_${Locale.getDefault().country}"
        val TAG: String? = Magic::class.java.name
        val LOG_WARN_MSG: String = "This extension only works with Magic Auth API Keys"
    }

    /**
     * Contains a Web3-compliant provider and a webview overlay. Pass this module to your Web3/Ethers
     * instance for automatic compatibility with Ethereum methods.
     */
    val rpcProvider = RpcProvider(applicationContext, urlBuilder)

    /**
     * Web3J sign TypedData Extension
     */
    val web3jSigExt = SignTypedDataExtension(rpcProvider)

    /**
     * Contains methods for interacting with user data, checking login
     * status, generating cryptographically-secure ID tokens, and more.
     */
    val user = UserModule(rpcProvider)

    /**
     * Contains methods for starting a Magic SDK authentication flow.
     */
    val auth = AuthModule(rpcProvider)

    /**
     * Contains methods for starting a Magic Connect flow.
     */
    val wallet = WalletModule(rpcProvider)

    /**
     * Contains methods for listening/emitting events from/to Magic.
     */
    val events = EventModule(rpcProvider)

    // Default initializer
    constructor(ctx:Context, apiKey: String, locale: String = defaultLocale)
            : this(ctx, URLBuilder(apiKey, EthNetwork.Mainnet, locale, ctx.packageName, emptyMap()))

    // Meta initializer
    constructor(ctx:Context, apiKey: String, meta: Map<String, Any>, locale: String = defaultLocale)
            : this(ctx, URLBuilder(apiKey, EthNetwork.Mainnet, locale, ctx.packageName, meta))

    // Eth Network initializer
    constructor(ctx:Context, apiKey: String, ethNetwork: EthNetwork, locale: String = defaultLocale)
            : this(ctx, URLBuilder(apiKey, ethNetwork, locale, ctx.packageName, emptyMap()))

    // Custom Node Initializer
    constructor(ctx:Context, apiKey: String, customNodeConfiguration: CustomNodeConfiguration, meta: Map<String, Any> = emptyMap(), locale: String = defaultLocale)
            : this(ctx, URLBuilder(apiKey, customNodeConfiguration, locale, ctx.packageName, meta))

    // Method to change the activity context when needed
    fun setContext(newContext: Context) {
        rpcProvider.context = newContext
    }

}

enum class EthNetwork {
    Mainnet, Goerli
}

enum class MagicEvent(val event: String) {
    // only available when allowAllEvents is true on the Magic constructor
    CLOSE_MAGIC_WINDOW("close-magic-window"),
    CLOSED_BY_USER("closed-by-user-on-received"),

    // LoginWithSmsOTPEventOnReceived
    SMS_OTP_SENT("sms-otp-sent"),
    INVALID_SMS_OTP("invalid-sms-otp"),
    EXPIRED_SMS_OTP("expired-sms-otp"),

    // LoginWithEmailOTPEventOnReceived
    EMAIL_OTP_SENT("email-otp-sent"),
    INVALID_EMAIL_OTP("invalid-email-otp"),
    INVALID_MFA_OTP("invalid-mfa-otp"),
    EXPIRED_EMAIL_OTP("expired-email-otp"),
    MFA_SENT_HANDLE("mfa-sent-handle"),

    // DeviceVerificationEventOnReceived
    DEVICE_APPROVED("device-approved"),
    DEVICE_NEEDS_APPROVAL("device-needs-approval"),
    DEVICE_VERIFICATION_LINK_EXPIRED("device-verification-link-expired"),
    DEVICE_VERIFICATION_EMAIL_SENT("device-verification-email-sent"),

    // RecencyCheckEventOnReceived
    PRIMARY_AUTH_FACTOR_NEEDS_VERIFICATION("Recency/auth-factor-needs-verification"),
    PRIMARY_AUTH_FACTOR_VERIFIED("Recency/auth-factor-verified"),
    RECENCY_INVALID_EMAIL_OTP("Recency/auth-factor-invalid-email-otp"),
    RECENCY_EMAIL_EXPIRED("Recency/auth-factor-verification-email-expired"),
    RECENCY_EMAIL_SENT("Recency/auth-factor-verification-email-sent"),
    RECENCY_EMAIL_NOT_DELIVERABLE("Recency/auth-factor-verification-email-not-deliverable"),

    // UpdateEmailEventOnReceived
    NEW_EMAIL_NEEDS_VERIFICATION("UpdateEmail/new-email-needs-verification"),
    EMAIL_UPDATED("UpdateEmail/email-updated"),
    UPDATE_EMAIL_INVALID_EMAIL_OTP("UpdateEmail/new-email-invalid-email-otp"),
    UPDATE_EMAIL_EXPIRED("UpdateEmail/new-email-verification-email-expired"),
    UPDATE_EMAIL_SENT("UpdateEmail/new-email-verification-email-sent"),
    UPDATE_EMAIL_NOT_DELIVERABLE("UpdateEmail/new-email-verification-email-not-deliverable"),
    INVALID_EMAIL("UpdateEmail/new-email-invalid"),
    EMAIL_ALREADY_EXISTS("UpdateEmail/new-email-already-exists"),

    // AuthEventOnReceived
    ID_TOKEN_CREATED("Auth/id-token-created"),

    // EnableMFAEventOnReceived
    MFA_SECRET_GENERATED("mfa-secret-generated"),
    ENABLE_INVALID_MFA_OTP("invalid-mfa-otp"),
    MFA_RECOVERY_CODES("mfa-recovery-codes");

    companion object {
        fun fromEvent(event: String): MagicEvent? {
            return values().find { it.event == event }
        }
    }

    override fun toString(): String {
        return event
    }
}
