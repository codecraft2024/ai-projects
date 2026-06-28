package com.ghosttalk.core.encryption

import javax.inject.Inject
import javax.inject.Singleton

/**
 * Pass-through implementation until Signal Protocol is integrated.
 */
@Singleton
class NoOpEncryptionProvider @Inject constructor() : EncryptionProvider {
    override fun encrypt(plainText: String, recipientPublicKey: String): String = plainText
    override fun decrypt(cipherText: String, senderPublicKey: String): String = cipherText
}
