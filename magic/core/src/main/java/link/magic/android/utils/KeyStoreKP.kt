package link.magic.android.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyStore
import java.security.PrivateKey
import java.security.spec.ECGenParameterSpec

class KeyStoreKP {

    companion object {
        private const val alias = "link.magic.auth.dpop"

        private fun generateKeyPair(): KeyPair {
            val keyPairGenerator = KeyPairGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_EC, "AndroidKeyStore"
            )

            keyPairGenerator.initialize(
                KeyGenParameterSpec.Builder(
                    this.alias,
                    KeyProperties.PURPOSE_SIGN or KeyProperties.PURPOSE_VERIFY
                )
                    .setDigests(KeyProperties.DIGEST_SHA256)
                    .setAlgorithmParameterSpec(ECGenParameterSpec("P-256"))
                    .build()
            )

            return keyPairGenerator.generateKeyPair()
        }


        internal fun getKeyPair(): KeyPair {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)

            val privateKey = keyStore.getKey(alias, null) ?: return generateKeyPair()
            val publicKey = keyStore.getCertificate(alias).publicKey
            return KeyPair(publicKey, privateKey as PrivateKey?)
        }

        private fun deleteKeyPair() {
            val keyStore = KeyStore.getInstance("AndroidKeyStore")
            keyStore.load(null)
            keyStore.deleteEntry(alias)
        }
    }
}