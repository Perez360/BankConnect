package com.codex.util

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.*


class PasswordOperations {

    companion object {

        // Specify the key length (128, 192, or 256 bits)
        private const val KEYLENGTH = 128 // or 192 or 256
        private const val ALGORITHM = "SHA-256"
        fun encrypt(passwordToEncrypt: String): String {
            val secureRandom = SecureRandom()
            val salt = ByteArray(KEYLENGTH)
            secureRandom.setSeed(salt)
            val messageDigest = MessageDigest.getInstance(ALGORITHM)
            messageDigest.update(salt)

            return messageDigest.digest(passwordToEncrypt.toByteArray(StandardCharsets.UTF_8)).toString()
        }

        fun verifyPassword(inputtedPassword: String): Boolean {
            val salt = ByteArray(KEYLENGTH)
            val messageDigest = MessageDigest.getInstance(ALGORITHM)
            messageDigest.update(salt)

            val storedPassword = messageDigest.digest(inputtedPassword.toByteArray(StandardCharsets.UTF_8))

            return Objects.deepEquals(storedPassword, inputtedPassword)
        }
    }
}