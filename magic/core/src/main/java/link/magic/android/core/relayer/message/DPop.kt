package link.magic.android.core.relayer.message

import android.util.Base64
import android.util.Log
import link.magic.android.utils.KeyStoreKP
import org.json.JSONObject
import java.nio.charset.StandardCharsets
import java.security.Signature
import java.security.interfaces.ECPublicKey
import java.util.*
import kotlin.collections.HashMap

class DPop {

    companion object {
        fun createJwt(): String {

            val keyPair = KeyStoreKP.generateKeyPair()
            val publicKey = keyPair.public as ECPublicKey
            val privateKey = keyPair.private

            val xCoordinateBase64 = base64UrlEncode(publicKey.w.affineX.toByteArray())
            val yCoordinateBase64 = base64UrlEncode(publicKey.w.affineY.toByteArray())

            val headers: MutableMap<Any?, Any?> = HashMap()
            headers["typ"] = "dpop+jwt"
            headers["alg"] = "ES256"

            val jwkMap: MutableMap<Any?, Any?> = HashMap()
            jwkMap["kty"] = "EC"
            jwkMap["crv"] = "P-256"
            jwkMap["x"] = xCoordinateBase64
            jwkMap["y"] = yCoordinateBase64

            headers["jwk"] = jwkMap

            val iat = System.currentTimeMillis() / 1000
            val jti = UUID.randomUUID().toString().lowercase()

            val claims = mapOf("iat" to iat, "jti" to jti)

            val headersJson = JSONObject(headers)
            val claimsJson = JSONObject(claims)

            val headersB64 = base64UrlEncode(headersJson.toString().toByteArray())
            val claimsB64 = base64UrlEncode(claimsJson.toString().toByteArray())

            val signingInput = "$headersB64.$claimsB64"
            val signingInputData = signingInput.toByteArray(StandardCharsets.UTF_8)

            Log.d("signingInput", signingInput)

            // Sign
            val signature: ByteArray = try {
                val signer = Signature.getInstance("SHA256withECDSA")
                signer.initSign(privateKey)
                signer.update(signingInputData)
                signer.sign()
            } catch (e: Exception) {
                throw KSKP(e)
            }

            val signatureB64 = base64UrlEncode(signature)

            Log.d("signature", "$signingInput.$signatureB64")

            return "$signingInput.$signatureB64"
        }

        private fun base64UrlEncode(data: ByteArray): String {
            return Base64.encodeToString(data, Base64.URL_SAFE or Base64.NO_WRAP)
        }
    }
}

class KSKP(e: Exception) : Throwable() {

}