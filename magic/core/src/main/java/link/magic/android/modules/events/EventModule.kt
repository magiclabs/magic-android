package link.magic.android.modules.events

import android.content.Context
import link.magic.android.core.provider.RpcProvider
import link.magic.android.modules.BaseModule
import link.magic.android.modules.wallet.response.DisconnectResponse
import org.web3j.protocol.core.Request
import java.util.concurrent.CompletableFuture

class EventModule(rpcProvider: RpcProvider) : BaseModule(rpcProvider) {
    // only available when allowAllEvents is true on the Magic constructor
    val CLOSE_MAGIC_WINDOW = "close-magic-window"
    val CLOSED_BY_USER = "closed-by-user-on-received"

    // LoginWithSmsOTPEventOnReceived
    val SMS_OTP_SENT = "sms-otp-sent"
    val INVALID_SMS_OTP = "invalid-sms-otp"
    val EXPIRED_SMS_OTP = "expired-sms-otp"

    // LoginWithEmailOTPEventOnReceived
    val EMAIL_OTP_SENT = "email-otp-sent"
    val INVALID_EMAIL_OTP = "invalid-email-otp"
    val INVALID_MFA_OTP = "invalid-mfa-otp"
    val EXPIRED_EMAIL_OTP = "expired-email-otp"
    val MFA_SENT_HANDLE = "mfa-sent-handle"

    // DeviceVerificationEventOnReceived
    val DEVICE_APPROVED = "device-approved"
    val DEVICE_NEEDS_APPROVAL = "device-needs-approval"
    val DEVICE_VERIFICATION_LINK_EXPIRED = "device-verification-link-expired"
    val DEVICE_VERIFICATION_EMAIL_SENT = "device-verification-email-sent"

    // RecencyCheckEventOnReceived
    val PRIMARY_AUTH_FACTOR_NEEDS_VERIFICATION = "Recency/auth-factor-needs-verification"
    val PRIMARY_AUTH_FACTOR_VERIFIED = "Recency/auth-factor-verified"
    val RECENCY_INVALID_EMAIL_OTP = "Recency/auth-factor-invalid-email-otp"
    val RECENCY_EMAIL_EXPIRED = "Recency/auth-factor-verification-email-expired"
    val RECENCY_EMAIL_SENT = "Recency/auth-factor-verification-email-sent"
    val RECENCY_EMAIL_NOT_DELIVERABLE = "Recency/auth-factor-verification-email-not-deliverable"

    // UpdateEmailEventOnReceived
    val NEW_EMAIL_NEEDS_VERIFICATION = "UpdateEmail/new-email-needs-verification"
    val EMAIL_UPDATED = "UpdateEmail/email-updated"
    val UPDATE_EMAIL_INVALID_EMAIL_OTP = "UpdateEmail/new-email-invalid-email-otp"
    val UPDATE_EMAIL_EXPIRED = "UpdateEmail/new-email-verification-email-expired"
    val UPDATE_EMAIL_SENT = "UpdateEmail/new-email-verification-email-sent"
    val UPDATE_EMAIL_NOT_DELIVERABLE = "UpdateEmail/new-email-verification-email-not-deliverable"
    val INVALID_EMAIL = "UpdateEmail/new-email-invalid"
    val EMAIL_ALREADY_EXISTS = "UpdateEmail/new-email-already-exists"

    // AuthEventOnReceived
    val ID_TOKEN_CREATED = "Auth/id-token-created"

    // EnableMFAEventOnReceived
    val MFA_SECRET_GENERATED = "mfa-secret-generated"
    val ENABLE_INVALID_MFA_OTP = "invalid-mfa-otp"
    val MFA_RECOVERY_CODES = "mfa-recovery-codes"

    fun emit(eventType: String, context: Context): CompletableFuture<DisconnectResponse> {
        provider.context = context
        val request = Request(Method.MAGIC_INTERMEDIARY_EVENT.toString(), listOf(mapOf("eventType" to eventType)), provider, DisconnectResponse::class.java)
        return provider.sendAsync(request, DisconnectResponse::class.java)
    }
}
