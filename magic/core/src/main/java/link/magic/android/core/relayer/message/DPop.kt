package link.magic.android.core.relayer.message

import android.util.Base64
import android.util.Log
import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.JWSObject
import com.nimbusds.jose.JWSSigner
import com.nimbusds.jose.Payload
import com.nimbusds.jose.crypto.ECDSASigner
import com.nimbusds.jose.jwk.Curve
import com.nimbusds.jose.jwk.ECKey
import link.magic.android.utils.KeyStoreKP
import java.security.interfaces.ECPublicKey
import java.util.UUID


class DPop {

    companion object {
        fun createJwt(): String {

            val keyPair = KeyStoreKP.getKeyPair()
            val ecJWK = ECKey.Builder(Curve.P_256, keyPair.public as ECPublicKey)
                .privateKey(keyPair.private)
                .build()
            val ecPublicJWK = ecJWK.toPublicJWK()

            // construct claims
            val iat = System.currentTimeMillis() / 1000
            val jti = UUID.randomUUID().toString().lowercase()

            val claims = mapOf("iat" to iat, "jti" to jti)

            val jwsObject = JWSObject(
                JWSHeader.Builder(JWSAlgorithm.ES256).type(JOSEObjectType("dpop+jwt")).jwk(ecPublicJWK).build(),
                Payload(claims)
            )

            // Create the EC signer
            val signer: JWSSigner = ECDSASigner(ecJWK)

            // Compute the EC signature
            jwsObject.sign(signer)

            // Serialize the JWS to compact form
            return jwsObject.serialize()
        }
    }
}
