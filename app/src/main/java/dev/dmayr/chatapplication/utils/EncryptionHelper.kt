package dev.dmayr.chatapplication.utils

import android.util.Base64
import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object EncryptionHelper {
    private const val TRANSFORMATION = "AES/CBC/PKCS5Padding"
    private const val AES_KEY_LENGTH = 256

    /**
     * Generate a new AES key for encryption
     */
    fun generateKey(): SecretKey {
        val keyGenerator = KeyGenerator.getInstance("AES")
        keyGenerator.init(AES_KEY_LENGTH)
        return keyGenerator.generateKey()
    }

    /**
     * Convert SecretKey to Base64 string for storage
     */
    fun keyToString(key: SecretKey): String =
        Base64.encodeToString(key.encoded, Base64.DEFAULT)

    /**
     * Convert Base64 string back to SecretKey
     */
    fun stringToKey(keyString: String): SecretKey {
        val keyBytes = Base64.decode(keyString, Base64.DEFAULT)
        return SecretKeySpec(keyBytes, "AES")
    }

    /**
     * Encrypt message with given key
     * Returns: "iv:encryptedData" format
     */
    fun encrypt(message: String, key: SecretKey): String {
        val cipher = Cipher.getInstance(TRANSFORMATION)

        // Generate random IV
        val iv = ByteArray(16)
        SecureRandom().nextBytes(iv)
        val ivSpec = IvParameterSpec(iv)

        cipher.init(Cipher.ENCRYPT_MODE, key, ivSpec)
        val encryptedBytes = cipher.doFinal(message.toByteArray())

        val ivBase64 = Base64.encodeToString(iv, Base64.DEFAULT)
        val encryptedBase64 = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)

        return "$ivBase64:$encryptedBase64"
    }

    /**
     * Decrypt message with given key
     * Expects format: "iv:encryptedData"
     */
    fun decrypt(encryptedMessage: String, key: SecretKey): String {
        val parts = encryptedMessage.split(":")
        if (parts.size != 2) throw IllegalArgumentException("Invalid encrypted message format")

        val iv = Base64.decode(parts[0], Base64.DEFAULT)
        val encryptedData = Base64.decode(parts[1], Base64.DEFAULT)

        val cipher = Cipher.getInstance(TRANSFORMATION)
        val ivSpec = IvParameterSpec(iv)

        cipher.init(Cipher.DECRYPT_MODE, key, ivSpec)
        val decryptedBytes = cipher.doFinal(encryptedData)

        return String(decryptedBytes)
    }
}
