package com.codex.util

import java.security.*
import java.util.*
import javax.crypto.*
import javax.crypto.spec.IvParameterSpec


//class PasswordOperations {
//
//    companion object {
//        private const val ALGORITHM = "AES"
//        private const val SECRETKEY: String = " 123456789"
//        val PADDINGSCHEME = "AES/CBC/PKCS5Padding"
//
//        // Specify the key length (128, 192, or 256 bits)
//        private var KEYLENGTH = 128 // or 192 or 256
//
//        fun encrypt(password: String): String {
//            val key: Key = generateAESKey()
//            val cipher = Cipher.getInstance(ALGORITHM)
//            cipher.init(Cipher.ENCRYPT_MODE, key)
//            val encryptedValue = cipher.doFinal(password.toByteArray())
//            return Base64.getEncoder().encodeToString(encryptedValue)
//        }
//
//        fun decrypt(encryptedPassword: String): String {
//            val key: Key = generateAESKey()
//            val cipher = Cipher.getInstance(PADDINGSCHEME)
//            cipher.init(Cipher.DECRYPT_MODE, key)
//            val decryptedValue = cipher.doFinal(Base64.getDecoder().decode(encryptedPassword))
//            return String(decryptedValue)
//        }
//
//        private fun generateAESKey(): Key {
//
//            val keyGenerator = KeyGenerator.getInstance("AES")
//            keyGenerator.init(KEYLENGTH) // Initialize with the desired key length
//
//            return keyGenerator.generateKey()
//        }
//
//    }
//}


class PasswordOperations {

    companion object {

        // Specify the key length (128, 192, or 256 bits)
        private const val KEYLENGTH = 128 // or 192 or 256
        private const val ALGORITHM = "AES/CBC/PKCS5Padding"

        @Throws(
            NoSuchPaddingException::class,
            NoSuchAlgorithmException::class,
            InvalidAlgorithmParameterException::class,
            InvalidKeyException::class,
            BadPaddingException::class,
            IllegalBlockSizeException::class
        )
        fun encrypt(password: String): String {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.ENCRYPT_MODE, generateAESKey(), generateIv())
            val cipherText = cipher.doFinal(password.toByteArray())
            return Base64.getEncoder()
                .encodeToString(cipherText)
        }


        @Throws(
            NoSuchPaddingException::class,
            NoSuchAlgorithmException::class,
            InvalidAlgorithmParameterException::class,
            InvalidKeyException::class,
            BadPaddingException::class,
            IllegalBlockSizeException::class
        )
        fun decrypt(password: String): String {
            val cipher = Cipher.getInstance(ALGORITHM)
            cipher.init(Cipher.DECRYPT_MODE, generateAESKey(), generateIv())
            val plainText = cipher.doFinal(
                Base64.getDecoder()
                    .decode(password)
            )
            return String(plainText)
        }

        private fun generateAESKey(): Key {

            val keyGenerator = KeyGenerator.getInstance("AES")
            keyGenerator.init(KEYLENGTH) // Initialize with the desired key length

            return keyGenerator.generateKey()
        }

        private fun generateIv(): IvParameterSpec {
            val iv = ByteArray(16)
            SecureRandom().nextBytes(iv)
            return IvParameterSpec(iv)
        }
    }
}