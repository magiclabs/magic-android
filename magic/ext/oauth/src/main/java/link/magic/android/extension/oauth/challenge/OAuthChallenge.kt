package link.magic.android.extension.oauth.challenge

import android.util.Base64
import java.security.MessageDigest
import java.util.*

class OAuthChallenge {
    var state: String
    val verifier: String
    val challenge: String

    private val letters :String = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789-._~"

    init {
        state = createRandomString(128)
        verifier = createRandomString(128)
        challenge = hexToBase64URLSafe(hash(verifier))
    }

    private fun createRandomString(size: Int): String {
        val random = Random()
        val randomStringBuilder = StringBuilder()
        for (i in 0 until size) {
            val index: Int = random.nextInt(letters.length)
            val tempChar: Char = letters[index]
            randomStringBuilder.append(tempChar)
        }
        return randomStringBuilder.toString()
    }

    private fun hexToBase64URLSafe(hexString: ByteArray): String {
        val base64String = Base64.encodeToString(hexString, Base64.DEFAULT)
        return base64String.replace("=", "").replace("+", "-").replace("/", "_")
    }

    private fun hash(string: String): ByteArray {
        return MessageDigest
                .getInstance("SHA-256")
                .digest(string.toByteArray())
    }
}
