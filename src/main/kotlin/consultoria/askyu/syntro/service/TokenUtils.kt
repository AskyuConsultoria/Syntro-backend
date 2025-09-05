package consultoria.askyu.syntro.service

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64

object TokenUtils {
    private val secureRandom = SecureRandom()
    private val b64url = Base64.getUrlEncoder().withoutPadding()

    fun generateToken(bytes: Int = 32): String {
        val buf = ByteArray(bytes)
        secureRandom.nextBytes(buf)
        return b64url.encodeToString(buf)
    }

    fun sha256Hex(input: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val out = md.digest(input.toByteArray(Charsets.UTF_8))
        return out.joinToString("") { "%02x".format(it) }
    }
}
