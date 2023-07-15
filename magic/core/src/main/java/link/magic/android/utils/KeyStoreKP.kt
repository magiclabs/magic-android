package link.magic.android.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey

import com.nimbusds.jose.jwk.RSAKey
import java.security.interfaces.ECPrivateKey
import java.security.interfaces.ECPublicKey
import java.security.spec.ECGenParameterSpec
import java.util.*

class KeyStoreKP {

    companion object {
        private const val alias = "link.magic.auth.dpop"

        internal fun generateKeyPair(): KeyPair {
            val keyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore"
            )

            keyPairGenerator.initialize(
                KeyGenParameterSpec.Builder(
                    this.alias,
                    KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
                )
                    .setDigests(KeyProperties.DIGEST_SHA256, KeyProperties.DIGEST_SHA512)
                    .setAlgorithmParameterSpec(ECGenParameterSpec("secp256r1"))
                    .build()
            )

            return keyPairGenerator.generateKeyPair()
        }


        private fun getKeyPair(alias: String): KeyPair {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)

            val privateKey = keyStore.getKey(alias, null) as ECPrivateKey
            val publicKey = keyStore.getCertificate(alias).publicKey as ECPublicKey
            return KeyPair(publicKey, privateKey)
        }


        internal fun getJWK(alias: String): RSAKey {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)

            val privateKey = keyStore.getKey(alias, null) as RSAPrivateKey
            val publicKey = keyStore.getCertificate(alias).publicKey as RSAPublicKey

            return RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build()
        }
    }

}