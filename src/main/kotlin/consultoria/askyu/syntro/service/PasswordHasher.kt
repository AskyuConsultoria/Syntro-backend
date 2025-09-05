package consultoria.askyu.syntro.service

import de.mkammerer.argon2.Argon2Factory
import org.springframework.stereotype.Component

@Component
class PasswordHasher {
    private val argon2 = Argon2Factory.create(
        Argon2Factory.Argon2Types.ARGON2id, 32, 64
    )

    private val iterations = 3
    private val memoryKb = 19456 // ~19MB
    private val parallelism = 1

    fun hash(raw: String): String = argon2.hash(iterations, memoryKb, parallelism, raw.toCharArray())
    fun verify(hash: String, raw: String): Boolean = argon2.verify(hash, raw.toCharArray())
}
